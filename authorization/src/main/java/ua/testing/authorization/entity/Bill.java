package ua.testing.authorization.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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
    private Long id;
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
}
