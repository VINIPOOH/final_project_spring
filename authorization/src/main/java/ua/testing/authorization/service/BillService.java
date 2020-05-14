package ua.testing.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.testing.authorization.dto.BillInfoToPayDto;
import ua.testing.authorization.dto.DeliveryOrderCreateDto;
import ua.testing.authorization.dto.mapper.Mapper;
import ua.testing.authorization.entity.Bill;
import ua.testing.authorization.entity.Delivery;
import ua.testing.authorization.entity.User;
import ua.testing.authorization.entity.Way;
import ua.testing.authorization.exception.DeliveryAlreadyPaidException;
import ua.testing.authorization.exception.NotEnoughMoneyException;
import ua.testing.authorization.exception.UnsupportableWeightFactorException;
import ua.testing.authorization.repository.BillRepository;
import ua.testing.authorization.repository.DeliveryRepository;
import ua.testing.authorization.repository.UserRepository;
import ua.testing.authorization.repository.WayRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final WayRepository wayRepository;

    @Autowired
    public BillService(BillRepository billRepository, UserRepository userRepository, DeliveryRepository deliveryRepository, WayRepository wayRepository) {
        this.billRepository = billRepository;
        this.userRepository = userRepository;
        this.deliveryRepository = deliveryRepository;
        this.wayRepository = wayRepository;
    }

    public List<BillInfoToPayDto> getBillsToPayByUserID(long userId, Locale locale) {
        return billRepository.findAllByUserIdAndIsDeliveryPaidFalse(userId).stream()
                .map(getMapperBillInfoToPayDto(locale)::map)
                .collect(Collectors.toList());
    }

    private Mapper<Bill, BillInfoToPayDto> getMapperBillInfoToPayDto(Locale locale) {
        return bill -> {
            BillInfoToPayDto billInfoToPayDto = BillInfoToPayDto.builder()
                    .weight(bill.getDelivery().getWeight())
                    .price(bill.getCostInCents())
                    .deliveryId(bill.getDelivery().getId())
                    .billId(bill.getId())
                    .addreeserEmail(bill.getDelivery().getAddressee().getEmail())
                    .build();
            if (locale.getLanguage().equals("ru")) {
                billInfoToPayDto.setLocalitySandName(bill.getDelivery().getWay().getLocalitySand().getNameRu());
                billInfoToPayDto.setLocalityGetName(bill.getDelivery().getWay().getLocalityGet().getNameRu());
            } else {
                billInfoToPayDto.setLocalitySandName(bill.getDelivery().getWay().getLocalitySand().getNameEn());
                billInfoToPayDto.setLocalityGetName(bill.getDelivery().getWay().getLocalityGet().getNameEn());
            }
            return billInfoToPayDto;
        };
    }

    @Transactional
    public boolean payForDelivery(long userId, long billId) throws DeliveryAlreadyPaidException, NotEnoughMoneyException {

        Bill bill = billRepository.findByIdAndIsDeliveryPaidFalse(billId).orElseThrow(DeliveryAlreadyPaidException::new);
        User user = userRepository.findByIdAndUserMoneyInCentsGreaterThanEqual(userId, bill.getCostInCents()).orElseThrow(NotEnoughMoneyException::new);
        user.setUserMoneyInCents(user.getUserMoneyInCents() - bill.getCostInCents());
        bill.setDeliveryPaid(true);
        userRepository.save(user);
        billRepository.save(bill);
        return true;
    }

    //
    @Transactional
    public void initializeBill(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException {

        User addressee = userRepository.findByEmail(deliveryOrderCreateDto.getAddresseeEmail()).get();
        Way way = wayRepository.findByLocalitySand_IdAndLocalityGet_Id(deliveryOrderCreateDto.getLocalitySandID(), deliveryOrderCreateDto.getLocalityGetID()).get();
        Delivery newDelivery = deliveryRepository.save(Delivery.builder()
                .addressee(addressee)
                .way(way)
                .weight(deliveryOrderCreateDto.getDeliveryWeight())
                .build());
        long cost = calculateDeliveryCost(deliveryOrderCreateDto.getDeliveryWeight(), way);
        User sender = userRepository.findById(initiatorId).get();
        Bill bill = Bill.builder()
                .delivery(newDelivery)
                .user(sender)
                .costInCents(cost)
                .dateOfPay(LocalDate.now())
                .build();
        billRepository.save(bill);
    }

    private int calculateDeliveryCost(int deliveryWeight, Way way) throws UnsupportableWeightFactorException {
        int overPayOnKilometerForWeight = way.getWayTariffs().stream()
                .filter(x -> x.getMinWeightRange() <= deliveryWeight
                        && x.getMaxWeightRange() > deliveryWeight)
                .findFirst().orElseThrow(UnsupportableWeightFactorException::new)
                .getOverPayOnKilometer();
        return (overPayOnKilometerForWeight + way.getPriceForKilometerInCents()) * way.getDistanceInKilometres();
    }

    public Page<Bill> getBillHistoryByUserId(long userId, Pageable pageable) {
        return billRepository.findAllByUserIdAndIsDeliveryPaidTrue(userId, pageable);
    }
}
