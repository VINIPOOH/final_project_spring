package ua.testing.authorization.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.testing.authorization.entity.Bill;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findAllByUserIdAndIsDeliveryPaidFalse(Long user_id);

    Page<Bill> findAllByUserIdAndIsDeliveryPaidTrue(Long user_id, Pageable pageable);

    Optional<Bill> findByIdAndIsDeliveryPaidFalse(long bill_id);
}
