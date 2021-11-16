package ua.testing.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.testing.delivery.entity.User;
import ua.testing.delivery.repository.UserRepository;

import java.util.Optional;

@RestController
public class UserRestController {

    private final UserRepository userRepository;

    @Autowired
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @DeleteMapping(value = "/users/{id}")
    void deleteUser(@PathVariable Long id){
        userRepository.deleteById(id);
    }

//    @PutMapping(value ="/users/{id}")
//    User addUser(@RequestBody User user){
//        return userRepository.save(user);
//    }

    @PostMapping(value = "/users/{id}")
    User editUser(@RequestBody User user, @PathVariable Long id){
        final Optional<User> byId = userRepository.findById(id);
        byId.get().setUserMoneyInCents(user.getUserMoneyInCents());
        return userRepository.save(byId.get());
    }

    @GetMapping(value = "/users/{id}")
    User getUser(@PathVariable Long id){
        return userRepository.findById(id).get();
    }

    @PutMapping(value = "/users")
    User putUser(@RequestBody User user){
        return userRepository.save(user);
    }
}
