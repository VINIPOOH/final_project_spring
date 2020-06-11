package ua.testing.delivery.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.delivery.controller.util.Util;
import ua.testing.delivery.entity.User;
import ua.testing.delivery.exception.AskedDataIsNotExist;
import ua.testing.delivery.service.DeliveryService;

import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
@RequestMapping(value = {"/user/"})
public class UserDeliveryGetController {
    private static final Logger log = LogManager.getLogger(UserDeliveryGetController.class);


    private final DeliveryService deliveryService;

    @Autowired
    public UserDeliveryGetController(DeliveryService deliveryService) {
        log.debug("created");

        this.deliveryService = deliveryService;
    }

    @GetMapping(value = {"delivers-to-get"})
    public ModelAndView userNotGottenDelivers(HttpSession httpSession, Locale locale) {
        log.debug("");

        ModelAndView modelAndView = new ModelAndView("user/user-delivers-to-get");
        User user = Util.getUserFromSession(httpSession);
        modelAndView.addObject("DeliveryInfoToGetDtoList",
                deliveryService.getDeliveryInfoToGet(user.getId(), locale));
        modelAndView.addObject(user);
        return modelAndView;
    }


    @PostMapping(value = {"delivers-to-get"})
    public String userConfirmDeliveryPay(long deliveryId, HttpSession httpSession) throws AskedDataIsNotExist {
        log.debug("delivery id" + deliveryId);

        deliveryService.confirmGettingDelivery(Util.getUserFromSession(httpSession).getId(), deliveryId);
        return "redirect:/user/delivers-to-get";
    }
}
