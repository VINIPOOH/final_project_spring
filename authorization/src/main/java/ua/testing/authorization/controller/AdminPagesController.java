package ua.testing.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.authorization.DataAder;
import ua.testing.authorization.service.UserService;

@Controller
public class AdminPagesController {

    private final DataAder dataAder;
    private UserService userService;

    @Autowired
    public AdminPagesController(DataAder dataAder, UserService userService) {
        this.dataAder = dataAder;
        this.userService = userService;
    }

    @RequestMapping(value = {"/admin/users"}, method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView users() {
        ModelAndView view = new ModelAndView("admin/users");
        view.addObject(userService.getAllUsers());
        return view;
    }
    @RequestMapping(value = {"/admin/addData"}, method = RequestMethod.GET)
    public ModelAndView addData() {
        ModelAndView view = new ModelAndView("redirect:/login");
       // dataAder.run();
        return view;
    }

}
