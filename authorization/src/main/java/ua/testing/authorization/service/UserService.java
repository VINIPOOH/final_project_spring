package ua.testing.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.testing.authorization.dto.RegistrationInfoDto;
import ua.testing.authorization.entity.RoleType;
import ua.testing.authorization.entity.User;
import ua.testing.authorization.exception.NoSuchUserException;
import ua.testing.authorization.exception.OccupiedLoginException;
import ua.testing.authorization.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with login: " + email));
    }

    public void addNewUserToDB(RegistrationInfoDto registrationInfoDto) throws OccupiedLoginException {
        User user = getMapper().mapToEntity(registrationInfoDto);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new OccupiedLoginException(user);
        }

    }

    @Transactional
    public User replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException {
        User user = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);
        user.setUserMoneyInCents(user.getUserMoneyInCents() + amountMoney);
        userRepository.save(user);
        return userRepository.findById(userId).get();
    }

    private EntityMapper<User, RegistrationInfoDto> getMapper() {
        return (registration) -> User.builder()
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
