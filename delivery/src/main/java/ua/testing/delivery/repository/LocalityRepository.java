package ua.testing.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.testing.delivery.entity.Locality;

import java.util.Optional;

@Repository
public interface LocalityRepository extends JpaRepository<Locality, Long> {
    Optional<Locality> findById(long id);
}
