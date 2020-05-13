package ua.testing.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.authorization.controller.util.Util;
import ua.testing.authorization.entity.User;
import ua.testing.authorization.exception.AskedDataIsNotExist;
import ua.testing.authorization.service.DeliveryService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = {"/user/"})
public class UserDeliveryGetController {

    private final DeliveryService deliveryService;

    @Autowired
    public UserDeliveryGetController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @RequestMapping(value = {"delivers-to-get"}, method = RequestMethod.GET)
    public ModelAndView userNotGottenDelivers(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView("user/user-deliverys-to-get");
        User user = Util.getUserFromSession(httpSession);
        modelAndView.addObject("deliveriesWhichAddressedForUser",
                deliveryService.getDeliveryInfoToGet(user.getId()));
        modelAndView.addObject(user);
        return modelAndView;
    }


    @RequestMapping(value = {"delivers-to-get"}, method = RequestMethod.POST)
    public String userConfirmDeliveryPay(int deliveryId, HttpSession httpSession) throws AskedDataIsNotExist {
        deliveryService.confirmGettingDelivery(Util.getUserFromSession(httpSession).getId(), deliveryId);
        return "redirect:/user/delivers-to-get";
    }
}
