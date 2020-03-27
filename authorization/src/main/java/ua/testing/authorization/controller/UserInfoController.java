package ua.testing.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.authorization.controller.util.Util;
import ua.testing.authorization.exception.NoSuchUserException;
import ua.testing.authorization.service.DeliveryProcessService;
import ua.testing.authorization.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = {"/user"})
public class UserInfoController {
    private final UserService userService;
    private final DeliveryProcessService deliveryProcessService;

    @Autowired
    public UserInfoController(UserService userService, DeliveryProcessService deliveryProcessService) {
        this.userService = userService;
        this.deliveryProcessService = deliveryProcessService;
    }

    @RequestMapping(value = {"/userprofile"}, method = RequestMethod.GET)
    public ModelAndView userProfile(HttpSession httpSession, @AuthenticationPrincipal UserDetails userDetails) {
        ModelAndView view = new ModelAndView("user/userprofile");
        if (Util.getUserFromSession(httpSession) == null) {
            Util.addUserToSession(httpSession, userService.findByEmail(userDetails.getUsername()));
        }
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

    @RequestMapping(value = "user-statistic", method = RequestMethod.GET)
    public ModelAndView userStatistic(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView("user/user-statistic");
        modelAndView.addObject("userDelivers", deliveryProcessService.findDeliveryHistoryByUserId(Util.getUserFromSession(httpSession).getId()));
        return modelAndView;
    }

}
