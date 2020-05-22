package ua.testing.delivery.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.delivery.controller.util.Util;
import ua.testing.delivery.exception.NoSuchUserException;
import ua.testing.delivery.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = {"/user"})
public class ProfileController {
    private static Logger log = LogManager.getLogger(ProfileController.class);

    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        log.debug("created");

        this.userService = userService;
    }

    @GetMapping(value = {"/userprofile"})
    public ModelAndView userProfile(HttpSession httpSession, @AuthenticationPrincipal UserDetails userDetails) {
        log.debug(userDetails);

        ModelAndView view = new ModelAndView("user/userprofile");
        Util.addUserToSession(httpSession, userService.findByEmail(userDetails.getUsername()));
        return view;
    }

    @PostMapping(value = {"/userprofile"})
    public ModelAndView userProfileReplenish(HttpSession httpSession, int money) throws NoSuchUserException {
        log.debug("money");

        ModelAndView modelAndView = new ModelAndView("user/userprofile");
        if (money <= 0) {
            modelAndView.addObject("incorrectMoney", true);
            return modelAndView;
        }
        Util.addUserToSession(httpSession, userService.replenishAccountBalance(Util.getUserFromSession(httpSession).getId(), money));
        return modelAndView;
    }
}