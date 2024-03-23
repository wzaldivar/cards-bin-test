package group.etraveli.card.cost;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableRabbit
public class CardCostApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardCostApiApplication.class, args);
    }

}
