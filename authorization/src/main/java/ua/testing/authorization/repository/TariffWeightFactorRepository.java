package ua.testing.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.testing.authorization.entity.TariffWeightFactor;

@Repository
public interface TariffWeightFactorRepository extends JpaRepository<TariffWeightFactor, Long> {
}
