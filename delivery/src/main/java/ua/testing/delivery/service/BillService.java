package ua.testing.delivery.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.testing.delivery.dto.BillDto;
import ua.testing.delivery.dto.BillInfoToPayDto;
import ua.testing.delivery.dto.DeliveryOrderCreateDto;
import ua.testing.delivery.dto.mapper.Mapper;
import ua.testing.delivery.entity.Bill;
import ua.testing.delivery.entity.Delivery;
import ua.testing.delivery.entity.User;
import ua.testing.delivery.entity.Way;
import ua.testing.delivery.exception.*;
import ua.testing.delivery.repository.BillRepository;
import ua.testing.delivery.repository.DeliveryRepository;
import ua.testing.delivery.repository.UserRepository;
import ua.testing.delivery.repository.WayRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class BillService {
    private static Logger log = LogManager.getLogger(BillService.class);


    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final WayRepository wayRepository;

    @Autowired
    public BillService(BillRepository billRepository, UserRepository userRepository, DeliveryRepository deliveryRepository, WayRepository wayRepository) {
        log.debug("created");

        this.billRepository = billRepository;
        this.userRepository = userRepository;
        this.deliveryRepository = deliveryRepository;
        this.wayRepository = wayRepository;
    }

    public List<BillInfoToPayDto> getBillsToPayByUserID(long userId, Locale locale) {
        log.debug("userId" + userId);


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
                    .addreeseeEmail(bill.getDelivery().getAddressee().getEmail())
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
        log.debug("userId" + userId + "billId" + billId);


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
    public Bill initializeBill(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, NoSuchUserException, NoSuchWayException {

        log.debug("deliveryOrderCreateDto" + deliveryOrderCreateDto);

        User addressee = userRepository.findByEmail(deliveryOrderCreateDto.getAddresseeEmail()).orElseThrow(NoSuchUserException::new);
        Way way = wayRepository.findByLocalitySand_IdAndLocalityGet_Id(deliveryOrderCreateDto.getLocalitySandID(), deliveryOrderCreateDto.getLocalityGetID())
                .orElseThrow(NoSuchWayException::new);
        Delivery newDelivery = deliveryRepository.save(Delivery.builder()
                .addressee(addressee)
                .way(way)
                .weight(deliveryOrderCreateDto.getDeliveryWeight())
                .build());
        long cost = calculateDeliveryCost(deliveryOrderCreateDto.getDeliveryWeight(), way);
        User sender = userRepository.findById(initiatorId).orElseThrow(DBWorkIncorrectException::new);
        Bill bill = Bill.builder()
                .delivery(newDelivery)
                .user(sender)
                .costInCents(cost)
                .build();
        return billRepository.save(bill);
    }

    private int calculateDeliveryCost(int deliveryWeight, Way way) throws UnsupportableWeightFactorException {
        int overPayOnKilometerForWeight = way.getWayTariffs().stream()
                .filter(x -> x.getMinWeightRange() <= deliveryWeight
                        && x.getMaxWeightRange() > deliveryWeight)
                .findFirst().orElseThrow(UnsupportableWeightFactorException::new)
                .getOverPayOnKilometer();
        return (overPayOnKilometerForWeight + way.getPriceForKilometerInCents()) * way.getDistanceInKilometres();
    }

    public Page<BillDto> getBillHistoryByUserId(long userId, Pageable pageable) {
        log.debug("userId" + userId);


        return billRepository.findAllByUserIdAndIsDeliveryPaidTrue(userId, pageable).map(getBillBillDtoMapper()::map);
    }

    private Mapper<Bill, BillDto> getBillBillDtoMapper() {
        return bill -> BillDto.builder()
                .id(bill.getId())
                .deliveryId(bill.getDelivery().getId())
                .isDeliveryPaid(bill.isDeliveryPaid())
                .costInCents(bill.getCostInCents())
                .dateOfPay(bill.getDateOfPay())
                .build();
    }
}
