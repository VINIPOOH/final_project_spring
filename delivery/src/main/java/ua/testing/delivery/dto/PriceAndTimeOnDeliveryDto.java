package ua.testing.delivery.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * Dto for transport info about how many time need to deliver {@link ua.testing.delivery.entity.Delivery}
 * and рow much it will be cost
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Data
@Builder
@EqualsAndHashCode
public class PriceAndTimeOnDeliveryDto {
    private int costInCents;
    private int timeOnWayInHours;
}
