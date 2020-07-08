package ua.testing.delivery.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface AuthenticationService extends UserDetailsService {

    UserDetails loadUserByUsername(String email);
}
