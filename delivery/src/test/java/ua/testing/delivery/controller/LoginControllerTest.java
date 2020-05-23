package ua.testing.delivery.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = LoginController.class)
public class LoginControllerTest {

    @Autowired
    LoginController controller;

    @Test
    public void loginError() {
        ModelAndView actual = controller.loginError();

        assertEquals("redirect:/404", actual.getViewName());
    }

    @Test
    public void loginWithErrorAndLogOutParam() {
        ModelAndView result = controller.login("error", "logout");

        Assert.assertEquals(result.getModel().get("error"), true);
        Assert.assertEquals(result.getModel().get("logout"), true);
        Assert.assertEquals("login", result.getViewName());
    }

    @Test
    public void loginWithErrorParam() {
        ModelAndView result = controller.login("error",null);

        Assert.assertEquals(result.getModel().get("error"), true);
        Assert.assertEquals(result.getModel().get("logout"), false);
        Assert.assertEquals("login", result.getViewName());
    }

    @Test
    public void loginWithLogoutParam() {
        ModelAndView result = controller.login(null,"logout");

        Assert.assertEquals(result.getModel().get("error"), false);
        Assert.assertEquals(result.getModel().get("logout"), true);
        Assert.assertEquals("login", result.getViewName());
    }

    @Test
    public void loginWithNoParams() {
        ModelAndView result = controller.login(null, null);

        Assert.assertEquals(result.getModel().get("error"), false);
        Assert.assertEquals(result.getModel().get("error"), false);
        Assert.assertEquals("login", result.getViewName());
    }


}