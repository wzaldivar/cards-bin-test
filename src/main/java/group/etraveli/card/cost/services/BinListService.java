package group.etraveli.card.cost.services;

import com.rabbitmq.client.Channel;
import group.etraveli.card.cost.models.BinListResponse;
import group.etraveli.card.cost.models.TaskData;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

@Service
public class BinListService {

    private static final int REQUEST_LIMIT = 5;

    private static final Duration REFILL_INTERVAL = Duration.ofHours(1);

    private final Queue<Instant> requestTimes = new LinkedList<>();

    private final RabbitTemplate rabbitTemplate;

    @Qualifier(value = "tasks_queue")
    private final org.springframework.amqp.core.Queue tasksQueue;

    @Qualifier(value = "responses_queue")
    private final org.springframework.amqp.core.Queue responsesQueue;


    @Autowired
    public BinListService(RabbitTemplate rabbitTemplate,
                          org.springframework.amqp.core.Queue tasksQueue,
                          org.springframework.amqp.core.Queue responsesQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.tasksQueue = tasksQueue;
        this.responsesQueue = responsesQueue;
    }

    public synchronized boolean isAbleToRequest() {
        Instant now = Instant.now();

        while (!requestTimes.isEmpty() && requestOutOfTimeInterval(requestTimes.peek(), now)) {
            requestTimes.poll();
        }

        if (requestTimes.size() < REQUEST_LIMIT) {
            requestTimes.add(now);
            return true;
        }

        return false;
    }

    private synchronized void lockRequests() {
        Instant now = Instant.now();
        requestTimes.clear();
        while (requestTimes.size() < REQUEST_LIMIT) {
            requestTimes.add(now);
        }
    }

    private boolean requestOutOfTimeInterval(Instant peek, Instant now) {
        return peek.plus(REFILL_INTERVAL).isBefore(now);
    }

    public String retrieveCountryCode(String iin, String correlationKey, boolean preVerified) {
        if (preVerified || isAbleToRequest()) {
            try {
                BinListResponse binListResponse = callExternalService(iin);
                return binListResponse.getCountry().getCountryCode();
            } catch (WebClientResponseException ex) {
                if (ex.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                    lockRequests();
                    return retrieveCountryCode(iin, correlationKey, false);
                }
            }
        } else {
            rabbitTemplate.convertAndSend(tasksQueue.getName(),
                    new TaskData(iin, Instant.now()),
                    message -> {
                        message.getMessageProperties().setCorrelationId(correlationKey);
                        return message;
                    });

            String response = rabbitTemplate.receiveAndConvert(
                    responsesQueue.getName(),
                    5000,
                    ParameterizedTypeReference.forType(String.class));

            return response;
        }
    }

    public BinListResponse callExternalService(String iin) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://lookup.binlist.net/")
                .defaultHeader("Accept-Version", "3")
                .build();

        Mono<BinListResponse> responseMono = webClient.get()
                .uri(iin)
                .retrieve()
                .onStatus(
                        httpStatusCode -> httpStatusCode.value() == HttpStatus.TOO_MANY_REQUESTS.value(),
                        clientResponse -> Mono.error(new WebClientResponseException(
                                HttpStatus.TOO_MANY_REQUESTS.value(),
                                "Requests limit exceded", null, null, null))
                )
                .bodyToMono(BinListResponse.class)
                .timeout(Duration.ofSeconds(5));

        return responseMono.block();
    }

    @RabbitListener(queues = "tasks_queue")
    public void handleTaskMessage(TaskData taskData, Channel channel, @Payload Message message) throws IOException {
        if (isAbleToRequest()) {
            String correlationKey = message.getMessageProperties().getCorrelationId();
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

            if (taskData.getTimeStamp().plus(Duration.ofSeconds(5)).isAfter(Instant.now())) {
                rabbitTemplate.convertAndSend("responses_queue", null, response -> {
                    response.getMessageProperties().setCorrelationId(correlationKey);
                    return response;
                });
            } else{
                String countryCode = retrieveCountryCode(taskData.getIin(), correlationKey, true);
                rabbitTemplate.convertAndSend("responses_queue", countryCode, response -> {
                    response.getMessageProperties().setCorrelationId(correlationKey);
                    return response;
                });
            }
        } else {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}
