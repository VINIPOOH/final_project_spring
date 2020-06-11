package ua.testing.delivery;

import ua.testing.delivery.dto.BillDto;
import ua.testing.delivery.dto.BillInfoToPayDto;
import ua.testing.delivery.dto.DeliveryInfoToGetDto;
import ua.testing.delivery.dto.DeliveryOrderCreateDto;
import ua.testing.delivery.entity.*;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ServisesTestConstant {

    private final static long USER_ID = 1L;
    private static final long BILL_ID = 1L;


    private static final long DELIVERY_ID = 1L;
    private static final Locale LOCALE_EN = new Locale("en");
    private static final Locale LOCALE_RU = new Locale("ru");

    public static long getUserId() {
        return USER_ID;
    }

    public static long getBillId() {
        return BILL_ID;
    }

    public static Locale getLocaleEn() {
        return LOCALE_EN;
    }

    public static Locale getLocaleRu() {
        return LOCALE_RU;
    }

    public static long getDeliveryId() {
        return DELIVERY_ID;
    }

    public static List<TariffWeightFactor> getTariffWeightFactors() {
        return Collections.singletonList(getTariffWeightFactor());
    }

    public static List<Delivery> getDeliveres() {
        return Collections.singletonList(getDelivery());
    }

    public static DeliveryInfoToGetDto getDeliveryInfoToGetDto() {
        return DeliveryInfoToGetDto.builder()
                .deliveryId(1L)
                .addresserEmail(getAddreser().getEmail())
                .build();
    }

    public static List<BillDto> getBillDtos() {
        return Collections.singletonList(getBillDto());
    }

    public static List<Bill> getBills() {
        return Collections.singletonList(getBill());
    }

    public static User getAdversee() {
        return User.builder()
                .id(USER_ID)
                .email("emailAdresee")
                .userMoneyInCents(0L)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .password("password")
                .roleType(RoleType.ROLE_USER)
                .build();
    }

    public static List<User> getUsers() {
        return Collections.singletonList(getAddreser());
    }

    public static User getAddreser() {
        return User.builder()
                .id(USER_ID)
                .email("emailAdreser")
                .userMoneyInCents(300L)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .password("password")
                .roleType(RoleType.ROLE_USER)
                .build();
    }

    public static BillInfoToPayDto getBillInfoToPayDto() {
        Bill bill = getBill();
        return BillInfoToPayDto.builder()
                .billId(bill.getId())
                .deliveryId(bill.getDelivery().getId())
                .price(bill.getCostInCents())
                .weight(bill.getDelivery().getWeight())
                .addreeseeEmail(bill.getDelivery().getAddressee().getEmail())
                .build();
    }

    public static TariffWeightFactor getTariffWeightFactor() {
        return TariffWeightFactor.builder()
                .id(1)
                .minWeightRange(0)
                .maxWeightRange(100)
                .overPayOnKilometer(1)
                .build();
    }

    public static Locality getLocalitySend() {
        return Locality.builder().nameEn("EnSend").nameRu("RuSend").id(1).build();
    }

    public static Locality getLocalityGet() {
        return Locality.builder().nameEn("EnGet").nameRu("RuGet").id(2).build();
    }

    public static List<Locality> getLocalities() {
        return Collections.singletonList(getLocalityGet());
    }

    public static Way getWay() {
        return Way.builder()
                .id(1)
                .localityGet(getLocalityGet())
                .localitySand(getLocalitySend())
                .distanceInKilometres(1)
                .wayTariffs(getTariffWeightFactors())
                .priceForKilometerInCents(1)
                .timeOnWayInDays(1)
                .build();
    }

    public static Delivery getDelivery() {
        return Delivery.builder()
                .weight(1)
                .id(1L)
                .addressee(User.builder().email("email").build())
                .way(getWay())
                .isPackageReceived(false)
                .build();
    }

    public static Bill getBill() {
        return Bill.builder()
                .id(1L)
                .costInCents(1)
                .isDeliveryPaid(true)
                .delivery(getDelivery())
                .user(getAddreser())
                .build();
    }

    public static BillDto getBillDto() {
        return BillDto.builder()
                .costInCents(1)
                .deliveryId(1)
                .isDeliveryPaid(true)
                .id(1)
                .build();
    }

    public static DeliveryOrderCreateDto getDeliveryOrderCreateDto() {
        return DeliveryOrderCreateDto
                .builder()
                .addresseeEmail("email")
                .deliveryWeight(1)
                .localityGetID(1)
                .localitySandID(2)
                .build();
    }
}
