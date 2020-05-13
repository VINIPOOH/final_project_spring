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
import ua.testing.authorization.dto.DeliveryInfoRequestDto;
import ua.testing.authorization.exception.NoSuchWayException;
import ua.testing.authorization.exception.UnsupportableWeightFactorException;
import ua.testing.authorization.service.DeliveryService;
import ua.testing.authorization.service.LocalityService;

import javax.validation.Valid;

@Controller
public class HomeController {
    private final DeliveryService deliveryService;
    private final LocalityService localityService;

    @Autowired
    public HomeController(DeliveryService deliveryService, LocalityService localityService) {
        this.deliveryService = deliveryService;
        this.localityService = localityService;
    }

    @RequestMapping(value = {"/home"}, method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject(new DeliveryInfoRequestDto());
        modelAndView.addObject(localityService.getLocalities());
        return modelAndView;
    }

    @RequestMapping(value = {"/home"}, method = RequestMethod.POST)
    public ModelAndView homeCount
            (@Valid @ModelAttribute DeliveryInfoRequestDto deliveryInfoRequestDto, BindingResult bindingResult,
             RedirectAttributes redirectAttributes) throws UnsupportableWeightFactorException, NoSuchWayException {
        ModelAndView modelAndView = new ModelAndView("redirect:/home");
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("incorrectWeightInput", true);
            return modelAndView;
        }
        redirectAttributes
                .addFlashAttribute(deliveryService.getDeliveryCostAndTimeDto(deliveryInfoRequestDto));
        return modelAndView;
    }

    @ExceptionHandler(NoSuchWayException.class)
    public ModelAndView noSuchWayExceptionHandling(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("noSuchWayException", true);
        return new ModelAndView("redirect:/home");
    }

    @ExceptionHandler(UnsupportableWeightFactorException.class)
    public ModelAndView unsupportableWeightFactorException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("unsupportableWeightFactorException", true);
        return new ModelAndView("redirect:/home");
    }
}
