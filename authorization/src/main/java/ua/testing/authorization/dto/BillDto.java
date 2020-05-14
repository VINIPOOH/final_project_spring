package ua.testing.authorization.dto;

import java.time.LocalDate;

public class BillDto {
    private long deliveryId;
    private long id;
    private Boolean isDeliveryPaid;
    private long costInCents;
    private LocalDate dateOfPay;

    BillDto(long deliveryId, long id, Boolean isDeliveryPaid, long costInCents, LocalDate dateOfPay) {
        this.deliveryId = deliveryId;
        this.id = id;
        this.isDeliveryPaid = isDeliveryPaid;
        this.costInCents = costInCents;
        this.dateOfPay = dateOfPay;
    }

    public static BillDtoBuilder builder() {
        return new BillDtoBuilder();
    }

    public LocalDate getDateOfPay() {
        return dateOfPay;
    }

    public long getDeliveryId() {
        return this.deliveryId;
    }

    public void setDeliveryId(long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCostInCents() {
        return this.costInCents;
    }

    public void setCostInCents(long costInCents) {
        this.costInCents = costInCents;
    }


    public static class BillDtoBuilder {
        private long deliveryId;
        private long id;
        private Boolean isDeliveryPaid;
        private long costInCents;
        private LocalDate dateOfPay;

        BillDtoBuilder() {
        }

        public BillDto.BillDtoBuilder deliveryId(long deliveryId) {
            this.deliveryId = deliveryId;
            return this;
        }

        public BillDto.BillDtoBuilder id(long id) {
            this.id = id;
            return this;
        }

        public BillDto.BillDtoBuilder isDeliveryPaid(Boolean isDeliveryPaid) {
            this.isDeliveryPaid = isDeliveryPaid;
            return this;
        }

        public BillDto.BillDtoBuilder costInCents(long costInCents) {
            this.costInCents = costInCents;
            return this;
        }

        public BillDto.BillDtoBuilder dateOfPay(LocalDate dateOfPay) {
            this.dateOfPay = dateOfPay;
            return this;
        }

        public BillDto build() {
            return new BillDto(deliveryId, id, isDeliveryPaid, costInCents, dateOfPay);
        }
    }
}
