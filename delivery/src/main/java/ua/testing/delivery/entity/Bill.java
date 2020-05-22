package ua.testing.delivery.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class Bill {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id", referencedColumnName = "id", nullable = false)
    Delivery delivery;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
        boolean q = id == bill.id;
        boolean s = isDeliveryPaid == bill.isDeliveryPaid;
        boolean a = costInCents == bill.costInCents;
        boolean w = user.equals(bill.user);
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