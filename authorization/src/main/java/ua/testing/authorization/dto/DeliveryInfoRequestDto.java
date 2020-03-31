package ua.testing.authorization.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
public class DeliveryInfoRequestDto {

    @Positive
    private int deliveryWeight;

    @PositiveOrZero
    private long localitySandID;

    @PositiveOrZero
    private long localityGetID;
}
