package ua.testing.delivery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
/**
 * Represent Locality table in db
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Locality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;
    @Column(nullable = false)
    private String nameRu;
    @Column(nullable = false)
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
