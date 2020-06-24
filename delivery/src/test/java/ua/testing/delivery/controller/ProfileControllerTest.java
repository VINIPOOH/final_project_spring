package ua.testing.delivery.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.delivery.controller.util.Util;
import ua.testing.delivery.entity.User;
import ua.testing.delivery.exception.NoSuchUserException;
import ua.testing.delivery.exception.ToMuchMoneyException;
import ua.testing.delivery.service.UserService;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ua.testing.delivery.ServisesTestConstant.getAddreser;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ProfileController.class)
public class ProfileControllerTest {
    @Autowired
    ProfileController profileController;

    @MockBean
    UserService userService;

    HttpSession httpSession;

    User user;

    @Before
    public void setUp() throws Exception {
        httpSession = new MockHttpSession();
        user = getAddreser();
        Util.addUserToSession(httpSession, user);
    }

    @Test
    public void userProfile() {
        when(userService.findByEmail(anyString())).thenReturn(user);

        ModelAndView actual = profileController.userProfile(httpSession, user);

        verify(userService, times(1)).findByEmail(anyString());
        assertEquals(user, Util.getUserFromSession(httpSession));
        assertEquals("user/userprofile", actual.getViewName());
    }

    @Test
    public void userProfileReplenishAllCorrect() throws NoSuchUserException, ToMuchMoneyException {
        when(userService.replenishAccountBalance(anyLong(), anyLong())).thenReturn(user);

        ModelAndView actual = profileController.userProfileReplenish(httpSession, 10);

        verify(userService, times(1)).replenishAccountBalance(anyLong(), anyLong());
        assertNull(actual.getModel().get("incorrectMoney"));
        assertEquals("user/userprofile", actual.getViewName());
    }

    @Test
    public void userProfileReplenishZeroMoney() throws NoSuchUserException {

        ModelAndView actual = profileController.userProfileReplenish(httpSession, 0);

        assertTrue((Boolean) actual.getModel().get("incorrectMoney"));
        assertEquals("user/userprofile", actual.getViewName());
    }

    @Test(expected = NoSuchUserException.class)
    public void userProfileIncorrectUser() throws NoSuchUserException, ToMuchMoneyException {
        when(userService.replenishAccountBalance(anyLong(), anyLong())).thenThrow(NoSuchUserException.class);

        profileController.userProfileReplenish(httpSession, 1);

        fail();
    }

    @Test()
    public void userProfileToMuchMoneyException() throws NoSuchUserException, ToMuchMoneyException {
        when(userService.replenishAccountBalance(anyLong(), anyLong())).thenThrow(ToMuchMoneyException.class);

        ModelAndView actual = profileController.userProfileReplenish(httpSession, 10);

        verify(userService, times(1)).replenishAccountBalance(anyLong(), anyLong());
        assertTrue((Boolean) actual.getModel().get("incorrectMoney"));
        assertEquals("user/userprofile", actual.getViewName());
    }
}