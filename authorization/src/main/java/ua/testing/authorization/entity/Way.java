package ua.testing.authorization.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
public class Way {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false , unique = true)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "localitySend_id")
    private Locality localitySand;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "localityGet_id")
    private Locality localityGet;
    @OneToMany(mappedBy = "way")
    private List<Delivery> deliveries;
    private int distanceInKilometres;
    private int timeOnWayInHours;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "way_tariffWeightFactor",
            joinColumns = @JoinColumn(name = "way_id"),
            inverseJoinColumns = @JoinColumn(name = "tariffWeightFactor_id"))
    private List<TariffWeightFactor> wayTariffs;

}
