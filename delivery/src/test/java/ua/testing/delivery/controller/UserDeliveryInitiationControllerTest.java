package ua.testing.delivery.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import ua.testing.delivery.controller.util.Util;
import ua.testing.delivery.dto.DeliveryInfoRequestDto;
import ua.testing.delivery.dto.DeliveryOrderCreateDto;
import ua.testing.delivery.dto.LocaliseLocalityDto;
import ua.testing.delivery.dto.PriceAndTimeOnDeliveryDto;
import ua.testing.delivery.entity.Locality;
import ua.testing.delivery.entity.User;
import ua.testing.delivery.exception.NoSuchUserException;
import ua.testing.delivery.exception.NoSuchWayException;
import ua.testing.delivery.exception.UnsupportableWeightFactorException;
import ua.testing.delivery.service.BillService;
import ua.testing.delivery.service.LocalityService;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ua.testing.delivery.ServisesTestConstant.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserDeliveryInitiationController.class)
public class UserDeliveryInitiationControllerTest {

    @Autowired
    UserDeliveryInitiationController userDeliveryInitiationController;

    @MockBean
    BillService billService;
    @MockBean
    LocalityService localityService;
    @MockBean
    BindingResult bindingResult;

    HttpSession httpSession;
    User user;

    @Before
    public void setUp() throws Exception {
        httpSession=new MockHttpSession();
        user=getAddreser();
        Util.addUserToSession(httpSession,user);
    }

    @Test
    public void userDeliveryInitiationEn() {
        Locality locality = getLocalitySend();
        LocaliseLocalityDto localiseLocalityDto = getLocaliseLocalityDtoEn(locality);
        List<LocaliseLocalityDto> localities = Collections.singletonList(localiseLocalityDto);
        when(localityService.getLocalities(any(Locale.class))).thenReturn(localities);


        ModelAndView result = userDeliveryInitiationController.userDeliveryInitiation(getLocaleEn());

        verify(localityService, times(1)).getLocalities(any(Locale.class));
        assertEquals(localities, result.getModel().get("localityDtoList"));
        assertNotNull(result.getModel().get("deliveryOrderCreateDto"));
        assertEquals("user/user-delivery-initiation", result.getViewName());
    }

    @Test
    public void userDeliveryInitiationRu() {
        Locality locality = getLocalitySend();
        LocaliseLocalityDto localiseLocalityDto = getLocaliseLocalityDtoRu(locality);
        List<LocaliseLocalityDto> localities = Collections.singletonList(localiseLocalityDto);
        when(localityService.getLocalities(any(Locale.class))).thenReturn(localities);

        ModelAndView result = userDeliveryInitiationController.userDeliveryInitiation(getLocaleRu());

        verify(localityService, times(1)).getLocalities(any(Locale.class));
        assertEquals(localities, result.getModel().get("localityDtoList"));
        assertNotNull(result.getModel().get("deliveryOrderCreateDto"));
        assertEquals("user/user-delivery-initiation", result.getViewName());
    }

    @Test
    public void userDeliveryInitiationPost() throws NoSuchUserException, UnsupportableWeightFactorException, NoSuchWayException {
        DeliveryOrderCreateDto deliveryOrderCreateDto = getDeliveryOrderCreateDto();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(billService.initializeBill(any(DeliveryOrderCreateDto.class), anyLong())).thenReturn(getBill());
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        ModelAndView result = userDeliveryInitiationController.userDeliveryInitiationPost(deliveryOrderCreateDto,bindingResult, httpSession, redirectAttributes);

        verify(bindingResult, times(1)).hasErrors();
        verify(billService, times(1)).initializeBill(any(DeliveryOrderCreateDto.class), anyLong());
        assertNull(redirectAttributes.getFlashAttributes().get("incorrectWeightInput"));
        assertEquals("redirect:/user/user-delivery-initiation", result.getViewName());
    }

    @Test
    public void userDeliveryInitiationPostIncorrectInput() throws NoSuchUserException, UnsupportableWeightFactorException, NoSuchWayException {
        DeliveryOrderCreateDto deliveryOrderCreateDto = getDeliveryOrderCreateDto();
        when(bindingResult.hasErrors()).thenReturn(true);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        ModelAndView result = userDeliveryInitiationController.userDeliveryInitiationPost(deliveryOrderCreateDto,bindingResult, httpSession, redirectAttributes);

        verify(bindingResult, times(1)).hasErrors();
        assertTrue((Boolean) redirectAttributes.getFlashAttributes().get("incorrectWeightInput"));
        assertEquals("redirect:/user/user-delivery-initiation", result.getViewName());
    }

    @Test(expected = NoSuchUserException.class)
    public void userDeliveryInitiationPostNoSuchUserException() throws NoSuchUserException, UnsupportableWeightFactorException, NoSuchWayException {
        DeliveryOrderCreateDto deliveryOrderCreateDto = getDeliveryOrderCreateDto();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(billService.initializeBill(any(DeliveryOrderCreateDto.class), anyLong())).thenThrow(NoSuchUserException.class);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        userDeliveryInitiationController.userDeliveryInitiationPost(deliveryOrderCreateDto,bindingResult, httpSession, redirectAttributes);

        fail();
    }

    @Test(expected = UnsupportableWeightFactorException.class)
    public void userDeliveryInitiationPostUnsupportableWeightFactorException() throws NoSuchUserException, UnsupportableWeightFactorException, NoSuchWayException {
        DeliveryOrderCreateDto deliveryOrderCreateDto = getDeliveryOrderCreateDto();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(billService.initializeBill(any(DeliveryOrderCreateDto.class), anyLong())).thenThrow(UnsupportableWeightFactorException.class);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        userDeliveryInitiationController.userDeliveryInitiationPost(deliveryOrderCreateDto,bindingResult, httpSession, redirectAttributes);

        fail();
    }

    @Test(expected = NoSuchWayException.class)
    public void userDeliveryInitiationPostNoSuchWayException() throws NoSuchUserException, UnsupportableWeightFactorException, NoSuchWayException {
        DeliveryOrderCreateDto deliveryOrderCreateDto = getDeliveryOrderCreateDto();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(billService.initializeBill(any(DeliveryOrderCreateDto.class), anyLong())).thenThrow(NoSuchWayException.class);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        userDeliveryInitiationController.userDeliveryInitiationPost(deliveryOrderCreateDto,bindingResult, httpSession, redirectAttributes);

        fail();
    }


    @Test
    public void noSuchWayExceptionHandling() {
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        ModelAndView result = userDeliveryInitiationController.noSuchWayExceptionHandling(redirectAttributes);

        assertTrue((Boolean) redirectAttributes.getFlashAttributes().get("noSuchWayException"));
        assertEquals("redirect:/user/user-delivery-initiation", result.getViewName());
    }

    @Test
    public void unsupportableWeightFactorException() {
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        ModelAndView result = userDeliveryInitiationController.unsupportableWeightFactorException(redirectAttributes);

        assertTrue((Boolean) redirectAttributes.getFlashAttributes().get("unsupportableWeightFactorException"));
        assertEquals("redirect:/user/user-delivery-initiation", result.getViewName());
    }

    @Test
    public void noSuchUserException() {
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        ModelAndView result = userDeliveryInitiationController.noSuchUserException(redirectAttributes);

        assertTrue((Boolean) redirectAttributes.getFlashAttributes().get("addresseeIsNotExist"));
        assertEquals("redirect:/user/user-delivery-initiation", result.getViewName());
    }

    private LocaliseLocalityDto getLocaliseLocalityDtoEn(Locality locality) {
        return LocaliseLocalityDto.builder()
                .id(locality.getId())
                .name(locality.getNameEn()).build();
    }

    private LocaliseLocalityDto getLocaliseLocalityDtoRu(Locality locality) {
        return LocaliseLocalityDto.builder()
                .id(locality.getId())
                .name(locality.getNameRu()).build();
    }

    private DeliveryOrderCreateDto getDeliveryOrderCreateDto() {
        return DeliveryOrderCreateDto.builder()
                .addresseeEmail("email")
                .deliveryWeight(10)
                .localityGetID(1)
                .localitySandID(2)
                .build();
    }

}