package ua.testing.delivery.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ua.testing.delivery.entity.RoleType;
import ua.testing.delivery.entity.User;
import ua.testing.delivery.repository.UserRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AuthenticationService.class)
public class AuthenticationServiceTest {

    @Autowired
    AuthenticationService authenticationService;

    @MockBean
    UserRepository userRepository;

    @Test
    public void loadUserByUsernameExistEmail() {
        String emailTestingUserExist = "ExistEmail";
        User user = User.builder().email(emailTestingUserExist)
                .userMoneyInCents(1L)
                .roleType(RoleType.ROLE_USER)
                .password("pass")
                .enabled(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .accountNonExpired(true)
                .id(1L)
                .build();
        when(userRepository.findByEmail(emailTestingUserExist)).thenReturn(java.util.Optional.ofNullable(user));
        Assert.assertEquals(user, authenticationService.loadUserByUsername(emailTestingUserExist));
        verify(userRepository, times(1)).findByEmail(emailTestingUserExist);


    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameNotExistEmail() {
        String emailNotExistingUser = "NotExistEmail";
        when(userRepository.findByEmail(emailNotExistingUser)).thenReturn(Optional.empty());
        authenticationService.loadUserByUsername(emailNotExistingUser);
        Assert.fail();
    }

}