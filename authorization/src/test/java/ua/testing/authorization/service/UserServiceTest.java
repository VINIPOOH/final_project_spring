package ua.testing.authorization.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ua.testing.authorization.entity.User;
import ua.testing.authorization.repository.UserRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static ua.testing.authorization.service.ServisesTestConstant.getUsers;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserService.class)
public class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    PasswordEncoder passwordEncoder;
    @MockBean
    UserRepository userRepository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getAllUsers() {
        List<User> users = getUsers();
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userRepository.findAll();

        verify(userRepository, times(1)).findAll();
        assertEquals(users.get(0),result.get(0));
    }

    @Test
    public void findByEmail() {

    }

    @Test
    public void addNewUserToDB() {
    }

    @Test
    public void replenishAccountBalance() {
    }
}