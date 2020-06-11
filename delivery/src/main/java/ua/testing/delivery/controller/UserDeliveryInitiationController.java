package ua.testing.delivery.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.testing.delivery.controller.util.Util;
import ua.testing.delivery.dto.DeliveryOrderCreateDto;
import ua.testing.delivery.exception.NoSuchUserException;
import ua.testing.delivery.exception.NoSuchWayException;
import ua.testing.delivery.exception.UnsupportableWeightFactorException;
import ua.testing.delivery.service.BillService;
import ua.testing.delivery.service.LocalityService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping(value = {"/user/"})
public class UserDeliveryInitiationController {
    private static final String REDIRECT_USER_USER_DELIVERY_INITIATION = "redirect:/user/user-delivery-initiation";
    private static final Logger log = LogManager.getLogger(UserDeliveryInitiationController.class);

    private final BillService billService;
    private final LocalityService localityService;

    @Autowired
    public UserDeliveryInitiationController(BillService billService, LocalityService localityService) {
        log.debug("created");

        this.billService = billService;
        this.localityService = localityService;
    }

    @GetMapping(value = {"user-delivery-initiation"})
    public ModelAndView userDeliveryInitiation(Locale locale) {
        log.debug("");

        ModelAndView modelAndView = new ModelAndView("user/user-delivery-initiation");
        modelAndView.addObject(new DeliveryOrderCreateDto());
        modelAndView.addObject("localityDtoList", localityService.getLocalities(locale));
        return modelAndView;
    }

    @PostMapping(value = {"user-delivery-initiation"})
    public ModelAndView userDeliveryInitiationPost(@Valid @ModelAttribute DeliveryOrderCreateDto deliveryOrderCreateDto,
                                                   BindingResult bindingResult, HttpSession httpSession,
                                                   RedirectAttributes redirectAttributes) throws UnsupportableWeightFactorException, NoSuchWayException, NoSuchUserException {
        log.debug("deliveryOrderCreateDto" + deliveryOrderCreateDto);

        ModelAndView modelAndView = new ModelAndView(REDIRECT_USER_USER_DELIVERY_INITIATION);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("incorrectWeightInput", true);
            return modelAndView;
        }
        billService.initializeBill(deliveryOrderCreateDto, Util.getUserFromSession(httpSession).getId());
        return modelAndView;
    }


    @ExceptionHandler(NoSuchWayException.class)
    public ModelAndView noSuchWayExceptionHandling(RedirectAttributes redirectAttributes) {
        log.debug("NoSuchWayException");


        redirectAttributes.addFlashAttribute("noSuchWayException", true);
        return new ModelAndView(REDIRECT_USER_USER_DELIVERY_INITIATION);
    }

    @ExceptionHandler(UnsupportableWeightFactorException.class)
    public ModelAndView unsupportableWeightFactorException(RedirectAttributes redirectAttributes) {
        log.debug("UnsupportableWeightFactorException");

        redirectAttributes.addFlashAttribute("unsupportableWeightFactorException", true);
        return new ModelAndView(REDIRECT_USER_USER_DELIVERY_INITIATION);
    }

    @ExceptionHandler(NoSuchUserException.class)
    public ModelAndView noSuchUserException(RedirectAttributes redirectAttributes) {
        log.debug("NoSuchUserException");

        redirectAttributes.addFlashAttribute("addresseeIsNotExist", true);
        return new ModelAndView(REDIRECT_USER_USER_DELIVERY_INITIATION);
    }
}
