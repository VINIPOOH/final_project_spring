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
import ua.testing.delivery.dto.DeliveryInfoToGetDto;
import ua.testing.delivery.entity.Delivery;
import ua.testing.delivery.entity.User;
import ua.testing.delivery.exception.AskedDataIsNotExist;
import ua.testing.delivery.service.DeliveryService;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ua.testing.delivery.ServisesTestConstant.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserDeliveryGetController.class)
public class UserDeliveryGetControllerTest {

    @Autowired
    UserDeliveryGetController userDeliveryGetController;
    @MockBean
    DeliveryService deliveryService;


    User user = getAddreser();
    HttpSession httpSession;

    @Before
    public void setUp() throws Exception {
        user = getAddreser();
        httpSession = new MockHttpSession();
        Util.addUserToSession(httpSession, user);
    }

    @Test
    public void userNotGottenDelivers() {
        Delivery delivery = getDelivery();
        DeliveryInfoToGetDto deliveryInfoToGetDto = getDeliveryInfoToGetDto(delivery);
        List<DeliveryInfoToGetDto> deliveryInfoToGetDtos = Collections.singletonList(deliveryInfoToGetDto);
        when(deliveryService.getDeliveryInfoToGet(anyLong(), any(Locale.class))).thenReturn(deliveryInfoToGetDtos);

        ModelAndView result = userDeliveryGetController.userNotGottenDelivers(httpSession, getLocaleEn());

        verify(deliveryService, times(1)).getDeliveryInfoToGet(anyLong(), any(Locale.class));
        assertEquals(deliveryInfoToGetDtos, result.getModel().get("DeliveryInfoToGetDtoList"));
        assertEquals("user/user-deliverys-to-get", result.getViewName());
    }

    private DeliveryInfoToGetDto getDeliveryInfoToGetDto(Delivery delivery) {
        return DeliveryInfoToGetDto.builder()
                .addresserEmail(user.getEmail())
                .deliveryId(delivery.getId())
                .localityGetName(delivery.getWay().getLocalityGet().getNameEn())
                .localitySandName(delivery.getWay().getLocalitySand().getNameEn())
                .build();
    }

    @Test
    public void userConfirmDeliveryPay() throws AskedDataIsNotExist {
        when(deliveryService.confirmGettingDelivery(anyLong(), anyLong())).thenReturn(true);

        String result = userDeliveryGetController.userConfirmDeliveryPay(1L, httpSession);

        verify(deliveryService, times(1)).confirmGettingDelivery(anyLong(), anyLong());
        assertEquals("redirect:/user/delivers-to-get", result);
    }

    @Test(expected = AskedDataIsNotExist.class)
    public void userConfirmDeliveryPayIncorrectData() throws AskedDataIsNotExist {
        when(deliveryService.confirmGettingDelivery(anyLong(), anyLong())).thenThrow(AskedDataIsNotExist.class);

        userDeliveryGetController.userConfirmDeliveryPay(1L, httpSession);

        fail();
    }
}