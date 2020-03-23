package ua.testing.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.authorization.entity.Delivery;
import ua.testing.authorization.entity.User;
import ua.testing.authorization.exception.AskedDataIsNotExist;
import ua.testing.authorization.service.DeliveryProcessService;
import ua.testing.authorization.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    private final DeliveryProcessService deliveryProcessService;
    private final UserService userService;

    @Autowired
    public UserController(DeliveryProcessService deliveryProcessService, UserService userService) {
        this.deliveryProcessService = deliveryProcessService;
        this.userService = userService;
    }

    @RequestMapping(value = {"/user/delivers-to-get"}, method = RequestMethod.GET)
    public ModelAndView userNotGottenDelivers(HttpSession httpSession, @AuthenticationPrincipal UserDetails userDetails) {
        ModelAndView modelAndView = new ModelAndView("user/user-deliverys-to-get");
        User user = (User) httpSession.getAttribute(SessionConstants.SESSION_USER);
        if (user == null) {
            user = userService.findByEmail(userDetails.getUsername());
            httpSession.setAttribute(SessionConstants.SESSION_USER, user);
        }
        List<Delivery> deliveriesWhichAddressedForUser = deliveryProcessService.getNotTakenDeliversByUserId(user.getId());
        modelAndView.addObject("deliveriesWhichAddressedForUser", deliveriesWhichAddressedForUser);
        modelAndView.addObject(user);
        return modelAndView;
    }


    @RequestMapping(value = {"/user/delivers-to-get"}, method = RequestMethod.POST)
    public String userNotGottenDeliversConfirmGettingDelivery(int deliveryId) throws AskedDataIsNotExist {
        deliveryProcessService.confirmGettingDelivery(deliveryId);
        return "redirect:/user/delivers-to-get";
    }

    @RequestMapping(value = {"/user/userprofile"}, method = RequestMethod.GET)
    public ModelAndView userProfile(HttpSession httpSession, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
//        httpSession.setAttribute(SessionConstants.SESSION_USER.name(), user);
        httpSession.setAttribute(SessionConstants.SESSION_USER, user);
        ModelAndView view = new ModelAndView("user/userprofile");
        view.addObject(user);
        return view;
    }

}
