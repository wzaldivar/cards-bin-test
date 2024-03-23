package group.etraveli.card.cost.repositories;

import group.etraveli.card.cost.models.IINCountryMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IINCountryMappingRepository extends JpaRepository<IINCountryMapping, String> {
    public Optional<IINCountryMapping> findByIin(String iin);
}
