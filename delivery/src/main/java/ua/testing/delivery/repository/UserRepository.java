package ua.testing.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.testing.delivery.entity.User;

import java.util.Optional;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndUserMoneyInCentsGreaterThanEqual(long id, long userMoneyInCents);
}
