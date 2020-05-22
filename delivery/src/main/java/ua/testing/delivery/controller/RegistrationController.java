package ua.testing.delivery.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.delivery.dto.RegistrationInfoDto;
import ua.testing.delivery.exception.OccupiedLoginException;
import ua.testing.delivery.service.UserService;

import javax.validation.Valid;

@Controller
public class RegistrationController {
    private static Logger log = LogManager.getLogger(RegistrationController.class);


    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        log.debug("created");

        this.userService = userService;
    }

    @RequestMapping(value = {"/registration"}, method = RequestMethod.GET)
    public ModelAndView registrationTry(Model model) {
        log.debug("model");

        model.addAttribute("registrationInfoDto", new RegistrationInfoDto());
        return new ModelAndView("registration");
    }

    @RequestMapping(value = {"/registration"}, method = RequestMethod.POST)
    public ModelAndView registrationTry(@Valid @ModelAttribute RegistrationInfoDto registrationInfoDto,
                                        BindingResult bindingResult) throws OccupiedLoginException {
        ModelAndView modelAndView = new ModelAndView("registration");
        if (bindingResult.hasErrors()) {
            return modelAndView;
        }
        if (registrationInfoDto.getPassword().equals(registrationInfoDto.getPasswordRepeat())) {
            userService.addNewUserToDB(registrationInfoDto);
            modelAndView.setViewName("redirect:/login");
        }
        return modelAndView;
    }

    @ExceptionHandler(OccupiedLoginException.class)
    public ModelAndView occupiedLoginExceptionHandling() {
        return new ModelAndView("redirect:/registration");
    }

}