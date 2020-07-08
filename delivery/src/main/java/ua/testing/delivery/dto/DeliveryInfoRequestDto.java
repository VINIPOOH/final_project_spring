package ua.testing.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryInfoRequestDto {

    @Positive
    private int deliveryWeight;

    @PositiveOrZero
    private long localitySandID;

    @PositiveOrZero
    private long localityGetID;
}
