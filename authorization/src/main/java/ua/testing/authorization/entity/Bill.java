package ua.testing.authorization.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id", referencedColumnName = "id", nullable = false)
    Delivery delivery;

    @Column(nullable = false, columnDefinition = "BIT(1) default 0")
    private Boolean isDeliveryPaid;

    @Column(nullable = false)
    private long costInCents;

    private LocalDate dateOfPay;


}
