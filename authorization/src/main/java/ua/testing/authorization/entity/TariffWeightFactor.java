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
    @Column(name = "id", nullable = false , unique = true)
    private Long id;
    private int minWeightRange;
    private int maxWeightRange;
    private int overPayOnKilometer;

    @ManyToMany(mappedBy = "wayTariffs")
    private List<Way> waysWhereUsed;
}
