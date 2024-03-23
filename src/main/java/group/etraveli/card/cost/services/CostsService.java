package group.etraveli.card.cost.services;

import group.etraveli.card.cost.models.CountryCost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CostsService {

    private final CostsCacheableService cacheableService;
    private final BinListService binListService;

    @Autowired
    public CostsService(CostsCacheableService cacheableService,
                        BinListService binListService) {
        this.cacheableService = cacheableService;
        this.binListService = binListService;
    }

    public CountryCost getByCountryCode(String countryCode) {
        if (countryCode == null){
            return null;
        }

        CountryCost countryCost = cacheableService.findByCountryCodeIgnoreCase(countryCode);

        if (countryCost != null) {
            return countryCost;
        }

        BigDecimal defaultCost = cacheableService.getDefaultCost();

        if (defaultCost == null) {
            return null;
        }

        return new CountryCost(countryCode, defaultCost);
    }

    public CountryCost findByIIN(String iin) {
        String countryCode = cacheableService.findByIIN(iin);

        if (countryCode == null) {
            binListService.retrieveCountryCode(iin, UUID.randomUUID().toString(), false);
        }

        if(countryCode == null){
            return null;
        }

        return getByCountryCode(countryCode);
    }
}
