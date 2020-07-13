package ua.testing.delivery.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.testing.delivery.controller.util.Util;
import ua.testing.delivery.exception.NoSuchUserException;
import ua.testing.delivery.exception.ToMuchMoneyException;
import ua.testing.delivery.service.UserService;

import javax.servlet.http.HttpSession;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Controller
@RequestMapping(value = {"/user"})
public class ProfileController {
    private static final Logger log = LogManager.getLogger(ProfileController.class);

    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        log.debug("created");

        this.userService = userService;
    }

    @GetMapping(value = {"/user-profile"})
    public ModelAndView userProfile(HttpSession httpSession, @AuthenticationPrincipal UserDetails userDetails) {
        log.debug(userDetails);

        ModelAndView view = new ModelAndView("user/user-profile");
        Util.addUserToSession(httpSession, userService.findByEmail(userDetails.getUsername()));
        return view;
    }

    @PostMapping(value = {"/user-profile"})
    public ModelAndView userProfileReplenish(HttpSession httpSession, long money) throws NoSuchUserException {
        log.debug("money");

        ModelAndView modelAndView = new ModelAndView("user/user-profile");
        if (money <= 0) {
            modelAndView.addObject("incorrectMoney", true);
            return modelAndView;
        }
        try {
            Util.addUserToSession(httpSession, userService.replenishAccountBalance(Util.getUserFromSession(httpSession).getId(), money));
        } catch (ToMuchMoneyException e) {
            modelAndView.addObject("incorrectMoney", true);
        }
        return modelAndView;
    }

    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView noSuchUserException(RedirectAttributes redirectAttributes) {
        log.debug("NoSuchUserException");
        ModelAndView modelAndView = new ModelAndView("user/user-profile");
        redirectAttributes.addFlashAttribute("incorrectMoney", true);
        return modelAndView;
    }
}