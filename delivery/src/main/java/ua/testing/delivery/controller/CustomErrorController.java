package ua.testing.delivery.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class CustomErrorController implements ErrorController {
    private static Logger log = LogManager.getLogger(CustomErrorController.class);

    @RequestMapping(value = {"/404"}, method = RequestMethod.GET)
    public ModelAndView userProfile(HttpSession httpSession, @AuthenticationPrincipal UserDetails userDetails) {
        ModelAndView view = new ModelAndView("404");
        return view;
    }

    @GetMapping("/error")
    public String getErrorPage() {
        return "404";
    }


    @Override
    public String getErrorPath() {
        return "/error";
    }
}
