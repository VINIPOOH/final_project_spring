package ua.testing.delivery.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ua.testing.delivery.dto.BillDto;
import ua.testing.delivery.dto.BillInfoToPayDto;
import ua.testing.delivery.entity.Bill;
import ua.testing.delivery.entity.Delivery;
import ua.testing.delivery.entity.User;
import ua.testing.delivery.exception.*;
import ua.testing.delivery.repository.BillRepository;
import ua.testing.delivery.repository.DeliveryRepository;
import ua.testing.delivery.repository.UserRepository;
import ua.testing.delivery.repository.WayRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static ua.testing.delivery.service.ServisesTestConstant.*;

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

    @Before
    public void init() {

        when(billRepository.findByIdAndIsDeliveryPaidFalse(getBillId())).thenReturn(Optional.of(getBill()));
        when(billRepository.findAllByUserIdAndIsDeliveryPaidFalse(anyLong())).thenReturn(getBills());
        doAnswer((invocation) -> invocation.getArgument(0)).when(billRepository).save(any(Bill.class));

        doAnswer((invocation) -> invocation.getArgument(0)).when(deliveryRepository).save(any(Delivery.class));//
//
        when(userRepository.findByIdAndUserMoneyInCentsGreaterThanEqual(getUserId(), getBill().getCostInCents())).thenReturn(Optional.of(getAdverser()));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(getAdversee()));
        doAnswer((i) -> i.getArgument(0)).when(userRepository).save(any(User.class));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(getAdverser()));
//
        when(wayRepository.findByLocalitySand_IdAndLocalityGet_Id(anyLong(), anyLong())).thenReturn(Optional.of(getWay()));

    }

    @Test
    public void initializeBillCorrect() throws UnsupportableWeightFactorException, NoSuchUserException, NoSuchWayException {
        Bill bill = getBill();
        bill.setCostInCents(2);
        bill.setId(0);
        bill.setDeliveryPaid(false);
        Delivery delivery =getDelivery();
        delivery.setId(0);

        Bill billResult = billService.initializeBill(getDeliveryOrderCreateDto(), getUserId());

        assertEquals(bill, billResult);
    }

    @Test(expected = NoSuchUserException.class)
    public void initializeBillCorrectInCorrectAddressee() throws UnsupportableWeightFactorException, NoSuchUserException, NoSuchWayException {
        Bill bill = getBill();
        bill.setCostInCents(2);
        bill.setId(0);
        bill.setDeliveryPaid(false);
        Delivery delivery =getDelivery();
        delivery.setId(0);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        billService.initializeBill(getDeliveryOrderCreateDto(), getUserId());

        fail();
    }

    @Test(expected = NoSuchWayException.class)
    public void initializeBillIncorrectInWay() throws UnsupportableWeightFactorException, NoSuchUserException, NoSuchWayException {
        Bill bill = getBill();
        bill.setCostInCents(2);
        bill.setId(0);
        bill.setDeliveryPaid(false);
        Delivery delivery =getDelivery();
        delivery.setId(0);
        when(wayRepository.findByLocalitySand_IdAndLocalityGet_Id(anyLong(), anyLong())).thenReturn(Optional.empty());

        billService.initializeBill(getDeliveryOrderCreateDto(), getUserId());

        fail();
    }

    @Test(expected = DBWorkIncorrectException.class)
    public void initializeBillIncorrectSender() throws UnsupportableWeightFactorException, NoSuchUserException, NoSuchWayException {
        Bill bill = getBill();
        bill.setCostInCents(2);
        bill.setId(0);
        bill.setDeliveryPaid(false);
        Delivery delivery =getDelivery();
        delivery.setId(0);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        billService.initializeBill(getDeliveryOrderCreateDto(), getUserId());

        fail();
    }

    @Test
    public void getBillsToPayByUserIDLocaleRu() {
        BillInfoToPayDto billInfoToPayDto = getBillInfoToPayDto();
        Bill bill = getBill();
        billInfoToPayDto.setLocalityGetName(bill.getDelivery().getWay().getLocalityGet().getNameRu());
        billInfoToPayDto.setLocalitySandName(bill.getDelivery().getWay().getLocalitySand().getNameRu());

        List<BillInfoToPayDto> result = billService.getBillsToPayByUserID(getUserId(), getLocaleRu());

        verify(billRepository, times(1)).findAllByUserIdAndIsDeliveryPaidFalse(getUserId());
        assertEquals(getBills().size(), result.size());
        assertEquals(billInfoToPayDto, result.get(0));
        assertEquals(billInfoToPayDto, result.get(0));
    }

    @Test
    public void getBillsToPayByUserIDLocaleEn() {
        BillInfoToPayDto billInfoToPayDto = getBillInfoToPayDto();
        Bill bill = getBill();
        billInfoToPayDto.setLocalityGetName(bill.getDelivery().getWay().getLocalityGet().getNameEn());
        billInfoToPayDto.setLocalitySandName(bill.getDelivery().getWay().getLocalitySand().getNameEn());

        List<BillInfoToPayDto> billInfoToPayDtos = billService.getBillsToPayByUserID(getUserId(), getLocaleEn());

        verify(billRepository, times(1)).findAllByUserIdAndIsDeliveryPaidFalse(getUserId());
        assertEquals(getBills().size(), billInfoToPayDtos.size());
        assertEquals(billInfoToPayDto, billInfoToPayDtos.get(0));
    }


    @Test
    public void getBillsToPayByUserIDUserIsNotExist() {
        when(billRepository.findAllByUserIdAndIsDeliveryPaidFalse(getUserId())).thenReturn(new ArrayList<>());

        List<BillInfoToPayDto> billInfoToPayDtos = billService.getBillsToPayByUserID(getUserId(), getLocaleEn());

        verify(billRepository, times(1)).findAllByUserIdAndIsDeliveryPaidFalse(getUserId());
        Assert.assertEquals(0, billInfoToPayDtos.size());

    }

    @Test
    public void payForDeliveryWhenAllCorrect() throws DeliveryAlreadyPaidException, NotEnoughMoneyException {
        Bill bill = getBill();
        bill.setDeliveryPaid(false);
        when(billRepository.findByIdAndIsDeliveryPaidFalse(getBillId())).thenReturn(Optional.of(bill));

        boolean payResult = billService.payForDelivery(getUserId(), getBillId());

        verify(billRepository, times(1)).findByIdAndIsDeliveryPaidFalse(getBillId());
        verify(userRepository, times(1)).findByIdAndUserMoneyInCentsGreaterThanEqual(getUserId(), bill.getCostInCents());
        Assert.assertTrue(payResult);
        Assert.assertTrue(bill.isDeliveryPaid());
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void payForDeliveryNotEnoughMoney() throws DeliveryAlreadyPaidException, NotEnoughMoneyException {
        Bill bill = getBill();
        bill.setDeliveryPaid(false);
        User adverser = getAdverser();
        adverser.setUserMoneyInCents(0L);
        when(userRepository.findByIdAndUserMoneyInCentsGreaterThanEqual(getUserId(), bill.getCostInCents())).thenReturn(Optional.empty());

        billService.payForDelivery(getUserId(), getBillId());

        fail();
    }

    @Test(expected = DeliveryAlreadyPaidException.class)
    public void payForDeliveryDeliveryAlreadyPaid() throws DeliveryAlreadyPaidException, NotEnoughMoneyException {
        when(billRepository.findByIdAndIsDeliveryPaidFalse(getBillId())).thenReturn(Optional.empty());

        billService.payForDelivery(getUserId(), getBillId());

        fail();
    }

    @Test
    public void getBillHistoryByUserId() {
        List<Bill> bills = getBills();
        Pageable page = PageRequest.of(1,1);
        when(billRepository.findAllByUserIdAndIsDeliveryPaidTrue(1, page)).thenReturn(new PageImpl<Bill>(bills , page, 1));

        Page<BillDto> result =billService.getBillHistoryByUserId(1, page);

        verify(billRepository, times(1)).findAllByUserIdAndIsDeliveryPaidTrue(1, page);
        assertEquals(getBillDto(), result.iterator().next());
        assertEquals(bills.size(), result.getSize());
    }


}