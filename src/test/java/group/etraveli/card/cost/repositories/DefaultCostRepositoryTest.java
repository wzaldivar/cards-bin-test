package group.etraveli.card.cost.repositories;

import group.etraveli.card.cost.models.CountryCost;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
@Testcontainers
public class DefaultCostRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:alpine")
            .withUsername("user")
            .withPassword("password")
            .withDatabaseName("test_db");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        postgres.start();
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    DefaultCostRepository defaultCostRepository;

    @Test
    public void noValue() {
        defaultCostRepository.deleteAll();
        Optional<BigDecimal> defaultCost = defaultCostRepository.findCost();
        Assertions.assertTrue(defaultCost.isEmpty());
    }

    @Test
    public void newValue() {
        defaultCostRepository.deleteAll();
        Optional<BigDecimal> defaultCost = defaultCostRepository.findCost();
        Assertions.assertTrue(defaultCost.isEmpty());

        BigDecimal newValue = BigDecimal.valueOf(22.56);

        BigDecimal result = defaultCostRepository.upsertDefaultCost(newValue);
        Assertions.assertEquals(result, newValue);

        defaultCost = defaultCostRepository.findCost();
        Assertions.assertTrue(defaultCost.isPresent());
        Assertions.assertEquals(defaultCost.get(), newValue);

        newValue = BigDecimal.valueOf(56.42);

        result = defaultCostRepository.upsertDefaultCost(newValue);
        Assertions.assertEquals(result, newValue);

        defaultCost = defaultCostRepository.findCost();
        Assertions.assertTrue(defaultCost.isPresent());
        Assertions.assertEquals(defaultCost.get(), newValue);
    }
}
