package ua.testing.delivery.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    GlobalExceptionHandler globalExceptionHandler;

    @Test
    public void handling() {
        String expected = "redirect:/404";

        String result = globalExceptionHandler.handling();

        assertEquals(expected, result);
    }

    @Test
    public void noSuchUserException() {
        String expected = "redirect:/405";
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        ModelAndView result = globalExceptionHandler.unExpectedException(redirectAttributes);

        assertEquals(expected, result.getViewName());
    }
}