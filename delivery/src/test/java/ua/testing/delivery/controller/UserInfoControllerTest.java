package ua.testing.delivery.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;
import ua.testing.delivery.controller.util.Util;
import ua.testing.delivery.dto.BillDto;
import ua.testing.delivery.entity.Bill;
import ua.testing.delivery.entity.User;
import ua.testing.delivery.service.BillService;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static ua.testing.delivery.ServisesTestConstant.getAddreser;
import static ua.testing.delivery.ServisesTestConstant.getBill;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = UserInfoController.class)
public class UserInfoControllerTest {

    @Autowired
    UserInfoController userInfoController;

    @MockBean
    BillService billService;

    User user = getAddreser();
    HttpSession httpSession;

    @Before
    public void setUp() throws Exception {
        user = getAddreser();
        httpSession = new MockHttpSession();
        Util.addUserToSession(httpSession,user);
    }

    @Test
    public void userStatistic() {
        Bill bill = getBill();
        BillDto billDto= getBillDto(bill);
        List<BillDto> billInfoToPayDtos = Collections.singletonList(billDto);
        when(billService.getBillHistoryByUserId(anyLong(), any(Pageable.class))).thenReturn(new PageImpl(billInfoToPayDtos, PageRequest.of(1, 1),1));

        ModelAndView result = userInfoController.userStatistic(httpSession, PageRequest.of(1, 1));

        verify(billService, times(1)).getBillHistoryByUserId(anyLong(), any(Pageable.class));
        assertEquals(billInfoToPayDtos, ((Page<BillDto>)result.getModel().get("BillDtoPage")).toList());
        assertEquals("user/user-statistic", result.getViewName());
    }

    private BillDto getBillDto(Bill bill) {
        return BillDto.builder()
                .costInCents(bill.getCostInCents())
                .deliveryId(bill.getDelivery().getId())
                .id(bill.getId())
                .isDeliveryPaid(bill.isDeliveryPaid())
                .dateOfPay(bill.getDateOfPay())
                .build();
    }
}