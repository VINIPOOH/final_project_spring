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
import ua.testing.authorization.dto.DeliveryOrderCreateDto;
import ua.testing.authorization.entity.User;
import ua.testing.authorization.exception.NoSuchUserException;
import ua.testing.authorization.exception.NoSuchWayException;
import ua.testing.authorization.exception.UnsupportableWeightFactorException;
import ua.testing.authorization.service.BillService;
import ua.testing.authorization.service.LocalityService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping(value = {"/user/"})
public class UserDeliveryInitiationController {

    private final BillService billService;
    private final LocalityService localityService;

    @Autowired
    public UserDeliveryInitiationController(BillService billService, LocalityService localityService) {
        this.billService = billService;
        this.localityService = localityService;
    }

    @RequestMapping(value = {"user-delivery-initiation"}, method = RequestMethod.GET)
    public ModelAndView userDeliveryInitiation() {
        ModelAndView modelAndView = new ModelAndView("user/user-delivery-initiation");
        modelAndView.addObject(new DeliveryOrderCreateDto());
        modelAndView.addObject(localityService.getLocalities());
        return modelAndView;
    }

    @RequestMapping(value = {"user-delivery-initiation"}, method = RequestMethod.POST)
    public ModelAndView userDeliveryInitiationPost(@Valid @ModelAttribute DeliveryOrderCreateDto deliveryOrderCreateDto,
                                                   BindingResult bindingResult, HttpSession httpSession,
                                                   RedirectAttributes redirectAttributes) throws UnsupportableWeightFactorException, NoSuchWayException, NoSuchUserException {
        ModelAndView modelAndView = new ModelAndView("redirect:/user/user-delivery-initiation");
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("incorrectWeightInput", true);
            return modelAndView;
        }
        billService.initializeBill(deliveryOrderCreateDto, ((User) httpSession.getAttribute(SessionConstants.SESSION_USER)).getId());
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
