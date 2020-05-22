package ua.testing.delivery.entity;

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
@Table(name = "tariff_weight_factor")
public class TariffWeightFactor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;
    @Column(nullable = false)
    private int minWeightRange;
    @Column(nullable = false)
    private int maxWeightRange;
    @Column(nullable = false)
    private int overPayOnKilometer;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "wayTariffs")
    private List<Way> waysWhereUsed;

    public TariffWeightFactor(int minWeightRange, int maxWeightRange, int overPayOnKilometer) {
        this.minWeightRange = minWeightRange;
        this.maxWeightRange = maxWeightRange;
        this.overPayOnKilometer = overPayOnKilometer;
    }
}
