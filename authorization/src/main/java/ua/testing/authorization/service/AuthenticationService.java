package ua.testing.authorization.service;


import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.testing.authorization.dto.RegistrationInfoDto;
import ua.testing.authorization.entity.RoleType;
import ua.testing.authorization.entity.User;
import ua.testing.authorization.repository.UserRepository;

@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with login: " + email));

    }

    public User convertRegistrationDotToSimpleUserReadyForAddToDB(RegistrationInfoDto registration) {
        return User.builder()
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .email(registration.getUsername())
                .enabled(true)
                .password(registration.getPassword())
                .roleType(RoleType.ROLE_USER)
                .build();
    }
}
