package ua.testing.delivery.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ua.testing.delivery.entity.Bill;
import ua.testing.delivery.entity.Delivery;
import ua.testing.delivery.entity.RoleType;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;
    private String email;
    private RoleType roleType;
    private String password;
    private long userMoneyInCents;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
}
