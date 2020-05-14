package ua.testing.authorization.dto;

public class BillInfoToPayDto {
    private long billId;
    private long price;
    private long deliveryId;
    private int weight;
    private String addreeserEmail;
    private String localitySandName;
    private String localityGetName;

    BillInfoToPayDto(long billId, long price, long deliveryId, int weight, String addreeserEmail, String localitySandName, String localityGetName) {
        this.billId = billId;
        this.price = price;
        this.deliveryId = deliveryId;
        this.weight = weight;
        this.addreeserEmail = addreeserEmail;
        this.localitySandName = localitySandName;
        this.localityGetName = localityGetName;
    }

    public static BillInfoToPayDtoBuilder builder() {
        return new BillInfoToPayDtoBuilder();
    }

    public long getBillId() {
        return this.billId;
    }

    public void setBillId(long billId) {
        this.billId = billId;
    }

    public long getPrice() {
        return this.price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getDeliveryId() {
        return this.deliveryId;
    }

    public void setDeliveryId(long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getAddreeserEmail() {
        return this.addreeserEmail;
    }

    public void setAddreeserEmail(String addreeserEmail) {
        this.addreeserEmail = addreeserEmail;
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


    public static class BillInfoToPayDtoBuilder {
        private long billId;
        private long price;
        private long deliveryId;
        private int weight;
        private String addreeserEmail;
        private String localitySandName;
        private String localityGetName;

        BillInfoToPayDtoBuilder() {
        }

        public BillInfoToPayDto.BillInfoToPayDtoBuilder billId(long billId) {
            this.billId = billId;
            return this;
        }

        public BillInfoToPayDto.BillInfoToPayDtoBuilder price(long price) {
            this.price = price;
            return this;
        }

        public BillInfoToPayDto.BillInfoToPayDtoBuilder deliveryId(long deliveryId) {
            this.deliveryId = deliveryId;
            return this;
        }

        public BillInfoToPayDto.BillInfoToPayDtoBuilder weight(int weight) {
            this.weight = weight;
            return this;
        }

        public BillInfoToPayDto.BillInfoToPayDtoBuilder addreeserEmail(String addreeserEmail) {
            this.addreeserEmail = addreeserEmail;
            return this;
        }

        public BillInfoToPayDto.BillInfoToPayDtoBuilder localitySandName(String localitySandName) {
            this.localitySandName = localitySandName;
            return this;
        }

        public BillInfoToPayDto.BillInfoToPayDtoBuilder localityGetName(String localityGetName) {
            this.localityGetName = localityGetName;
            return this;
        }

        public BillInfoToPayDto build() {
            return new BillInfoToPayDto(billId, price, deliveryId, weight, addreeserEmail, localitySandName, localityGetName);
        }

        public String toString() {
            return "BillInfoToPayDto.BillInfoToPayDtoBuilder(billId=" + this.billId + ", price=" + this.price + ", deliveryId=" + this.deliveryId + ", weight=" + this.weight + ", addreeserEmail=" + this.addreeserEmail + ", localitySandName=" + this.localitySandName + ", localityGetName=" + this.localityGetName + ")";
        }
    }
}
