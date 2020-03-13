package ua.testing.authorization.entity;

import lombok.*;
import java.time.LocalDate;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false , unique = true)
    private Long id;

    @Column(name = "arrivalDate", nullable = false)
    private LocalDate arrivalDate;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "way_id")
    private Way way;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "addressee_id")
    private User addressee;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "addresser_id")
    private User addresser;

    private boolean isPackageReceived;
}
