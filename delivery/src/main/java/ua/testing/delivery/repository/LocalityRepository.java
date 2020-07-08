package ua.testing.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.testing.delivery.entity.Locality;

import java.util.List;
import java.util.Optional;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Repository
public interface LocalityRepository extends JpaRepository<Locality, Long> {
    Optional<Locality> findById(long id);

    /**
     * @param idToSearch is id locality from which way takes begin
     * @return list of localities in which can be sand delivery from locality with {@param idToSearch}
     */
    @Query("SELECT way.localityGet FROM Way way WHERE way.localitySand.id= :idToSearch")
    List<Locality> findGetLocalitiesByLocalitySandId(@Param("idToSearch") long idToSearch);
}
