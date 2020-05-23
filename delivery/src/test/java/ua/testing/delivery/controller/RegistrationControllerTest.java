package ua.testing.delivery.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import ua.testing.delivery.dto.RegistrationInfoDto;
import ua.testing.delivery.entity.User;
import ua.testing.delivery.exception.OccupiedLoginException;
import ua.testing.delivery.service.UserService;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static ua.testing.delivery.ServisesTestConstant.getAddreser;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = RegistrationController.class)
public class RegistrationControllerTest {

    @Autowired
    RegistrationController registrationController;
    @MockBean
    UserService userService;
    @MockBean
    BindingResult bindingResult;

    User user = getAddreser();

    @Test
    public void registrationTry() {
        ModelAndView result = registrationController.registrationTry();

        assertNotNull(result.getModel().get("registrationInfoDto"));
        assertEquals("registration", result.getViewName());
    }

    @Test
    public void testRegistrationTryPostAllCorrect() throws OccupiedLoginException {
        RegistrationInfoDto registrationInfoDto = getRegistrationInfoDto();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.addNewUserToDB(registrationInfoDto)).thenReturn(user);

        ModelAndView result = registrationController.registrationTry(registrationInfoDto, bindingResult);

        verify(bindingResult, times(1)).hasErrors();
        verify(userService, times(1)).addNewUserToDB(registrationInfoDto);
        assertNull(result.getModel().get("inputPasswordsIsNotEquals"));
        assertEquals("redirect:/login", result.getViewName());
    }

    @Test
    public void testRegistrationTryPostIncorrectInput() throws OccupiedLoginException {
        RegistrationInfoDto registrationInfoDto = getRegistrationInfoDto();
        when(bindingResult.hasErrors()).thenReturn(true);
        ModelAndView result = registrationController.registrationTry(registrationInfoDto, bindingResult);

        verify(bindingResult, times(1)).hasErrors();
        assertNull(result.getModel().get("inputPasswordsIsNotEquals"));
        assertEquals("registration", result.getViewName());
    }

    @Test
    public void testRegistrationTryPostNotEqualsEqualsPasswords() throws OccupiedLoginException {
        RegistrationInfoDto registrationInfoDto = getRegistrationInfoDto();
        registrationInfoDto.setPassword("notEquals");
        when(bindingResult.hasErrors()).thenReturn(false);
        ModelAndView result = registrationController.registrationTry(registrationInfoDto, bindingResult);

        verify(bindingResult, times(1)).hasErrors();
        assertTrue((Boolean) result.getModel().get("inputPasswordsIsNotEquals"));
        assertEquals("registration", result.getViewName());
    }

    @Test(expected = OccupiedLoginException.class)
    public void testRegistrationTryPostOccupiedLogin() throws OccupiedLoginException {
        RegistrationInfoDto registrationInfoDto = getRegistrationInfoDto();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.addNewUserToDB(registrationInfoDto)).thenThrow(OccupiedLoginException.class);

        registrationController.registrationTry(registrationInfoDto, bindingResult);

        fail();
    }

    private RegistrationInfoDto getRegistrationInfoDto() {
        return RegistrationInfoDto.builder()
                .username("email")
                .password("password")
                .passwordRepeat("password")
                .build();
    }

    @Test
    public void occupiedLoginExceptionHandling() {
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        ModelAndView result = registrationController.occupiedLoginExceptionHandling(redirectAttributes);

        assertTrue((Boolean) redirectAttributes.getFlashAttributes().get("OccupiedLoginException"));
        assertEquals("redirect:/registration", result.getViewName());
    }
}