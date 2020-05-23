package ua.testing.delivery.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.delivery.entity.User;
import ua.testing.delivery.service.UserService;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static ua.testing.delivery.ServisesTestConstant.getAddreser;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AdminPagesController.class)
public class AdminPagesControllerTest {


    @Autowired
    AdminPagesController adminPagesController;

    @MockBean
    UserService userService;

    @Test
    public void users() {
        List<User> users = Collections.singletonList(getAddreser());
        when(userService.getAllUsers()).thenReturn(users);


        ModelAndView actual = adminPagesController.users();

        verify(userService, times(1)).getAllUsers();
        assertEquals(users, actual.getModel().get("userList"));
        assertEquals("admin/users", actual.getViewName());
    }
}