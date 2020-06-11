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
import ua.testing.delivery.dto.BillInfoToPayDto;
import ua.testing.delivery.entity.Bill;
import ua.testing.delivery.entity.User;
import ua.testing.delivery.exception.DeliveryAlreadyPaidException;
import ua.testing.delivery.exception.NotEnoughMoneyException;
import ua.testing.delivery.service.BillService;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static ua.testing.delivery.ServisesTestConstant.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserDeliveryPayController.class)
public class UserDeliveryPayControllerTest {

    @Autowired
    UserDeliveryPayController userDeliveryPayController;

    @MockBean
    BillService billService;

    User user = getAddreser();
    HttpSession httpSession;

    @Before
    public void setUp() throws Exception {
        user = getAddreser();
        httpSession = new MockHttpSession();
        Util.addUserToSession(httpSession, user);
    }

    @Test
    public void userConfirmDeliversEn() {
        Bill bill = getBill();
        BillInfoToPayDto billInfoToPayDto = getBillInfoToPayDtoEn(bill);
        billInfoToPayDto.setLocalityGetName(bill.getDelivery().getWay().getLocalityGet().getNameEn());
        billInfoToPayDto.setLocalitySandName(bill.getDelivery().getWay().getLocalitySand().getNameEn());
        List<BillInfoToPayDto> billInfoToPayDtos = Collections.singletonList(billInfoToPayDto);
        when(billService.getBillsToPayByUserID(anyLong(), any(Locale.class))).thenReturn(billInfoToPayDtos);

        ModelAndView result = userDeliveryPayController.userConfirmDelivers(httpSession, getLocaleEn());

        verify(billService, times(1)).getBillsToPayByUserID(anyLong(), any(Locale.class));
        assertEquals(billInfoToPayDtos, result.getModel().get("BillInfoToPayDtoList"));
        assertEquals("user/user-delivery-pay", result.getViewName());
    }

    @Test
    public void userConfirmDeliversRu() {
        Bill bill = getBill();
        BillInfoToPayDto billInfoToPayDto = getBillInfoToPayDtoEn(bill);
        billInfoToPayDto.setLocalityGetName(bill.getDelivery().getWay().getLocalityGet().getNameRu());
        billInfoToPayDto.setLocalitySandName(bill.getDelivery().getWay().getLocalitySand().getNameRu());
        List<BillInfoToPayDto> billInfoToPayDtos = Collections.singletonList(billInfoToPayDto);
        when(billService.getBillsToPayByUserID(anyLong(), any(Locale.class))).thenReturn(billInfoToPayDtos);

        ModelAndView result = userDeliveryPayController.userConfirmDelivers(httpSession, getLocaleRu());

        verify(billService, times(1)).getBillsToPayByUserID(anyLong(), any(Locale.class));
        assertEquals(billInfoToPayDtos, result.getModel().get("BillInfoToPayDtoList"));
        assertEquals("user/user-delivery-pay", result.getViewName());
    }

    private BillInfoToPayDto getBillInfoToPayDtoEn(Bill bill) {
        return BillInfoToPayDto.builder()
                .addreeseeEmail(bill.getDelivery().getAddressee().getEmail())
                .billId(bill.getId())
                .deliveryId(bill.getDelivery().getId())
                .price(bill.getCostInCents())
                .weight(bill.getDelivery().getWeight())
                .build();
    }

    @Test
    public void userNotGottenDeliversConfirmGettingDelivery() throws DeliveryAlreadyPaidException, NotEnoughMoneyException {
        when(billService.payForDelivery(anyLong(), anyLong())).thenReturn(true);

        String result = userDeliveryPayController.userNotGottenDeliversConfirmGettingDelivery(httpSession, 1L);

        verify(billService, times(1)).payForDelivery(anyLong(), anyLong());
        assertEquals("redirect:/user/user-delivery-pay", result);
    }
}