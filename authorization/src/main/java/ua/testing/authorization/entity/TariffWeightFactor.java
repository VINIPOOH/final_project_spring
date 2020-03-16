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
public class TariffWeightFactor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    private int minWeightRange;
    private int maxWeightRange;
    private int overPayOnKilometer;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "wayTariffs")
    private List<Way> waysWhereUsed;

    public TariffWeightFactor(int minWeightRange, int maxWeightRange, int overPayOnKilometer) {
        this.minWeightRange = minWeightRange;
        this.maxWeightRange = maxWeightRange;
        this.overPayOnKilometer = overPayOnKilometer;
    }
}
