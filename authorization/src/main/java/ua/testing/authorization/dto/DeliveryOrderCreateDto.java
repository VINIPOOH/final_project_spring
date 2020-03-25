package ua.testing.authorization.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@ToString
public class DeliveryOrderCreateDto {
    @Positive
    private int deliveryWeight;

    @PositiveOrZero
    private long localitySandID;

    @PositiveOrZero
    private long localityGetID;
    @Email
    private String addresseeEmail;

    private String addresserEmail;
}
