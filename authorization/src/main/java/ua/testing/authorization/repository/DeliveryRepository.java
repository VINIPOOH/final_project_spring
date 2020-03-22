package ua.testing.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.testing.authorization.entity.Delivery;

import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findAllByIsPackageReceivedFalseAndAddressee_Id(Long addressee_id);


}
