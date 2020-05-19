package ua.testing.authorization.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ua.testing.authorization.dto.BillInfoToPayDto;
import ua.testing.authorization.entity.*;
import ua.testing.authorization.repository.BillRepository;
import ua.testing.authorization.repository.DeliveryRepository;
import ua.testing.authorization.repository.UserRepository;
import ua.testing.authorization.repository.WayRepository;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BillService.class)
public class BillServiceTest {
    @Autowired
    BillService billService;

    @MockBean
    BillRepository billRepository;
    @MockBean
    UserRepository userRepository;
    @MockBean
    DeliveryRepository deliveryRepository;
    @MockBean
    WayRepository wayRepository;

    @Test
    public void getBillsToPayByUserIDLocaleRu() {
        List<Bill> bills = getBills();
        long existUserId = 1L;
        Locale locale = new Locale("ru");

        when(billRepository.findAllByUserIdAndIsDeliveryPaidFalse(existUserId)).thenReturn(bills);

        List<BillInfoToPayDto> billInfoToPayDtos = billService.getBillsToPayByUserID(existUserId, locale);
        checkOnUnexpectedModifiedDataGetBilsToPay(bills, billInfoToPayDtos, locale);
        verify(billRepository, times(1)).findAllByUserIdAndIsDeliveryPaidFalse(existUserId);
    }
    @Test
    public void getBillsToPayByUserIDLocaleEn() {
        List<Bill> bills = getBills();
        long existUserId = 1L;
        Locale locale = new Locale("en");

        when(billRepository.findAllByUserIdAndIsDeliveryPaidFalse(existUserId)).thenReturn(bills);

        List<BillInfoToPayDto> billInfoToPayDtos = billService.getBillsToPayByUserID(existUserId, locale);
        checkOnUnexpectedModifiedDataGetBilsToPay(bills, billInfoToPayDtos, locale);
        verify(billRepository, times(1)).findAllByUserIdAndIsDeliveryPaidFalse(existUserId);
    }

    private void checkOnUnexpectedModifiedDataGetBilsToPay(List<Bill> bills, List<BillInfoToPayDto> billInfoToPayDtos, Locale locale) {
        Assert.assertEquals(bills.size(), billInfoToPayDtos.size());
        Iterator billsEntityIterator = bills.iterator();
        for (BillInfoToPayDto billDto :
                billInfoToPayDtos) {
            Bill bill = (Bill) billsEntityIterator.next();
            Assert.assertEquals(billDto.getBillId(), bill.getId());
            Assert.assertEquals(billDto.getDeliveryId(), bill.getDelivery().getId());
            Assert.assertEquals(billDto.getPrice(), bill.getCostInCents());
            Assert.assertEquals(billDto.getWeight(), bill.getDelivery().getWeight());
            Assert.assertEquals(billDto.getAddreeseeEmail(), bill.getDelivery().getAddressee().getEmail());
            if (locale.getLanguage().equals("ru")){
                Assert.assertEquals(billDto.getLocalityGetName(), bill.getDelivery().getWay().getLocalityGet().getNameRu());
                Assert.assertEquals(billDto.getLocalitySandName(), bill.getDelivery().getWay().getLocalitySand().getNameRu());
            }
            else {
                Assert.assertEquals(billDto.getLocalityGetName(), bill.getDelivery().getWay().getLocalityGet().getNameEn());
                Assert.assertEquals(billDto.getLocalitySandName(), bill.getDelivery().getWay().getLocalitySand().getNameEn());
            }
        }
    }

    private List<Bill> getBills() {
        return Arrays.asList(
                Bill.builder()
                        .id(1L)
                        .dateOfPay(LocalDate.now())
                        .costInCents(12)
                        .isDeliveryPaid(true)
                        .delivery(Delivery.builder()
                                .weight(2)
                                .id(1L)
                                .addressee(User.builder().email("email").build())
                                .way(Way.builder()
                                        .localityGet(Locality.builder().nameEn("EnGet").nameRu("RuGet").build())
                                        .localitySand(Locality.builder().nameEn("EnSend").nameRu("RuSend").build())
                                        .build())
                                .build())
                        .build(),
                Bill.builder()
                        .id(2L)
                        .dateOfPay(LocalDate.now())
                        .costInCents(32)
                        .isDeliveryPaid(true)
                        .delivery(Delivery.builder()
                                .weight(4)
                                .id(2L)
                                .addressee(User.builder().email("email").build())
                                .way(Way.builder()
                                        .localityGet(Locality.builder().nameEn("EnGet").nameRu("RuGet").build())
                                        .localitySand(Locality.builder().nameEn("EnSend").nameRu("RuSend").build())
                                        .build())
                                .build())
                        .build(),
                Bill.builder()
                        .id(3L)
                        .dateOfPay(LocalDate.now())
                        .costInCents(12)
                        .isDeliveryPaid(true)
                        .delivery(Delivery.builder()
                                .weight(2)
                                .id(3L)
                                .addressee(User.builder().email("email").build())
                                .way(Way.builder()
                                        .localityGet(Locality.builder().nameEn("EnGet").nameRu("RuGet").build())
                                        .localitySand(Locality.builder().nameEn("EnSend").nameRu("RuSend").build())
                                        .build())
                                .build())
                        .build()
        );
    }

    @Test
    public void getBillsToPayByUserIDUserIsNotExist() {
        long notExistUserId = 1L;
        Locale locale = new Locale("en");

        when(billRepository.findAllByUserIdAndIsDeliveryPaidFalse(notExistUserId)).thenReturn(new ArrayList<>());

        Assert.assertEquals(0, billService.getBillsToPayByUserID(notExistUserId, locale).size());
        verify(billRepository, times(1)).findAllByUserIdAndIsDeliveryPaidFalse(notExistUserId);
    }


    @Test
    public void payForDelivery() {
    }

    @Test
    public void initializeBill() {
    }

    @Test
    public void getBillHistoryByUserId() {
    }
}