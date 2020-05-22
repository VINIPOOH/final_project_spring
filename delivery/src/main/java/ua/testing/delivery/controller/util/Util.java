package ua.testing.delivery.controller.util;

import ua.testing.delivery.controller.SessionConstants;
import ua.testing.delivery.entity.User;

import javax.servlet.http.HttpSession;

public class Util {
    public static User getUserFromSession(HttpSession httpSession) {
        return (User) httpSession.getAttribute(SessionConstants.SESSION_USER);
    }

    public static void addUserToSession(HttpSession httpSession, User user) {
        httpSession.setAttribute(SessionConstants.SESSION_USER, user);
    }
}
