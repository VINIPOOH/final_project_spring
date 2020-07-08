package ua.testing.delivery.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.testing.delivery.controller.util.Util;
import ua.testing.delivery.entity.User;
import ua.testing.delivery.exception.DeliveryAlreadyPaidException;
import ua.testing.delivery.exception.NotEnoughMoneyException;
import ua.testing.delivery.service.BillService;

import javax.servlet.http.HttpSession;
import java.util.Locale;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Controller
@RequestMapping(value = {"/user/"})
public class UserDeliveryPayController {
    private static final Logger log = LogManager.getLogger(UserDeliveryPayController.class);

    private final BillService billService;

    @Autowired
    public UserDeliveryPayController(BillService billService) {
        log.debug("created");

        this.billService = billService;
    }


    @GetMapping(value = {"user-delivery-pay"})
    public ModelAndView userConfirmDelivers(HttpSession httpSession, Locale locale) {
        log.debug("");

        ModelAndView modelAndView = new ModelAndView("user/user-delivery-pay");
        User user = Util.getUserFromSession(httpSession);
        modelAndView.addObject("BillInfoToPayDtoList",
                billService.getBillsToPayByUserID(user.getId(), locale));
        modelAndView.addObject(user);
        return modelAndView;
    }


    @PostMapping(value = {"user-delivery-pay"})
    public String userNotGottenDeliversConfirmGettingDelivery(HttpSession httpSession, long deliveryId)
            throws DeliveryAlreadyPaidException, NotEnoughMoneyException {
        log.debug("deliveryId" + deliveryId);

        billService.payForDelivery(Util.getUserFromSession(httpSession).getId(), deliveryId);
        return "redirect:/user/user-delivery-pay";
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ModelAndView notEnoughMoneyException(RedirectAttributes redirectAttributes) {
        log.debug("");

        redirectAttributes.addFlashAttribute("notEnoughMoneyException", true);
        return new ModelAndView("redirect:/user/user-delivery-pay");
    }
}
