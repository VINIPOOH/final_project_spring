package ua.testing.delivery.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ua.testing.delivery.dto.RegistrationInfoDto;
import ua.testing.delivery.entity.RoleType;
import ua.testing.delivery.entity.User;
import ua.testing.delivery.exception.NoSuchUserException;
import ua.testing.delivery.exception.OccupiedLoginException;
import ua.testing.delivery.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static ua.testing.delivery.ServisesTestConstant.*;

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
        doAnswer((i) -> i.getArgument(0)).when(passwordEncoder).encode(any(String.class));
    }

    @Test
    public void getAllUsers() {
        List<User> users = getUsers();
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userRepository.findAll();

        verify(userRepository, times(1)).findAll();
        assertEquals(users.get(0), result.get(0));
    }

    @Test
    public void findByEmailAllCorrect() {
        User user = getAddreser();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User result = userService.findByEmail(user.getEmail());

        assertEquals(user, result);
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByEmailIncorrectEmail() {
        User user = getAddreser();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        userService.findByEmail(user.getEmail());

        fail();
    }

    @Test
    public void addNewUserToDBAllCorrect() throws OccupiedLoginException {
        RegistrationInfoDto registrationInfoDto = getRegistrationInfoDto();
        doAnswer((i) -> i.getArgument(0)).when(userRepository).save(any(User.class));
        User expected = getUser(registrationInfoDto);
        doAnswer((invocation) -> invocation.getArgument(0)).when(passwordEncoder).encode(anyString());


        User result = userService.addNewUserToDB(registrationInfoDto);

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(expected, result);
    }


    @Test(expected = OccupiedLoginException.class)
    public void addNewUserToDBOccupiedLogin() throws OccupiedLoginException {
        RegistrationInfoDto registrationInfoDto = getRegistrationInfoDto();
        doAnswer((invocation) -> invocation.getArgument(0)).when(passwordEncoder).encode(anyString());
        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        userService.addNewUserToDB(registrationInfoDto);

        fail();
    }

    @Test
    public void replenishAccountBalanceAllCorrect() throws NoSuchUserException {
        User expected = getAddreser();
        User setIn = getAddreser();
        setIn.setUserMoneyInCents(0L);
        expected.setUserMoneyInCents(10L);
        long paymentSum = 10L;
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(setIn));
        doAnswer((i) -> i.getArgument(0)).when(userRepository).save(any(User.class));

        User result = userService.replenishAccountBalance(expected.getId(), paymentSum);

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(expected, result);
        assertEquals(10L, setIn.getUserMoneyInCents());
    }

    @Test(expected = NoSuchUserException.class)
    public void replenishAccountBalanceNoSuchUser() throws NoSuchUserException {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        userService.replenishAccountBalance(getUserId(), 10);

        fail();
    }

    private RegistrationInfoDto getRegistrationInfoDto() {
        return RegistrationInfoDto.builder()
                .password("password")
                .passwordRepeat("password")
                .username("email")
                .build();
    }

    private User getUser(RegistrationInfoDto registrationInfoDto) {
        return User.builder()
                .id(0)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .email(registrationInfoDto.getUsername())
                .enabled(true)
                .userMoneyInCents(0L)
                .password(passwordEncoder.encode(registrationInfoDto.getPassword()))
                .roleType(RoleType.ROLE_USER)
                .build();
    }

}