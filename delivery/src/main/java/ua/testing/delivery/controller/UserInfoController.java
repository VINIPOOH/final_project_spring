package ua.testing.delivery.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.delivery.controller.util.Util;
import ua.testing.delivery.service.BillService;

import javax.servlet.http.HttpSession;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Controller
@RequestMapping(value = {"/user"})
public class UserInfoController {
    private static final Logger log = LogManager.getLogger(UserInfoController.class);

    private final BillService billService;

    @Autowired
    public UserInfoController(BillService billService) {
        log.debug("created");

        this.billService = billService;
    }


    @GetMapping(value = "/user-statistic")
    public ModelAndView userStatistic(HttpSession httpSession,
                                      @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 4) Pageable pageable) {
        log.debug("");

        ModelAndView modelAndView = new ModelAndView("user/user-statistic");
        modelAndView.addObject("BillDtoPage", billService.getBillHistoryByUserId(Util.getUserFromSession(httpSession).getId(), pageable));
        return modelAndView;
    }

}
