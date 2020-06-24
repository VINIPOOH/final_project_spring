package ua.testing.delivery.service.impl;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.testing.delivery.repository.UserRepository;
import ua.testing.delivery.service.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger log = LogManager.getLogger(AuthenticationServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository) {
        log.debug("created");

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.debug("email" + email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with login: " + email));
    }
}
