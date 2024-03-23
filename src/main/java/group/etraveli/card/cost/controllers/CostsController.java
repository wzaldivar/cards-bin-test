package group.etraveli.card.cost.controllers;

import group.etraveli.card.cost.models.CardData;
import group.etraveli.card.cost.models.CostData;
import group.etraveli.card.cost.models.CountryCost;
import group.etraveli.card.cost.services.CostsCacheableService;
import group.etraveli.card.cost.services.CostsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class CostsController {

    private final CostsCacheableService costsCacheableService;
    private final CostsService costsService;

    @Autowired
    public CostsController(CostsCacheableService costsCacheableService,
                           CostsService costsService) {
        this.costsCacheableService = costsCacheableService;
        this.costsService = costsService;
    }

    @GetMapping("/costs/{countryCode}")
    public ResponseEntity<CountryCost> getCostByCountryCode(@PathVariable String countryCode) {
        CountryCost countryCost = costsService.getByCountryCode(countryCode);

        if (countryCost == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(countryCost);
    }

    @PutMapping("/costs/{countryCode}")
    public CountryCost putCostByCountryCode(@PathVariable String countryCode,
                                            @RequestBody CostData costData) {
        return costsCacheableService.upsertCountryCost(countryCode, costData.getCost());
    }

    @DeleteMapping("/costs/{countryCode}")
    public ResponseEntity<Void> removeCostByCountryCode(@PathVariable String countryCode) {
        if (costsCacheableService.removeByCountryCodeIgnoreCase(countryCode)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/costs")
    public CountryCost postCountryCost(@Valid @RequestBody CountryCost countryCost) {
        return costsCacheableService.upsertCountryCost(countryCost);
    }

    @GetMapping("/costs/default")
    public ResponseEntity<CostData> getDefaultCost() {
        BigDecimal defaultCost = costsCacheableService.getDefaultCost();
        if (defaultCost == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(new CostData(defaultCost));
        }
    }

    @PutMapping("/costs/default")
    public CostData upsertDefaultCost(@Valid @RequestBody CostData costData) {
        return new CostData(costsCacheableService.upsertDefaultCost(costData));
    }

    @PostMapping("/payment-cards-cost")
    public ResponseEntity<CountryCost> paymentCardCost(@Valid @RequestBody CardData cardData) {
        CountryCost result = costsService.findByIIN(cardData.getIIN());
        if (result == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }
}
