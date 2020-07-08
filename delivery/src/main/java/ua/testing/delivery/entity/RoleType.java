package ua.testing.delivery.entity;

import org.springframework.security.core.GrantedAuthority;
/**
 * Contain authorisation user roles
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public enum RoleType implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_USER,
    ;

    @Override
    public String getAuthority() {
        return name();
    }
}
