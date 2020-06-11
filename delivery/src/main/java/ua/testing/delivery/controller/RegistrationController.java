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
import ua.testing.delivery.dto.RegistrationInfoDto;
import ua.testing.delivery.exception.OccupiedLoginException;
import ua.testing.delivery.service.UserService;

import javax.validation.Valid;

@Controller
public class RegistrationController {
    private static final Logger log = LogManager.getLogger(RegistrationController.class);


    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        log.debug("created");

        this.userService = userService;
    }

    @GetMapping(value = {"/registration"})
    public ModelAndView registrationTry() {
        log.debug("model");
        ModelAndView modelAndView = new ModelAndView("registration");
        modelAndView.addObject("registrationInfoDto", new RegistrationInfoDto());
        return modelAndView;
    }

    @PostMapping(value = {"/registration"})
    public ModelAndView registrationTry(@Valid @ModelAttribute RegistrationInfoDto registrationInfoDto,
                                        BindingResult bindingResult) throws OccupiedLoginException {
        ModelAndView modelAndView = new ModelAndView("registration");
        if (bindingResult.hasErrors()) {
            return modelAndView;
        }
        if (registrationInfoDto.getPassword().equals(registrationInfoDto.getPasswordRepeat())) {
            userService.addNewUserToDB(registrationInfoDto);
            modelAndView.setViewName("redirect:/login");
            return modelAndView;
        }
        modelAndView.addObject("inputPasswordsIsNotEquals", true);
        return modelAndView;
    }

    @ExceptionHandler(OccupiedLoginException.class)
    public ModelAndView occupiedLoginExceptionHandling(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("OccupiedLoginException", true);
        return new ModelAndView("redirect:/registration");
    }

}
