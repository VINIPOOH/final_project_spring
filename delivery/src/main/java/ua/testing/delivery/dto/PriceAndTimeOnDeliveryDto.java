package ua.testing.delivery.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class PriceAndTimeOnDeliveryDto {
    private int costInCents;
    private int timeOnWayInHours;
}
