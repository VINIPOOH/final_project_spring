package ua.testing.delivery.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CustomErrorController.class)
public class CustomErrorControllerTest {
    @Autowired
    CustomErrorController customErrorController;

    @Test
    public void error404() {
        ModelAndView actual = customErrorController.error404();

        assertEquals("404", actual.getViewName());
    }

    @Test
    public void error405() {
        ModelAndView actual = customErrorController.error405();

        assertEquals("405", actual.getViewName());
    }

    @Test
    public void getErrorPage() {
        String actual = customErrorController.getErrorPage();

        assertEquals("404", actual);
    }

    @Test
    public void getErrorPath() {
        String actual = customErrorController.getErrorPath();

        assertEquals("/error", actual);
    }
}