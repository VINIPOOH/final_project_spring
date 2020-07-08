package ua.testing.delivery.service;

import ua.testing.delivery.dto.DeliveryInfoRequestDto;
import ua.testing.delivery.dto.DeliveryInfoToGetDto;
import ua.testing.delivery.dto.PriceAndTimeOnDeliveryDto;
import ua.testing.delivery.exception.AskedDataIsNotExist;
import ua.testing.delivery.exception.NoSuchWayException;
import ua.testing.delivery.exception.UnsupportableWeightFactorException;

import java.util.List;
import java.util.Locale;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface DeliveryService {

    List<DeliveryInfoToGetDto> getDeliveryInfoToGet(long userId, Locale locale);

    boolean confirmGettingDelivery(long userId, long deliveryId) throws AskedDataIsNotExist;

    PriceAndTimeOnDeliveryDto getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto) throws NoSuchWayException, UnsupportableWeightFactorException;

}
