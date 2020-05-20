package ua.testing.authorization.service;

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
import ua.testing.authorization.dto.BillDto;
import ua.testing.authorization.dto.BillInfoToPayDto;
import ua.testing.authorization.dto.DeliveryOrderCreateDto;
import ua.testing.authorization.entity.*;
import ua.testing.authorization.exception.*;
import ua.testing.authorization.repository.BillRepository;
import ua.testing.authorization.repository.DeliveryRepository;
import ua.testing.authorization.repository.UserRepository;
import ua.testing.authorization.repository.WayRepository;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = BillService.class)
public class BillServiceTest {

    public static DeliveryOrderCreateDto DELIVERY_ORDER_CREATE_DTO = getDeliveryOrderCreateDto();


    private final static long USER_ID = 1L;


    private static final long BILL_ID = 1L;
    private static final Locale LOCALE_EN = new Locale("en");
    private static final Locale LOCALE_RU = new Locale("ru");

    private static TariffWeightFactor tariffWeightFactor = getTariffWeightFactor();

    private static List<TariffWeightFactor> tariffWeightFactors = Collections.singletonList(tariffWeightFactor);

    private static User adverser = getAdverser();
    private static User adversee = getAdversee();
    private static Way way = getWay();
    private static Delivery delivery = getDelivery();
    private static Bill bill = getBill();
    private static BillInfoToPayDto billInfoToPayDto = getBillInfoToPayDto();
    private static BillDto  billDto = getBillDto();

    private static List<BillDto> billDtos = Collections.singletonList(billDto);


    private static List<Bill> bills = Collections.singletonList(bill);

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
        way = getWay();
        delivery = getDelivery();
        bill = getBill();
        billInfoToPayDto = getBillInfoToPayDto();
        bills = Collections.singletonList(bill);
        adverser = getAdverser();
        adversee = getAdversee();

        when(billRepository.findByIdAndIsDeliveryPaidFalse(BILL_ID)).thenReturn(Optional.of(bill));
        when(billRepository.findAllByUserIdAndIsDeliveryPaidFalse(anyLong())).thenReturn(bills);
        doAnswer((invocation) -> invocation.getArgument(0)).when(billRepository).save(any(Bill.class));

        doAnswer((invocation) -> invocation.getArgument(0)).when(deliveryRepository).save(any(Delivery.class));//
//
        when(userRepository.findByIdAndUserMoneyInCentsGreaterThanEqual(USER_ID, bill.getCostInCents())).thenReturn(Optional.of(adverser));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(adversee));
        doAnswer((i) -> i.getArgument(0)).when(userRepository).save(any(User.class));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(adverser));
//
        when(wayRepository.findByLocalitySand_IdAndLocalityGet_Id(anyLong(), anyLong())).thenReturn(Optional.of(way));

    }

    @Test
    public void initializeBillCorrect() throws UnsupportableWeightFactorException, NoSuchUserException, NoSuchWayException {
        bill.setCostInCents(2);
        bill.setId(0);
        delivery.setId(0);
        bill.setDeliveryPaid(false);

        Bill billResult = billService.initializeBill(DELIVERY_ORDER_CREATE_DTO, USER_ID);

        assertEquals(bill, billResult);
    }

    @Test(expected = NoSuchUserException.class)
    public void initializeBillCorrectInCorrectAddressee() throws UnsupportableWeightFactorException, NoSuchUserException, NoSuchWayException {
        bill.setCostInCents(2);
        bill.setId(0);
        delivery.setId(0);
        bill.setDeliveryPaid(false);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        billService.initializeBill(DELIVERY_ORDER_CREATE_DTO, USER_ID);

        fail();
    }

    @Test(expected = NoSuchWayException.class)
    public void initializeBillIncorrectInWay() throws UnsupportableWeightFactorException, NoSuchUserException, NoSuchWayException {
        bill.setCostInCents(2);
        bill.setId(0);
        delivery.setId(0);
        bill.setDeliveryPaid(false);
        when(wayRepository.findByLocalitySand_IdAndLocalityGet_Id(anyLong(), anyLong())).thenReturn(Optional.empty());

        billService.initializeBill(DELIVERY_ORDER_CREATE_DTO, USER_ID);

        fail();
    }

    @Test(expected = DBWorkIncorrectException.class)
    public void initializeBillIncorrectSender() throws UnsupportableWeightFactorException, NoSuchUserException, NoSuchWayException {
        bill.setCostInCents(2);
        bill.setId(0);
        delivery.setId(0);
        bill.setDeliveryPaid(false);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        billService.initializeBill(DELIVERY_ORDER_CREATE_DTO, USER_ID);

        fail();
    }

    @Test
    public void getBillsToPayByUserIDLocaleRu() {
        billInfoToPayDto.setLocalityGetName(bill.getDelivery().getWay().getLocalityGet().getNameRu());
        billInfoToPayDto.setLocalitySandName(bill.getDelivery().getWay().getLocalitySand().getNameRu());

        List<BillInfoToPayDto> billInfoToPayDtos = billService.getBillsToPayByUserID(USER_ID, LOCALE_RU);

        verify(billRepository, times(1)).findAllByUserIdAndIsDeliveryPaidFalse(USER_ID);
        Assert.assertEquals(bills.size(), billInfoToPayDtos.size());

    }

    @Test
    public void getBillsToPayByUserIDLocaleEn() {
        billInfoToPayDto.setLocalityGetName(bill.getDelivery().getWay().getLocalityGet().getNameEn());
        billInfoToPayDto.setLocalitySandName(bill.getDelivery().getWay().getLocalitySand().getNameEn());

        List<BillInfoToPayDto> billInfoToPayDtos = billService.getBillsToPayByUserID(USER_ID, LOCALE_EN);

        verify(billRepository, times(1)).findAllByUserIdAndIsDeliveryPaidFalse(USER_ID);
        Assert.assertEquals(bills.size(), billInfoToPayDtos.size());
    }


    @Test
    public void getBillsToPayByUserIDUserIsNotExist() {
        when(billRepository.findAllByUserIdAndIsDeliveryPaidFalse(USER_ID)).thenReturn(new ArrayList<>());

        List<BillInfoToPayDto> billInfoToPayDtos = billService.getBillsToPayByUserID(USER_ID, LOCALE_EN);

        verify(billRepository, times(1)).findAllByUserIdAndIsDeliveryPaidFalse(USER_ID);
        Assert.assertEquals(0, billInfoToPayDtos.size());
    }

    @Test
    public void payForDeliveryWhenAllCorrect() throws DeliveryAlreadyPaidException, NotEnoughMoneyException {
        bill.setDeliveryPaid(false);

        boolean payResult = billService.payForDelivery(USER_ID, BILL_ID);

        verify(billRepository, times(1)).findByIdAndIsDeliveryPaidFalse(BILL_ID);
        verify(userRepository, times(1)).findByIdAndUserMoneyInCentsGreaterThanEqual(USER_ID, bill.getCostInCents());
        Assert.assertTrue(payResult);
        Assert.assertTrue(bill.isDeliveryPaid());
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void payForDeliveryNotEnoughMoney() throws DeliveryAlreadyPaidException, NotEnoughMoneyException {
        bill.setDeliveryPaid(false);
        adverser.setUserMoneyInCents(0L);
        when(userRepository.findByIdAndUserMoneyInCentsGreaterThanEqual(USER_ID, bill.getCostInCents())).thenReturn(Optional.empty());

        billService.payForDelivery(USER_ID, BILL_ID);

        fail();
    }

    @Test(expected = DeliveryAlreadyPaidException.class)
    public void payForDeliveryDeliveryAlreadyPaid() throws DeliveryAlreadyPaidException, NotEnoughMoneyException {
        when(billRepository.findByIdAndIsDeliveryPaidFalse(BILL_ID)).thenReturn(Optional.empty());

        billService.payForDelivery(USER_ID, BILL_ID);

        fail();
    }

    @Test
    public void getBillHistoryByUserId() {
        Pageable page = PageRequest.of(1,1);
        when(billRepository.findAllByUserIdAndIsDeliveryPaidTrue(1, page)).thenReturn(new PageImpl<Bill>(bills , page, 1));

        Page<BillDto> result =billService.getBillHistoryByUserId(1, page);

        verify(billRepository, times(1)).findAllByUserIdAndIsDeliveryPaidTrue(1, page);
        assertEquals(billDto, result.iterator().next());
        assertEquals(bills.size(), result.getSize());
    }

    private static User getAdversee() {
        return User.builder()
                .id(USER_ID)
                .email("emailAdresee")
                .userMoneyInCents(0L)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .password("password")
                .roleType(RoleType.ROLE_USER)
                .build();
    }

    private static User getAdverser() {
        return User.builder()
                .id(USER_ID)
                .email("emailAdreser")
                .userMoneyInCents(300L)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .password("password")
                .roleType(RoleType.ROLE_USER)
                .build();
    }

    private static BillInfoToPayDto getBillInfoToPayDto() {
        return BillInfoToPayDto.builder()
                .billId(bill.getId())
                .deliveryId(bill.getDelivery().getId())
                .price(bill.getCostInCents())
                .weight(bill.getDelivery().getWeight())
                .addreeseeEmail(bill.getDelivery().getAddressee().getEmail())
                .build();
    }

    private static TariffWeightFactor getTariffWeightFactor() {
        return TariffWeightFactor.builder()
                .id(1)
                .minWeightRange(0)
                .maxWeightRange(100)
                .overPayOnKilometer(1)
                .build();
    }

    private static Way getWay() {
        return Way.builder()
                .id(1)
                .localityGet(Locality.builder().nameEn("EnGet").nameRu("RuGet").build())
                .localitySand(Locality.builder().nameEn("EnSend").nameRu("RuSend").build())
                .distanceInKilometres(1)
                .wayTariffs(tariffWeightFactors)
                .priceForKilometerInCents(1)
                .timeOnWayInDays(1)
                .build();
    }

    private static Delivery getDelivery() {
        return Delivery.builder()
                .weight(1)
                .id(1L)
                .addressee(User.builder().email("email").build())
                .way(way)
                .isPackageReceived(false)
                .build();
    }

    private static Bill getBill() {
        return Bill.builder()
                .id(1L)
                .costInCents(1)
                .isDeliveryPaid(true)
                .delivery(delivery)
                .user(adverser)
                .build();
    }

    private static BillDto getBillDto() {
        return BillDto.builder()
                .costInCents(1)
                .deliveryId(1)
                .isDeliveryPaid(true)
                .id(1)
                .build();
    }

    private static DeliveryOrderCreateDto getDeliveryOrderCreateDto() {
        return DeliveryOrderCreateDto
                .builder()
                .addresseeEmail("email")
                .deliveryWeight(1)
                .localityGetID(1)
                .localitySandID(2)
                .build();
    }
}