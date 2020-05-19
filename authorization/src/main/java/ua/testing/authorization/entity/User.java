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
@EqualsAndHashCode()

@Entity
@Table(name = "user",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "role", nullable = false, columnDefinition = "VARCHAR(255) default 'ROLE_USER'")
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, columnDefinition = "BIGINT default 0")
    private Long userMoneyInCents;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addressee")
    private transient List<Delivery> deliveriesForUser;

    @Column(nullable = false, columnDefinition = "BIT(1) default 1")
    private boolean accountNonExpired;
    @Column(nullable = false, columnDefinition = "BIT(1) default 1")
    private boolean accountNonLocked;
    @Column(nullable = false, columnDefinition = "BIT(1) default 1")
    private boolean credentialsNonExpired;
    @Column(nullable = false, columnDefinition = "BIT(1) default 1")
    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private transient List<Bill> bills;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(roleType);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String toString() {
        return "User{}";
    }
}
