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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localitySand")
    private List<Way> waysWhereThisLocalityIsSend;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localityGet")
    private List<Way> waysWhereThisLocalityIsGet;

    public Locality(String nameRu, String nameEn) {
        this.nameRu = nameRu;
        this.nameEn = nameEn;
    }
}
