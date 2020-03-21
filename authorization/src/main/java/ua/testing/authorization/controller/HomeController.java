package ua.testing.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.authorization.dto.DeliveryCostAndTimeDto;
import ua.testing.authorization.dto.DeliveryInfoRequestDto;
import ua.testing.authorization.entity.Locality;
import ua.testing.authorization.exception.NoSuchWayException;
import ua.testing.authorization.exception.UnsupportableWeightFactorException;
import ua.testing.authorization.service.DeliveryProcessService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class HomeController {
    private final DeliveryProcessService deliveryProcessService;

    @Autowired
    public HomeController(DeliveryProcessService deliveryProcessService) {
        this.deliveryProcessService = deliveryProcessService;
    }

    @RequestMapping(value = {"/home"}, method = RequestMethod.GET)
    public ModelAndView home() {


        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject(new DeliveryInfoRequestDto());
        List<Locality> localities = deliveryProcessService.getLocalitis();
        modelAndView.addObject(localities);
        return modelAndView;
    }

    @RequestMapping(value = {"/home"}, method = RequestMethod.POST)
    public ModelAndView homeCount
            (@Valid @ModelAttribute DeliveryInfoRequestDto deliveryInfoRequestDto, BindingResult bindingResult,
             HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject(httpSession.getAttribute(SessionConstants.SESSION_USER.name()));
        modelAndView.addObject(deliveryInfoRequestDto);
        if (bindingResult.hasErrors()) {
            return modelAndView;
        }
        try {
            DeliveryCostAndTimeDto deliveryCostAndTimeDto = deliveryProcessService
                    .getDeliveryCostAndTimeDto(deliveryInfoRequestDto);
            modelAndView.addObject(deliveryCostAndTimeDto);


        } catch (NoSuchWayException e) {
            modelAndView.addObject("noSuchWayException", true);
        } catch (UnsupportableWeightFactorException e) {
            modelAndView.addObject("unsupportableWeightFactorException", true);
        }
        return modelAndView;
    }
}
