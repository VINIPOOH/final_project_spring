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
public class Locality {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false , unique = true)
    private Long id;
    private String nameRu;
    private String nameEn;
    @OneToMany(mappedBy = "localitySand")
    private List<Way> waysWhereThisLocalityIsSend;
    @OneToMany(mappedBy = "localityGet")
    private List<Way> waysWhereThisLocalityIsGet;
}
