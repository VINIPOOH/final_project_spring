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
import ua.testing.delivery.dto.DeliveryInfoRequestDto;
import ua.testing.delivery.dto.LocaliseLocalityDto;
import ua.testing.delivery.dto.PriceAndTimeOnDeliveryDto;
import ua.testing.delivery.entity.Locality;
import ua.testing.delivery.exception.NoSuchWayException;
import ua.testing.delivery.exception.UnsupportableWeightFactorException;
import ua.testing.delivery.service.DeliveryService;
import ua.testing.delivery.service.LocalityService;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ua.testing.delivery.ServisesTestConstant.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = HomeController.class)
public class HomeControllerTest {
    @Autowired
    HomeController homeController;

    @MockBean
    DeliveryService deliveryService;
    @MockBean
    LocalityService localityService;

    @MockBean
    BindingResult bindingResult;


    @Test
    public void homeEn() {
        String expected = "home";
        Locality locality = getLocalitySend();
        LocaliseLocalityDto localiseLocalityDto = getLocaliseLocalityDtoEn(locality);
        List<LocaliseLocalityDto> localities = Collections.singletonList(localiseLocalityDto);
        when(localityService.getLocalities(any(Locale.class))).thenReturn(localities);


        ModelAndView result = homeController.home(getLocaleEn());

        verify(localityService, times(1)).getLocalities(any(Locale.class));
        assertEquals(localities, result.getModel().get("localityDtoList"));
        assertNotNull(result.getModel().get("deliveryInfoRequestDto"));
        assertEquals(expected, result.getViewName());
    }

    @Test
    public void homeRu() {
        String expected = "home";
        Locality locality = getLocalitySend();
        LocaliseLocalityDto localiseLocalityDto = getLocaliseLocalityDtoRu(locality);
        List<LocaliseLocalityDto> localities = Collections.singletonList(localiseLocalityDto);
        when(localityService.getLocalities(any(Locale.class))).thenReturn(localities);


        ModelAndView result = homeController.home(getLocaleRu());

        verify(localityService, times(1)).getLocalities(any(Locale.class));
        assertEquals(localities, result.getModel().get("localityDtoList"));
        assertNotNull(result.getModel().get("deliveryInfoRequestDto"));
        assertEquals(expected, result.getViewName());
    }



    @Test
    public void homeCountAllCorrect() throws UnsupportableWeightFactorException, NoSuchWayException {
        String expected = "redirect:/home";
        DeliveryInfoRequestDto deliveryInfoRequestDto = getDeliveryInfoRequestDto();
        PriceAndTimeOnDeliveryDto priceAndTimeOnDeliveryDto = getPriceAndTimeOnDeliveryDto();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(deliveryService.getDeliveryCostAndTimeDto(deliveryInfoRequestDto)).thenReturn(priceAndTimeOnDeliveryDto);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        ModelAndView result = homeController.homeCount(deliveryInfoRequestDto,bindingResult, redirectAttributes);

        verify(bindingResult, times(1)).hasErrors();
        verify(deliveryService, times(1)).getDeliveryCostAndTimeDto(deliveryInfoRequestDto);
        assertEquals(priceAndTimeOnDeliveryDto, redirectAttributes.getFlashAttributes().get("PriceAndTimeOnDeliveryDto"));
        assertNull(redirectAttributes.getFlashAttributes().get("incorrectWeightInput"));
        assertEquals(expected, result.getViewName());
    }

    @Test
    public void homeCountIncorrectInput() throws UnsupportableWeightFactorException, NoSuchWayException {
        String expected = "redirect:/home";
        DeliveryInfoRequestDto deliveryInfoRequestDto = getDeliveryInfoRequestDto();
        PriceAndTimeOnDeliveryDto priceAndTimeOnDeliveryDto = getPriceAndTimeOnDeliveryDto();
        when(bindingResult.hasErrors()).thenReturn(true);
        when(deliveryService.getDeliveryCostAndTimeDto(deliveryInfoRequestDto)).thenReturn(priceAndTimeOnDeliveryDto);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        ModelAndView result = homeController.homeCount(deliveryInfoRequestDto,bindingResult, redirectAttributes);

        verify(bindingResult, times(1)).hasErrors();
        verify(deliveryService, times(0)).getDeliveryCostAndTimeDto(deliveryInfoRequestDto);
        assertNull(redirectAttributes.getFlashAttributes().get("PriceAndTimeOnDeliveryDto"));
        assertTrue((Boolean) redirectAttributes.getFlashAttributes().get("incorrectWeightInput"));
        assertEquals(expected, result.getViewName());
    }

    @Test(expected = UnsupportableWeightFactorException.class)
    public void homeCountServiceThrowUnsupportableWeightFactorException() throws UnsupportableWeightFactorException, NoSuchWayException {
        DeliveryInfoRequestDto deliveryInfoRequestDto = getDeliveryInfoRequestDto();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(deliveryService.getDeliveryCostAndTimeDto(deliveryInfoRequestDto)).thenThrow(UnsupportableWeightFactorException.class);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        homeController.homeCount(deliveryInfoRequestDto,bindingResult, redirectAttributes);

        fail();
    }
    @Test(expected = NoSuchWayException.class)
    public void homeCountServiceThrowNoSuchWayException() throws UnsupportableWeightFactorException, NoSuchWayException {
        DeliveryInfoRequestDto deliveryInfoRequestDto = getDeliveryInfoRequestDto();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(deliveryService.getDeliveryCostAndTimeDto(deliveryInfoRequestDto)).thenThrow(NoSuchWayException.class);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        homeController.homeCount(deliveryInfoRequestDto,bindingResult, redirectAttributes);

        fail();
    }


    @Test
    public void noSuchWayExceptionHandling() {
        String expected = "redirect:/home";
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        ModelAndView result = homeController.noSuchWayExceptionHandling(redirectAttributes);

        assertTrue((Boolean) redirectAttributes.getFlashAttributes().get("noSuchWayException"));
        assertEquals(expected, result.getViewName());
    }



    @Test
    public void unsupportableWeightFactorException() {
        String expected = "redirect:/home";
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        ModelAndView result = homeController.unsupportableWeightFactorException(redirectAttributes);

        assertTrue((Boolean) redirectAttributes.getFlashAttributes().get("unsupportableWeightFactorException"));
        assertEquals(expected, result.getViewName());
    }

    private DeliveryInfoRequestDto getDeliveryInfoRequestDto() {
        return DeliveryInfoRequestDto.builder()
                .deliveryWeight(10)
                .localityGetID(1)
                .localitySandID(2)
                .build();
    }

    private PriceAndTimeOnDeliveryDto getPriceAndTimeOnDeliveryDto() {
        return PriceAndTimeOnDeliveryDto.builder()
                .costInCents(1)
                .timeOnWayInHours(1)
                .build();
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
}