package ua.testing.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.testing.authorization.entity.Way;

import java.util.Optional;

@Repository
public interface WayRepository extends JpaRepository<Way, Long> {
    Optional<Way> findByLocalitySand_IdAndLocalityGet_Id(long localitySandID, long localityGetID);
}
