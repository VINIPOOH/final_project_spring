package ua.testing.authorization.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryCostAndTimeDto {
    private int costInCents;
    private int timeOnWayInHours;
}
