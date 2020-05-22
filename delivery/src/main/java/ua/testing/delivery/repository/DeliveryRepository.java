package ua.testing.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.testing.delivery.entity.Delivery;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findAllByBill_User_IdAndIsPackageReceivedFalse(long bill_user_id);

    Optional<Delivery> findByIdAndBill_User_IdAndIsPackageReceivedFalse(long id, long addressee_id);

//    List<Delivery> findAllByIsPackageReceivedFalseAndIsDeliveryPaidTrueAndAddressee_Id(Long addressee_id);
//
//    List<Delivery> findAllByIsDeliveryPaidFalseAndAddresser_Id(Long addresser_id);
//
//    Page<Delivery> findAllByAddressee_IdOrAddresser_Id(Long addressee_id, Long addresser_id, Pageable pageable);
}