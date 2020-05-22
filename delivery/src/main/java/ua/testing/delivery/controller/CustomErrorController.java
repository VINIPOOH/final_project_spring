package ua.testing.delivery.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class CustomErrorController implements ErrorController {
    private static Logger log = LogManager.getLogger(CustomErrorController.class);

    @GetMapping(value = {"/404"})
    public ModelAndView error404(HttpSession httpSession, @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("");

        return new ModelAndView("404");
    }
    @GetMapping(value = {"/405"})
    public ModelAndView error405(HttpSession httpSession, @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("");

        return new ModelAndView("404");
    }

    @GetMapping("/error")
    public String getErrorPage() {
        log.debug("");

        return "404";
    }


    @Override
    public String getErrorPath() {
        log.debug("");

        return "/error";
    }
}
