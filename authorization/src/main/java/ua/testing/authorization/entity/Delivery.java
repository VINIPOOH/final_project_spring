package ua.testing.authorization.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Delivery {
    @OneToOne(mappedBy = "delivery")
    Bill bill;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "way_id", nullable = false)
    private Way way;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "addressee_id", nullable = false)
    private User addressee;
    @Column(columnDefinition = "BIT(1) default 0")
    private boolean isPackageReceived;
    @Column(nullable = false)
    private int weight;
}
