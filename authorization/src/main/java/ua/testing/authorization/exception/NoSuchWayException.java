package ua.testing.authorization.exception;


import ua.testing.authorization.dto.DeliveryInfoRequestDto;

public class NoSuchWayException extends Exception {
    private DeliveryInfoRequestDto deliveryInfoRequestDto;

    public NoSuchWayException(DeliveryInfoRequestDto deliveryInfoRequestDto) {
        this.deliveryInfoRequestDto = deliveryInfoRequestDto;
    }
}
