package ua.testing.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.authorization.dto.RegistrationInfoDto;
import ua.testing.authorization.exception.OccupiedLoginException;
import ua.testing.authorization.service.AuthenticationService;
import ua.testing.authorization.service.UserService;

import javax.swing.text.StyledEditorKit;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        ModelAndView modelAndView = new ModelAndView("registration");
//переправить страница в доступе отказано сделать 404 нот фаунд
        modelAndView.addObject("isLoginOrPasswordWrong",true);
        return modelAndView;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout) {
        ModelAndView modelAndView= new ModelAndView("login");
        modelAndView.addObject("error", error != null);
        modelAndView.addObject("logout", logout != null);
        return modelAndView;
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
            }catch (OccupiedLoginException e){
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
