package group.etraveli.card.cost.repositories;

import group.etraveli.card.cost.models.CountryCost;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryCostsRepository extends JpaRepository<CountryCost, String>, CustomCountryCostsRepository {
    Optional<CountryCost> findByCountryCodeIgnoreCase(String countryCode);

    @Transactional
    int removeByCountryCodeIgnoreCase(String countryCode);
}
