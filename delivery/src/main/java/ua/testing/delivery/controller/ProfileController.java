package ua.testing.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.delivery.controller.util.Util;
import ua.testing.delivery.exception.NoSuchUserException;
import ua.testing.delivery.service.DeliveryService;
import ua.testing.delivery.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = {"/user"})
public class ProfileController {
    private final UserService userService;
    private final DeliveryService deliveryService;

    @Autowired
    public ProfileController(UserService userService, DeliveryService deliveryService) {
        this.userService = userService;
        this.deliveryService = deliveryService;
    }

    @RequestMapping(value = {"/userprofile"}, method = RequestMethod.GET)
    public ModelAndView userProfile(HttpSession httpSession, @AuthenticationPrincipal UserDetails userDetails) {
        ModelAndView view = new ModelAndView("user/userprofile");
        Util.addUserToSession(httpSession, userService.findByEmail(userDetails.getUsername()));
        return view;
    }

    @RequestMapping(value = {"/userprofile"}, method = RequestMethod.POST)
    public ModelAndView userProfileReplenish(HttpSession httpSession, int money) throws NoSuchUserException {
        ModelAndView modelAndView = new ModelAndView("user/userprofile");
        if (money <= 0) {
            modelAndView.addObject("incorrectMoney", true);
            return modelAndView;
        }
        Util.addUserToSession(httpSession, userService.replenishAccountBalance(Util.getUserFromSession(httpSession).getId(), money));
        return modelAndView;
    }
}