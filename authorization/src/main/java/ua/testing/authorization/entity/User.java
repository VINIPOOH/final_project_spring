package ua.testing.authorization.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "user",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    private String password;
    private Long userMoneyInCents;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addressee")
    private List<Delivery> waysWhereThisLocalityIsSend;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addresser")
    private List<Delivery> waysWhereThisLocalityIsGet;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(roleType);
    }

    @Override
    public String getUsername() {
        return email;
    }


}
