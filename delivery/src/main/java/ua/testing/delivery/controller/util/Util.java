package ua.testing.delivery.controller.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ua.testing.delivery.controller.SessionConstants;
import ua.testing.delivery.entity.User;

import javax.servlet.http.HttpSession;

public class Util {
    private static final Logger log = LogManager.getLogger(Util.class);

    private Util() {
    }

    public static User getUserFromSession(HttpSession httpSession) {
        log.debug(httpSession.getAttribute(SessionConstants.SESSION_USER));

        return (User) httpSession.getAttribute(SessionConstants.SESSION_USER);
    }

    public static void addUserToSession(HttpSession httpSession, User user) {
        log.debug(user);

        httpSession.setAttribute(SessionConstants.SESSION_USER, user);
    }
}
