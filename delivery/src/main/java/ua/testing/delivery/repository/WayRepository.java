package ua.testing.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.testing.delivery.entity.Way;

import java.util.Optional;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Repository
public interface WayRepository extends JpaRepository<Way, Long> {
    Optional<Way> findByLocalitySand_IdAndLocalityGet_Id(long localitySandID, long localityGetID);
}
