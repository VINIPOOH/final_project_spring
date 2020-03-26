package ua.testing.authorization.controller.util;

import ua.testing.authorization.controller.SessionConstants;
import ua.testing.authorization.entity.User;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class Util {
    public static User getUserFromSession(HttpSession httpSession) {
        return (User) httpSession.getAttribute(SessionConstants.SESSION_USER);
    }

    public static void addUserToSession(HttpSession httpSession, User user) {
        httpSession.setAttribute(SessionConstants.SESSION_USER, user);
    }
}
