package ua.testing.delivery.service;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.testing.delivery.repository.UserRepository;

@Service
public class AuthenticationService implements UserDetailsService {
    private static Logger log = LogManager.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationService(UserRepository userRepository) {
        log.debug("created");

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("email"+ email);


        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with login: " + email));

    }
}
