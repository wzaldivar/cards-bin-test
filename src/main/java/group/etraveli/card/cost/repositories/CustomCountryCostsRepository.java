package group.etraveli.card.cost.repositories;

import group.etraveli.card.cost.models.CountryCost;

import java.math.BigDecimal;

public interface CustomCountryCostsRepository {
    CountryCost upsertCountryCost(String countryCode, BigDecimal cost);

    CountryCost upsertCountryCost(CountryCost countryCost);
}
