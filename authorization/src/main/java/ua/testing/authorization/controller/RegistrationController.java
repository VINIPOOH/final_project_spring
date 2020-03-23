package ua.testing.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.authorization.dto.RegistrationInfoDto;
import ua.testing.authorization.exception.OccupiedLoginException;
import ua.testing.authorization.service.AuthenticationService;
import ua.testing.authorization.service.UserService;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final UserService userService;


    @Autowired
    public RegistrationController(PasswordEncoder passwordEncoder, AuthenticationService authenticationService, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }


    @RequestMapping(value = {"/registration"}, method = RequestMethod.GET)
    public ModelAndView registrationTry(Model model) {
        model.addAttribute("registrationInfoDto", new RegistrationInfoDto());
        return new ModelAndView("registration");
    }

    @RequestMapping(value = {"/registration"}, method = RequestMethod.POST)
    public ModelAndView registrationTry(@Valid @ModelAttribute RegistrationInfoDto registrationInfoDto, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
            return modelAndView;
        }
        if (registrationInfoDto.getPassword().equals(registrationInfoDto.getPasswordRepeat())) {
            registrationInfoDto.setPassword
                    (passwordEncoder.encode(registrationInfoDto.getPassword()));
            try {
                userService.addNewUserToDB(
                        authenticationService.convertRegistrationDotToSimpleUserReadyForAddToDB
                                (registrationInfoDto)
                );
            } catch (OccupiedLoginException e) {
                modelAndView.addObject(registrationInfoDto);
                modelAndView.setViewName("registration");
                return modelAndView;
            }
            modelAndView.setViewName("login");
        } else {
            modelAndView.setViewName("registration");
        }
        return modelAndView;
    }


}