package group.etraveli.card.cost.services;

import group.etraveli.card.cost.models.CostData;
import group.etraveli.card.cost.models.CountryCost;
import group.etraveli.card.cost.models.IINCountryMapping;
import group.etraveli.card.cost.repositories.CountryCostsRepository;
import group.etraveli.card.cost.repositories.DefaultCostRepository;
import group.etraveli.card.cost.repositories.IINCountryMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CostsCacheableService {
    private final CountryCostsRepository countryCostsRepository;
    private final DefaultCostRepository defaultCostRepository;
    private final IINCountryMappingRepository iinCountryMappingRepository;

    @Autowired
    public CostsCacheableService(CountryCostsRepository countryCostsRepository,
                                 DefaultCostRepository defaultCostRepository,
                                 IINCountryMappingRepository iinCountryMappingRepository) {
        this.countryCostsRepository = countryCostsRepository;
        this.defaultCostRepository = defaultCostRepository;
        this.iinCountryMappingRepository = iinCountryMappingRepository;
    }


    @Cacheable(value = "countryCostCache", key = "#countryCode.toUpperCase()", unless = "#result == null")
    public CountryCost findByCountryCodeIgnoreCase(String countryCode) {
        return countryCostsRepository.findByCountryCodeIgnoreCase(countryCode).orElse(null);
    }

    @CachePut(value = "countryCostCache", key = "#countryCode.toUpperCase()")
    public CountryCost upsertCountryCost(String countryCode, BigDecimal cost) {
        return countryCostsRepository.upsertCountryCost(countryCode, cost);
    }

    @CachePut(value = "countryCostCache", key = "#countryCost.countryCode")
    public CountryCost upsertCountryCost(CountryCost countryCost) {
        return countryCostsRepository.upsertCountryCost(countryCost);
    }

    @CacheEvict(value = "countryCostCache", key = "#countryCode.toUpperCase()")
    public boolean removeByCountryCodeIgnoreCase(String countryCode) {
        return countryCostsRepository.removeByCountryCodeIgnoreCase(countryCode) > 0;
    }

    @CachePut(value = "defaultCostCache", key = "'value'")
    public BigDecimal upsertDefaultCost(CostData costData) {
        return defaultCostRepository.upsertDefaultCost(costData.getCost());
    }

    @Cacheable(value = "defaultCostCache", key = "'value'", unless = "#result == null")
    public BigDecimal getDefaultCost() {
        return defaultCostRepository.findCost().orElse(null);
    }

    @Cacheable(value = "countryMappingCache", key = "#iin", unless = "#result == null")
    public String findByIIN(String iin) {
        Optional<IINCountryMapping> countryMapping = iinCountryMappingRepository.findByIin(iin);
        return countryMapping.map(IINCountryMapping::getCountryCode).orElse(null);
    }
}
