package ua.testing.delivery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
/**
 * Represent Bill table in db
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bill {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id", referencedColumnName = "id", nullable = false)
    Delivery delivery;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(columnDefinition = "BIT(1) default 0")
    private boolean isDeliveryPaid;

    @Column(nullable = false)
    private long costInCents;

    private LocalDate dateOfPay;

    @Override
    public String toString() {
        return "Bill{}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return id == bill.id &&
                isDeliveryPaid == bill.isDeliveryPaid &&
                costInCents == bill.costInCents &&
                user.equals(bill.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, isDeliveryPaid, costInCents, dateOfPay);
    }
}
