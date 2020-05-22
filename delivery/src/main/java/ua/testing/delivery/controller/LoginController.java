package ua.testing.delivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
    @RequestMapping(value = {"/login/error"}, method = RequestMethod.GET)
    public ModelAndView loginError() {
        return new ModelAndView("404");
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout) {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("error", error != null);
        modelAndView.addObject("logout", logout != null);
        return modelAndView;
    }
}
