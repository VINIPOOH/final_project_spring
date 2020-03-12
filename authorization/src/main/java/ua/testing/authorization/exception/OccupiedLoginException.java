package ua.testing.authorization.exception;

import ua.testing.authorization.entity.User;

public class OccupiedLoginException extends Exception {
    private User user;

    public OccupiedLoginException(User user) {
        this.user = user;
    }
}
