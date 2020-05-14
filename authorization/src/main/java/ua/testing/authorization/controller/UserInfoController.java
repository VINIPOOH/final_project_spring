package ua.testing.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.authorization.controller.util.Util;
import ua.testing.authorization.service.BillService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = {"/user"})
public class UserInfoController {

    private final BillService billService;

    @Autowired
    public UserInfoController(BillService billService) {
        this.billService = billService;
    }


    @RequestMapping(value = "/user-statistic", method = RequestMethod.GET)
    public ModelAndView userStatistic(HttpSession httpSession,
                                      @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 4) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("user/user-statistic");
        modelAndView.addObject("BillDtoPage", billService.getBillHistoryByUserId(Util.getUserFromSession(httpSession).getId(), pageable));
        return modelAndView;
    }

}
