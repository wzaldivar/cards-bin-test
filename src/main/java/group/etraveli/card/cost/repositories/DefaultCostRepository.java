package group.etraveli.card.cost.repositories;

import group.etraveli.card.cost.models.DefaultCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultCostRepository extends JpaRepository<DefaultCost, Integer>, CustomDefaultCostRepository{
}
