package ua.testing.authorization.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    private LocalDate arrivalDate;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "way_id")
    private Way way;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "addressee_id")
    private User addressee;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "addresser_id")
    private User addresser;
    @Column(nullable = false, columnDefinition = "BIT(1) default 0")
    private Boolean isPackageReceived;
    @Column(nullable = false, columnDefinition = "BIT(1) default 0")
    private Boolean isDeliveryPaid;
    @Column(nullable = false)
    private int weight;
    @Column(nullable = false)
    private long costInCents;
}
