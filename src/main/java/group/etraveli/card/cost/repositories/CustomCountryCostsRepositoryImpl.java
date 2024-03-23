package group.etraveli.card.cost.repositories;

import group.etraveli.card.cost.models.CountryCost;
import group.etraveli.card.cost.repositories.CustomCountryCostsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class CustomCountryCostsRepositoryImpl implements CustomCountryCostsRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional
    public CountryCost upsertCountryCost(String countryCode, BigDecimal cost) {
        CountryCost countryCost = manager.find(CountryCost.class, countryCode);

        if (countryCost != null) {
            countryCost.setCost(cost);
            manager.merge(countryCost);
        } else {
            countryCost = new CountryCost(countryCode, cost);
            manager.persist(countryCost);
        }

        return countryCost;
    }

    @Override
    @Transactional
    public CountryCost upsertCountryCost(CountryCost countryCost) {
        return manager.merge(countryCost);
    }
}
