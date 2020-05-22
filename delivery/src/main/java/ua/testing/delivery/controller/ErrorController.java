package ua.testing.delivery.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class ErrorController {
    private static Logger log = LogManager.getLogger(ErrorController.class);

    @RequestMapping(value = {"/404"}, method = RequestMethod.GET)
    public ModelAndView userProfile(HttpSession httpSession, @AuthenticationPrincipal UserDetails userDetails) {
        ModelAndView view = new ModelAndView("404");
        return view;
    }
}
