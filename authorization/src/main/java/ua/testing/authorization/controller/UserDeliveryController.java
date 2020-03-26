package ua.testing.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.testing.authorization.controller.util.Util;
import ua.testing.authorization.dto.DeliveryOrderCreateDto;
import ua.testing.authorization.entity.User;
import ua.testing.authorization.exception.*;
import ua.testing.authorization.service.DeliveryProcessService;
import ua.testing.authorization.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping(value = {"/user/"})
public class UserDeliveryController {

    private final DeliveryProcessService deliveryProcessService;
    private final UserService userService;

    @Autowired
    public UserDeliveryController(DeliveryProcessService deliveryProcessService, UserService userService) {
        this.deliveryProcessService = deliveryProcessService;
        this.userService = userService;
    }

    @RequestMapping(value = {"delivers-to-get"}, method = RequestMethod.GET)
    public ModelAndView userNotGottenDelivers(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView("user/user-deliverys-to-get");
        User user = Util.getUserFromSession(httpSession);
        modelAndView.addObject("deliveriesWhichAddressedForUser",
                deliveryProcessService.getPricesAndNotTakenDeliversByUserId(user.getId()));
        modelAndView.addObject(user);
        return modelAndView;
    }


    @RequestMapping(value = {"delivers-to-get"}, method = RequestMethod.POST)
    public String userConfirmDeliveryPay(int deliveryId, HttpSession httpSession) throws AskedDataIsNotExist {
        User user = Util.getUserFromSession(httpSession);
        deliveryProcessService.confirmGettingDelivery(deliveryId);
        return "redirect:/user/delivers-to-get";
    }


    @RequestMapping(value = {"user-delivery-initiation"}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("user/user-delivery-initiation");
        modelAndView.addObject(new DeliveryOrderCreateDto());
        modelAndView.addObject(deliveryProcessService.getLocalities());
        return modelAndView;
    }

    @RequestMapping(value = {"user-delivery-initiation"}, method = RequestMethod.POST)
    public ModelAndView homeCount
            (@Valid @ModelAttribute DeliveryOrderCreateDto deliveryOrderCreateDto, BindingResult bindingResult,
             HttpSession httpSession, RedirectAttributes redirectAttributes) throws UnsupportableWeightFactorException, NoSuchWayException, NoSuchUserException, AskedDataIsNotExist {
        ModelAndView modelAndView = new ModelAndView("redirect:/user/user-delivery-initiation");
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("incorrectWeightInput", true);
            return modelAndView;
        }
        deliveryOrderCreateDto.setAddresserEmail(
                ((User) httpSession.getAttribute(SessionConstants.SESSION_USER)).getEmail());
        deliveryProcessService.createDeliveryOrder(deliveryOrderCreateDto);

        return modelAndView;
    }

    @RequestMapping(value = {"user-delivery-request-confirm"}, method = RequestMethod.GET)
    public ModelAndView userConfirmDelivers(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView("user/user-delivery-request-confirm");
        User user = Util.getUserFromSession(httpSession);
        modelAndView.addObject("deliveriesWhichIsNotPaid", deliveryProcessService.getNotPayedDeliversByUserId(user.getId()));
        modelAndView.addObject(user);
        return modelAndView;
    }


    @RequestMapping(value = {"user-delivery-request-confirm"}, method = RequestMethod.POST)
    public String userNotGottenDeliversConfirmGettingDelivery(HttpSession httpSession, int deliveryId)
            throws AskedDataIsNotExist, DeliveryAlreadyPaidException, NotEnoughMoneyException, NoSuchUserException {
        User user = Util.getUserFromSession(httpSession);
        Util.addUserToSession(httpSession, deliveryProcessService.payForDelivery(deliveryId, user.getId()).getAddresser());
        return "redirect:/user/user-delivery-request-confirm";
    }

    @ExceptionHandler(NoSuchWayException.class)
    public ModelAndView noSuchWayExceptionHandling(RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("noSuchWayException", true);
        return new ModelAndView("redirect:/user/user-delivery-initiation");
    }

    @ExceptionHandler(UnsupportableWeightFactorException.class)
    public ModelAndView unsupportableWeightFactorException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("unsupportableWeightFactorException", true);
        return new ModelAndView("redirect:/user/user-delivery-initiation");
    }

    @ExceptionHandler(NoSuchUserException.class)
    public ModelAndView noSuchUserException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("addresseeIsNotExist", true);
        return new ModelAndView("redirect:/user/user-delivery-initiation");
    }
}
