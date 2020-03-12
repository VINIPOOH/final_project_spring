package ua.testing.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.testing.authorization.entity.User;
import ua.testing.authorization.exception.OccupiedLoginException;
import ua.testing.authorization.repository.UserRepository;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with login: " + email));
    }

    public void addNewUserToDB(User user) throws OccupiedLoginException {

        try {
            userRepository.save(user);
        }catch (DataIntegrityViolationException e){
            throw new OccupiedLoginException(user);
        }

    }
}
