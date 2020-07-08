package ua.testing.delivery.dto;

import lombok.Builder;
import lombok.Data;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Builder
@Data
public class BillInfoToPayDto {
    private long billId;
    private long price;
    private long deliveryId;
    private int weight;
    private String addreeseeEmail;
    private String localitySandName;
    private String localityGetName;

    BillInfoToPayDto(long billId, long price, long deliveryId, int weight, String addreeseeEmail, String localitySandName, String localityGetName) {
        this.billId = billId;
        this.price = price;
        this.deliveryId = deliveryId;
        this.weight = weight;
        this.addreeseeEmail = addreeseeEmail;
        this.localitySandName = localitySandName;
        this.localityGetName = localityGetName;
    }


}
