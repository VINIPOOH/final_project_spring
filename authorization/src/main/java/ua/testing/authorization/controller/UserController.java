package ua.testing.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.testing.authorization.dto.DeliveryOrderCreateDto;
import ua.testing.authorization.entity.Delivery;
import ua.testing.authorization.entity.User;
import ua.testing.authorization.exception.AskedDataIsNotExist;
import ua.testing.authorization.exception.NoSuchUserException;
import ua.testing.authorization.exception.NoSuchWayException;
import ua.testing.authorization.exception.UnsupportableWeightFactorException;
import ua.testing.authorization.service.DeliveryProcessService;
import ua.testing.authorization.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
        httpSession.setAttribute(SessionConstants.SESSION_USER, user);
        ModelAndView view = new ModelAndView("user/userprofile");
        view.addObject(user);
        return view;
    }

    @RequestMapping(value = {"/user/userprofile"}, method = RequestMethod.POST)
    public ModelAndView userProfileReplenish(HttpSession httpSession, int money) {
        ModelAndView modelAndView = new ModelAndView("user/userprofile");
        User user = (User) httpSession.getAttribute(SessionConstants.SESSION_USER);
        modelAndView.addObject(user);
        if (money <= 0) {
            modelAndView.addObject("incorrectMoney", true);
            return modelAndView;
        }
        userService.replenishAccountBalance(user, money);
        return modelAndView;
    }

    @RequestMapping(value = {"/user/user-delivery-initiation"}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("user/user-delivery-initiation");
        modelAndView.addObject(new DeliveryOrderCreateDto());
        modelAndView.addObject(deliveryProcessService.getLocalities());
        return modelAndView;
    }

    @RequestMapping(value = {"/user/user-delivery-initiation"}, method = RequestMethod.POST)
    public ModelAndView homeCount
            (@Valid @ModelAttribute DeliveryOrderCreateDto deliveryOrderCreateDto, BindingResult bindingResult,
             HttpSession httpSession, RedirectAttributes redirectAttributes) throws UnsupportableWeightFactorException, NoSuchWayException, NoSuchUserException {
        ModelAndView modelAndView = new ModelAndView("redirect:/user/user-delivery-initiation");
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("incorrectWeightInput", true);
            return modelAndView;
        }
        deliveryOrderCreateDto.setAddresserEmail(
                ((User) httpSession.getAttribute(SessionConstants.SESSION_USER)).getEmail());

        deliveryProcessService.сreateDeliveryOrder(deliveryOrderCreateDto);

        return modelAndView;
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
