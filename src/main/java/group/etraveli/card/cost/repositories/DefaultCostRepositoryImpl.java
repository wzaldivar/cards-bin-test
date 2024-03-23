package group.etraveli.card.cost.repositories;

import group.etraveli.card.cost.models.DefaultCost;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public class DefaultCostRepositoryImpl implements DefaultCostRepository {

    static final int DEFAULT_COST_ID = 1;

    @PersistenceContext
    EntityManager manager;

    private Optional<DefaultCost> findDefaultCost() {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<DefaultCost> criteriaQuery = criteriaBuilder.createQuery(DefaultCost.class);

        Root<DefaultCost> root = criteriaQuery.from(DefaultCost.class);

        criteriaQuery.select(root);

        return manager.createQuery(criteriaQuery).setMaxResults(1).getResultStream().findFirst();
    }

    @Override
    public Optional<BigDecimal> findCost() {
        Optional<DefaultCost> defaultCost = findDefaultCost();
        return defaultCost.map(DefaultCost::getCost);
    }

    @Override
    @Transactional
    public BigDecimal upsertDefaultCost(BigDecimal cost) {
        Optional<DefaultCost> defaultCost = findDefaultCost();
        if (defaultCost.isEmpty()) {
            manager.persist(
                    new DefaultCost(DEFAULT_COST_ID, cost)
            );
        } else {
            DefaultCost currentDefaultCost = defaultCost.get();
            currentDefaultCost.setCost(cost);
            manager.merge(currentDefaultCost);
        }
        return cost;
    }
}
