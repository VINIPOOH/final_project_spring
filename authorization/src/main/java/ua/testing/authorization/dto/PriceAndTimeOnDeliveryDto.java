package ua.testing.authorization.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceAndTimeOnDeliveryDto {
    private int costInCents;
    private int timeOnWayInHours;
}
