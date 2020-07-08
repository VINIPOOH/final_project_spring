package ua.testing.delivery.dto;

import lombok.EqualsAndHashCode;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@EqualsAndHashCode
public class DeliveryInfoToGetDto {
    private String addresserEmail;
    private Long deliveryId;
    private String localitySandName;
    private String localityGetName;

    DeliveryInfoToGetDto(String addresserEmail, Long deliveryId, String localitySandName, String localityGetName) {
        this.addresserEmail = addresserEmail;
        this.deliveryId = deliveryId;
        this.localitySandName = localitySandName;
        this.localityGetName = localityGetName;
    }

    public static DeliveryInfoToGetDtoBuilder builder() {
        return new DeliveryInfoToGetDtoBuilder();
    }

    public String getAddresserEmail() {
        return this.addresserEmail;
    }

    public Long getDeliveryId() {
        return this.deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getLocalitySandName() {
        return this.localitySandName;
    }

    public void setLocalitySandName(String localitySandName) {
        this.localitySandName = localitySandName;
    }

    public String getLocalityGetName() {
        return this.localityGetName;
    }

    public void setLocalityGetName(String localityGetName) {
        this.localityGetName = localityGetName;
    }


    public static class DeliveryInfoToGetDtoBuilder {
        private String addresserEmail;
        private Long deliveryId;
        private String localitySandName;
        private String localityGetName;

        DeliveryInfoToGetDtoBuilder() {
        }

        public DeliveryInfoToGetDto.DeliveryInfoToGetDtoBuilder addresserEmail(String addresserEmail) {
            this.addresserEmail = addresserEmail;
            return this;
        }

        public DeliveryInfoToGetDto.DeliveryInfoToGetDtoBuilder deliveryId(Long deliveryId) {
            this.deliveryId = deliveryId;
            return this;
        }

        public DeliveryInfoToGetDto.DeliveryInfoToGetDtoBuilder localitySandName(String localitySandName) {
            this.localitySandName = localitySandName;
            return this;
        }

        public DeliveryInfoToGetDto.DeliveryInfoToGetDtoBuilder localityGetName(String localityGetName) {
            this.localityGetName = localityGetName;
            return this;
        }

        public DeliveryInfoToGetDto build() {
            return new DeliveryInfoToGetDto(addresserEmail, deliveryId, localitySandName, localityGetName);
        }


    }
}
