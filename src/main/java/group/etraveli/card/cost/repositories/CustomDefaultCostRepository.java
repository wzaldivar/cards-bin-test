package group.etraveli.card.cost.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface CustomDefaultCostRepository {
    public Optional<BigDecimal> findCost();

    public BigDecimal upsertDefaultCost(BigDecimal cost);
}
