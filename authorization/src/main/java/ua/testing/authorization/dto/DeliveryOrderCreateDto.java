package ua.testing.authorization.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
public class DeliveryOrderCreateDto {
    @Positive
    private int deliveryWeight;

    @PositiveOrZero
    private long localitySandID;

    @PositiveOrZero
    private long localityGetID;
    @Email
    private String addresseeEmail;
}
