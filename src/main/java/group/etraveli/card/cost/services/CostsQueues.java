package group.etraveli.card.cost.services;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CostsQueues {
    @Bean
    @Qualifier(value = "tasks_queue")
    public Queue tasksQueue() {
        return new Queue("tasks_queue");
    }

    @Bean
    @Qualifier("responses_queue")
    public Queue responsesQueue() {
        return new Queue("responses_queue");
    }
}
