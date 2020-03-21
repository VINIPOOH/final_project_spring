package ua.testing.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.testing.authorization.entity.Delivery;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findAllByIsPackageReceivedFalseAndAddressee_Id(Long addressee_id);

    List<Delivery> findAllByIsPackageReceivedFalseAndAddresser_Id(Long addresser_id);

}
