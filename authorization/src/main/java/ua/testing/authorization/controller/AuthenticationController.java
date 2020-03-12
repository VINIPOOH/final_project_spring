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
import ua.testing.authorization.service.AuthenticationService;
import ua.testing.authorization.service.UserService;

import javax.validation.Valid;

@Controller
public class AuthenticationController {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final UserService userService;


    @Autowired
    public AuthenticationController(PasswordEncoder passwordEncoder, AuthenticationService authenticationService, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }


    @RequestMapping(value = {"/login/error"}, method = RequestMethod.GET)
    public ModelAndView loginError() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = {"/registration"}, method = RequestMethod.GET)
    public ModelAndView registrationTry(Model model) {
        model.addAttribute("registrationInfoDto", new RegistrationInfoDto());
        return new ModelAndView("registration");
    }

    @RequestMapping(value = {"/registration"}, method = RequestMethod.POST)
    public ModelAndView registrationTry(@Valid @ModelAttribute RegistrationInfoDto registrationInfoDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //вернусть страницу регистрации
            return new ModelAndView("registration");
        }
        if (registrationInfoDto.getPassword().equals(registrationInfoDto.getPasswordRepeat())) {
            registrationInfoDto.setPassword
                    (passwordEncoder.encode(registrationInfoDto.getPassword()));
            userService.addNewUserToDB(
                    authenticationService.convertRegistrationDotToSimpleUserReadyForAddToDB
                            (registrationInfoDto)
            );
            return new ModelAndView("login");
        } else {
            return new ModelAndView("registration");
        }
    }
}
