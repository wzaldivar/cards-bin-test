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

@SpringBootTest
@Testcontainers
public class CountryCostRepositoryTest {

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
    CountryCostsRepository countryCostsRepository;

    static final String COUNTRY_CODE = "UY";

    @Test
    public void insertNew(){
        countryCostsRepository.deleteAll();

        BigDecimal cost = BigDecimal.valueOf(34.52);

        CountryCost countryCost = countryCostsRepository.upsertCountryCost(COUNTRY_CODE, cost);

        Assertions.assertEquals(countryCost.getCountryCode(), COUNTRY_CODE);
        Assertions.assertEquals(countryCost.getCost(), cost);
    }

    @Test
    public void updateExistent(){
        countryCostsRepository.deleteAll();

        BigDecimal cost = BigDecimal.valueOf(34.52);

        CountryCost countryCost = countryCostsRepository.upsertCountryCost(COUNTRY_CODE, cost);

        BigDecimal newCost = BigDecimal.valueOf(50.52);

        countryCost = countryCostsRepository.upsertCountryCost(COUNTRY_CODE, newCost);

        Assertions.assertEquals(countryCost.getCountryCode(), COUNTRY_CODE);
        Assertions.assertEquals(countryCost.getCost(), newCost);
    }
}
