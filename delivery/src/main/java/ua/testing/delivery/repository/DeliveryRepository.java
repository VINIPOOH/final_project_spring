package ua.testing.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.testing.delivery.entity.Delivery;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findAllByBill_User_IdAndIsPackageReceivedFalse(long billUserId);

    Optional<Delivery> findByIdAndBill_User_IdAndIsPackageReceivedFalse(long id, long addresseeId);

}
