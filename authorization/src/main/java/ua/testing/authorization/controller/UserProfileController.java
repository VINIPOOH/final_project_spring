package ua.testing.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.authorization.DataAder;
import ua.testing.authorization.entity.Delivery;
import ua.testing.authorization.entity.User;
import ua.testing.authorization.repository.DeliveryRepository;
import ua.testing.authorization.service.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserProfileController {


    private final UserService userService;
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public UserProfileController(UserService userService, DeliveryRepository deliveryRepository) {

        this.userService = userService;
        this.deliveryRepository = deliveryRepository;
    }


    @RequestMapping(value = {"/user/delivers-to-get"}, method = RequestMethod.GET)
    public ModelAndView userNotGottenDelivers(HttpSession httpSession, @AuthenticationPrincipal UserDetails userDetails) {


        ModelAndView modelAndView = new ModelAndView("user/user-deliverys-to-get");
        User user = (User) httpSession.getAttribute(SessionConstants.SESSION_USER.name());
        if (user == null) {
            user = userService.findByEmail(userDetails.getUsername());
            httpSession.setAttribute(SessionConstants.SESSION_USER.name(), user);
        }
        List<Delivery> deliveriesWhichAddressedForUser = deliveryRepository.findAllByIsPackageReceivedFalseAndAddressee_Id(user.getId());
        List<Delivery> deliveriesWhichAddressedFromUser = deliveryRepository.findAllByIsPackageReceivedFalseAndAddresser_Id(user.getId());
        modelAndView.addObject("deliveriesWhichAddressedForUser", deliveriesWhichAddressedForUser);
        modelAndView.addObject("deliveriesWhichAddressedFromUser", deliveriesWhichAddressedFromUser);
        modelAndView.addObject(user);
        return modelAndView;
    }

    @RequestMapping(value = {"/user/userprofile"}, method = RequestMethod.GET)
    public ModelAndView userProfile(HttpSession httpSession, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        httpSession.setAttribute(SessionConstants.SESSION_USER.name(), user);
        ModelAndView view = new ModelAndView("user/userprofile");
        view.addObject(user);
        return view;
    }

}
