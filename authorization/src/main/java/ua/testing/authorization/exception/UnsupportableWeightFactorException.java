package ua.testing.authorization.exception;

import ua.testing.authorization.dto.DeliveryInfoRequestDto;

public class UnsupportableWeightFactorException extends Exception {
    private DeliveryInfoRequestDto deliveryInfoRequestDto;

    public UnsupportableWeightFactorException(DeliveryInfoRequestDto deliveryInfoRequestDto) {
        this.deliveryInfoRequestDto = deliveryInfoRequestDto;
    }
}
