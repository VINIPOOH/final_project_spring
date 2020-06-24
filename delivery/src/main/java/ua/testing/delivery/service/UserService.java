package ua.testing.delivery.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.testing.delivery.dto.RegistrationInfoDto;
import ua.testing.delivery.entity.RoleType;
import ua.testing.delivery.entity.User;
import ua.testing.delivery.exception.NoSuchUserException;
import ua.testing.delivery.exception.OccupiedLoginException;
import ua.testing.delivery.exception.ToMuchMoneyException;
import ua.testing.delivery.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {
    private static final Logger log = LogManager.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        log.debug("created");

        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        log.debug("");

        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        log.debug("email" + email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with login: " + email));
    }

    public User addNewUserToDB(RegistrationInfoDto registrationInfoDto) throws OccupiedLoginException {
        log.debug("registrationInfoDto" + registrationInfoDto);

        User user = getMapper().mapToEntity(registrationInfoDto);
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new OccupiedLoginException();
        }
    }

    @Transactional
    public User replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException, ToMuchMoneyException {
        log.debug("userId" + userId + "amountMoney" + amountMoney);

        User user = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);
        if (user.getUserMoneyInCents() + amountMoney <= 0) {
            throw new ToMuchMoneyException();
        }
        user.setUserMoneyInCents(user.getUserMoneyInCents() + amountMoney);
        return userRepository.save(user);
    }

    private EntityMapper<User, RegistrationInfoDto> getMapper() {
        return registration -> User.builder()
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .email(registration.getUsername())
                .enabled(true)
                .userMoneyInCents(0L)
                .password(passwordEncoder.encode(registration.getPassword()))
                .roleType(RoleType.ROLE_USER)
                .build();
    }
}
