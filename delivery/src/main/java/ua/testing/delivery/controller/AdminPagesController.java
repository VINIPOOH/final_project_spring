package ua.testing.delivery.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.delivery.service.UserService;

@Controller
public class AdminPagesController {
    private static final Logger log = LogManager.getLogger(AdminPagesController.class);


    private final UserService userService;

    @Autowired
    public AdminPagesController(UserService userService) {
        log.debug("created");

        this.userService = userService;
    }

    @GetMapping(value = {"/admin/users"})
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView users() {
        log.debug("");

        ModelAndView view = new ModelAndView("admin/users");
        view.addObject("userList", userService.getAllUsers());
        return view;
    }

}
