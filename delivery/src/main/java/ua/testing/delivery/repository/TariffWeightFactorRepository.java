package ua.testing.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.testing.delivery.entity.TariffWeightFactor;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Repository
public interface TariffWeightFactorRepository extends JpaRepository<TariffWeightFactor, Long> {
}
