package ua.testing.authorization.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "way",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"localitySend_id", "localityGet_id"})})

public class Way {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "localitySend_id")
    private Locality localitySand;
    @ManyToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "localityGet_id")
    private Locality localityGet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "way")
    private List<Delivery> deliveries;
    private int distanceInKilometres;
    private int timeOnWayInHours;
    private int priceForKilometerInCents;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "way_tariffWeightFactor",
            joinColumns = @JoinColumn(name = "way_id"),
            inverseJoinColumns = @JoinColumn(name = "tariffWeightFactor_id"))
    private List<TariffWeightFactor> wayTariffs;

    @Override
    public String toString() {
        return "Way{"; //+
//                "id=" + id +
//                ", localitySand=" + localitySand.getNameRu() +
//                ", localityGet=" + localityGet.getNameRu() +
//                ", distanceInKilometres=" + distanceInKilometres +
//                ", timeOnWayInHours=" + timeOnWayInHours +
//                ", priceForKilometerInCents=" + priceForKilometerInCents +
//                ", wayTariffs=" + wayTariffs +
//                '}';
    }
}
