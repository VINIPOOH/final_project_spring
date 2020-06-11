package ua.testing.delivery.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ua.testing.delivery.dto.DeliveryInfoRequestDto;
import ua.testing.delivery.dto.DeliveryInfoToGetDto;
import ua.testing.delivery.dto.PriceAndTimeOnDeliveryDto;
import ua.testing.delivery.entity.Delivery;
import ua.testing.delivery.entity.Way;
import ua.testing.delivery.exception.AskedDataIsNotExist;
import ua.testing.delivery.exception.NoSuchWayException;
import ua.testing.delivery.exception.UnsupportableWeightFactorException;
import ua.testing.delivery.repository.DeliveryRepository;
import ua.testing.delivery.repository.WayRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static ua.testing.delivery.ServisesTestConstant.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DeliveryService.class)
public class DeliveryServiceTest {

    @Autowired
    DeliveryService deliveryService;

    @MockBean
    WayRepository wayRepository;
    @MockBean
    DeliveryRepository deliveryRepository;

    @Before
    public void setUp() throws Exception {
        doAnswer((i) -> i.getArgument(0)).when(deliveryRepository).save(any(Delivery.class));
    }

    @Test
    public void getDeliveryInfoToGetRu() {
        Delivery delivery = getDelivery();
        delivery.setBill(getBill());
        DeliveryInfoToGetDto deliveryInfoToGetDto = getDeliveryInfoToGetDto();
        deliveryInfoToGetDto.setLocalityGetName(delivery.getWay().getLocalityGet().getNameRu());
        deliveryInfoToGetDto.setLocalitySandName(delivery.getWay().getLocalitySand().getNameRu());
        when(deliveryRepository.findAllByAddressee_IdAndIsPackageReceivedFalseAndBill_IsDeliveryPaidTrue(getUserId())).thenReturn(Collections.singletonList(delivery));

        List<DeliveryInfoToGetDto> result = deliveryService.getDeliveryInfoToGet(getUserId(), getLocaleRu());

        verify(deliveryRepository, times(1)).findAllByAddressee_IdAndIsPackageReceivedFalseAndBill_IsDeliveryPaidTrue(getUserId());
        assertEquals(deliveryInfoToGetDto, result.get(0));
        assertEquals(getDeliveres().size(), result.size());
    }

    @Test
    public void getDeliveryInfoToGetEn() {
        Delivery delivery = getDelivery();
        delivery.setBill(getBill());
        DeliveryInfoToGetDto deliveryInfoToGetDto = getDeliveryInfoToGetDto();
        deliveryInfoToGetDto.setLocalityGetName(delivery.getWay().getLocalityGet().getNameEn());
        deliveryInfoToGetDto.setLocalitySandName(delivery.getWay().getLocalitySand().getNameEn());
        when(deliveryRepository.findAllByAddressee_IdAndIsPackageReceivedFalseAndBill_IsDeliveryPaidTrue(getUserId())).thenReturn(Collections.singletonList(delivery));

        List<DeliveryInfoToGetDto> result = deliveryService.getDeliveryInfoToGet(getUserId(), getLocaleEn());

        verify(deliveryRepository, times(1)).findAllByAddressee_IdAndIsPackageReceivedFalseAndBill_IsDeliveryPaidTrue(getUserId());
        assertEquals(deliveryInfoToGetDto, result.get(0));
        assertEquals(getDeliveres().size(), result.size());
    }

    @Test
    public void confirmGettingDeliveryAllCorrect() throws AskedDataIsNotExist {
        Delivery delivery = getDelivery();
        when(deliveryRepository.findByIdAndAddressee_IdAndIsPackageReceivedFalse(getDeliveryId(), getUserId()))
                .thenReturn(Optional.ofNullable(delivery));

        boolean result = deliveryService.confirmGettingDelivery(getUserId(), getDeliveryId());

        verify(deliveryRepository, times(1)).findByIdAndAddressee_IdAndIsPackageReceivedFalse(getDeliveryId(), getUserId());
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
        assertTrue(result);
        assertTrue(delivery.isPackageReceived());
    }

    @Test(expected = AskedDataIsNotExist.class)
    public void confirmGettingDeliveryIsNoExistDelivery() throws AskedDataIsNotExist {
        when(deliveryRepository.findByIdAndAddressee_IdAndIsPackageReceivedFalse(getDeliveryId(), getUserId()))
                .thenReturn(Optional.empty());

        deliveryService.confirmGettingDelivery(getUserId(), getDeliveryId());

        fail();
    }

    @Test
    public void getDeliveryCostAndTimeDtoAllCorrect() throws UnsupportableWeightFactorException, NoSuchWayException {
        DeliveryInfoRequestDto deliveryInfoRequestDto = getDeliveryInfoRequestDto(1);
        PriceAndTimeOnDeliveryDto priceAndTimeOnDeliveryDto = getPriceAndTimeOnDeliveryDto();
        Delivery delivery = getDelivery();
        Way way = delivery.getWay();
        when(wayRepository.findByLocalitySand_IdAndLocalityGet_Id(anyLong(),
                anyLong())).thenReturn(Optional.of(way));

        PriceAndTimeOnDeliveryDto result = deliveryService.getDeliveryCostAndTimeDto(deliveryInfoRequestDto);

        verify(wayRepository, times(1)).findByLocalitySand_IdAndLocalityGet_Id
                (deliveryInfoRequestDto.getLocalitySandID(), deliveryInfoRequestDto.getLocalityGetID());
        assertEquals(priceAndTimeOnDeliveryDto, result);
    }

    @Test(expected = NoSuchWayException.class)
    public void getDeliveryCostAndTimeIncorrectWay() throws UnsupportableWeightFactorException, NoSuchWayException {
        DeliveryInfoRequestDto deliveryInfoRequestDto = getDeliveryInfoRequestDto(1);
        PriceAndTimeOnDeliveryDto priceAndTimeOnDeliveryDto = getPriceAndTimeOnDeliveryDto();
        when(wayRepository.findByLocalitySand_IdAndLocalityGet_Id(anyLong(),
                anyLong())).thenReturn(Optional.empty());

        PriceAndTimeOnDeliveryDto result = deliveryService.getDeliveryCostAndTimeDto(deliveryInfoRequestDto);

        fail();
    }

    @Test(expected = UnsupportableWeightFactorException.class)
    public void getDeliveryCostAndTimeIncorrectWeightFactorBiggerOnOne() throws UnsupportableWeightFactorException, NoSuchWayException {
        int weightRangeMax = 2;
        int weightRangeReal = 2;
        DeliveryInfoRequestDto deliveryInfoRequestDto = getDeliveryInfoRequestDto(weightRangeReal);

        Delivery delivery = getDelivery();
        Way way = delivery.getWay();
        way.getWayTariffs().get(0).setMaxWeightRange(weightRangeMax);
        when(wayRepository.findByLocalitySand_IdAndLocalityGet_Id(anyLong(),
                anyLong())).thenReturn(Optional.of(way));

        deliveryService.getDeliveryCostAndTimeDto(deliveryInfoRequestDto);

        fail();
    }

    @Test()
    public void getDeliveryCostAndTimeWeightFactorOnHighestRange() throws UnsupportableWeightFactorException, NoSuchWayException {
        int weightRangeMax = 2;
        int weightRangeReal = 1;
        DeliveryInfoRequestDto deliveryInfoRequestDto = getDeliveryInfoRequestDto(weightRangeReal);
        PriceAndTimeOnDeliveryDto priceAndTimeOnDeliveryDto = getPriceAndTimeOnDeliveryDto();
        Delivery delivery = getDelivery();
        Way way = delivery.getWay();
        way.getWayTariffs().get(0).setMaxWeightRange(weightRangeMax);
        when(wayRepository.findByLocalitySand_IdAndLocalityGet_Id(anyLong(),
                anyLong())).thenReturn(Optional.of(way));

        PriceAndTimeOnDeliveryDto result = deliveryService.getDeliveryCostAndTimeDto(deliveryInfoRequestDto);

        verify(wayRepository, times(1)).findByLocalitySand_IdAndLocalityGet_Id
                (deliveryInfoRequestDto.getLocalitySandID(), deliveryInfoRequestDto.getLocalityGetID());
        assertEquals(priceAndTimeOnDeliveryDto, result);
    }

    private DeliveryInfoRequestDto getDeliveryInfoRequestDto(int weightRangeReal) {
        return DeliveryInfoRequestDto.builder()
                .deliveryWeight(weightRangeReal)
                .localityGetID(1)
                .localitySandID(2)
                .build();
    }

    private PriceAndTimeOnDeliveryDto getPriceAndTimeOnDeliveryDto() {
        return PriceAndTimeOnDeliveryDto.builder()
                .timeOnWayInHours(1)
                .costInCents(2)
                .build();
    }
}