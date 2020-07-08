package ua.testing.delivery.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.testing.delivery.dto.DeliveryInfoRequestDto;
import ua.testing.delivery.exception.NoSuchWayException;
import ua.testing.delivery.exception.UnsupportableWeightFactorException;
import ua.testing.delivery.service.DeliveryService;
import ua.testing.delivery.service.LocalityService;

import javax.validation.Valid;
import java.util.Locale;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Controller
public class HomeController {
    private static final String REDIRECT_HOME = "redirect:/home";
    private static final Logger log = LogManager.getLogger(HomeController.class);


    private final DeliveryService deliveryService;
    private final LocalityService localityService;

    @Autowired
    public HomeController(DeliveryService deliveryService, LocalityService localityService) {
        log.debug("created");

        this.deliveryService = deliveryService;
        this.localityService = localityService;
    }

    @GetMapping(value = {"/home", "/"})
    public ModelAndView home(Locale locale) {
        log.debug("");

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject(new DeliveryInfoRequestDto());
        modelAndView.addObject("localityDtoList", localityService.getLocalities(locale));
        return modelAndView;
    }

    @PostMapping(value = {"/home"})
    public ModelAndView homeCount
            (@Valid @ModelAttribute DeliveryInfoRequestDto deliveryInfoRequestDto, BindingResult bindingResult,
             RedirectAttributes redirectAttributes) throws UnsupportableWeightFactorException, NoSuchWayException {
        log.debug(deliveryInfoRequestDto);

        ModelAndView modelAndView = new ModelAndView(REDIRECT_HOME);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("incorrectWeightInput", true);
            return modelAndView;
        }
        redirectAttributes
                .addFlashAttribute("PriceAndTimeOnDeliveryDto", deliveryService.getDeliveryCostAndTimeDto(deliveryInfoRequestDto));
        return modelAndView;
    }

    @ExceptionHandler(NoSuchWayException.class)
    public ModelAndView noSuchWayExceptionHandling(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("noSuchWayException", true);
        return new ModelAndView(REDIRECT_HOME);
    }

    @ExceptionHandler(UnsupportableWeightFactorException.class)
    public ModelAndView unsupportableWeightFactorException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("unsupportableWeightFactorException", true);
        return new ModelAndView(REDIRECT_HOME);
    }
}
