package ua.testing.delivery.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.testing.delivery.entity.Bill;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findAllByUserIdAndIsDeliveryPaidFalse(long userId);

    Page<Bill> findAllByUserIdAndIsDeliveryPaidTrue(long userId, Pageable pageable);

    Optional<Bill> findByIdAndIsDeliveryPaidFalse(long billId);
}
