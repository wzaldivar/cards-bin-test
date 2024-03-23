package group.etraveli.card.cost.repositories;

import java.math.BigDecimal;
import java.util.Optional;

public interface DefaultCostRepository {
    Optional<BigDecimal> findCost();

    public BigDecimal upsertDefaultCost(BigDecimal cost);
}
