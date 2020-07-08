package ua.testing.delivery.service;

import org.springframework.stereotype.Service;
import ua.testing.delivery.dto.RegistrationInfoDto;
import ua.testing.delivery.entity.User;
import ua.testing.delivery.exception.NoSuchUserException;
import ua.testing.delivery.exception.OccupiedLoginException;
import ua.testing.delivery.exception.ToMuchMoneyException;

import java.util.List;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Service
public interface UserService {

    List<User> getAllUsers();

    User findByEmail(String email);

    User addNewUserToDB(RegistrationInfoDto registrationInfoDto) throws OccupiedLoginException;

    User replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException, ToMuchMoneyException;

}
