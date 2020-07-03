package ua.testing.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.testing.delivery.entity.Locality;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocalityRepository extends JpaRepository<Locality, Long> {
    Optional<Locality> findById(long id);

    @Query("SELECT way.localityGet FROM Way way WHERE way.localitySand.id= :idToSearch")
    List<Locality> findGetLocalitiesByLocalitySetId(@Param("idToSearch") long idToSearch);
}
