package ua.testing.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.authorization.controller.util.Util;
import ua.testing.authorization.entity.User;
import ua.testing.authorization.exception.DeliveryAlreadyPaidException;
import ua.testing.authorization.exception.NotEnoughMoneyException;
import ua.testing.authorization.service.BillService;
import ua.testing.authorization.service.LocalityService;

import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
@RequestMapping(value = {"/user/"})
public class UserDeliveryPayController {

    private final BillService billService;
    private final LocalityService localityService;

    @Autowired
    public UserDeliveryPayController(BillService billService, LocalityService localityService) {
        this.billService = billService;
        this.localityService = localityService;
    }


    @RequestMapping(value = {"user-delivery-pay"}, method = RequestMethod.GET)
    public ModelAndView userConfirmDelivers(HttpSession httpSession, Locale locale) {
        ModelAndView modelAndView = new ModelAndView("user/user-delivery-pay");
        User user = Util.getUserFromSession(httpSession);
        modelAndView.addObject("BillInfoToPayDtoList",
                billService.getBillsToPayByUserID(user.getId(), locale));
        modelAndView.addObject(user);
        return modelAndView;
    }


    @RequestMapping(value = {"user-delivery-pay"}, method = RequestMethod.POST)
    public String userNotGottenDeliversConfirmGettingDelivery(HttpSession httpSession, int deliveryId)
            throws DeliveryAlreadyPaidException, NotEnoughMoneyException {
        billService.payForDelivery(Util.getUserFromSession(httpSession).getId(), deliveryId);
        return "redirect:/user/user-delivery-pay";
    }
}
