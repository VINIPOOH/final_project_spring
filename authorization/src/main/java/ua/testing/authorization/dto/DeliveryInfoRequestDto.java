package ua.testing.authorization.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@ToString
public class DeliveryInfoRequestDto {

    @Positive
    private int deliveryWeight;

    @PositiveOrZero
    private int localitySandID;

    @PositiveOrZero
    private int localityGetID;
}
