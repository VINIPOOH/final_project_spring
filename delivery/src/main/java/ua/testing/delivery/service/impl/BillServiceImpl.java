package ua.testing.delivery.service.impl;

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
import ua.testing.delivery.service.BillService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Service
public class BillServiceImpl implements BillService {
    private static final Logger log = LogManager.getLogger(BillServiceImpl.class);

    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final DeliveryRepository deliveryRepository;
    private final WayRepository wayRepository;

    @Autowired
    public BillServiceImpl(BillRepository billRepository, UserRepository userRepository, DeliveryRepository deliveryRepository, WayRepository wayRepository) {
        log.debug("created");

        this.billRepository = billRepository;
        this.userRepository = userRepository;
        this.deliveryRepository = deliveryRepository;
        this.wayRepository = wayRepository;
    }

    @Override
    public List<BillInfoToPayDto> getBillsToPayByUserID(long userId, Locale locale) {
        log.debug("userId" + userId);

        return billRepository.findAllByUserIdAndIsDeliveryPaidFalse(userId).stream()
                .map(getMapperBillInfoToPayDto(locale)::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean payForDelivery(long userId, long billId) throws DeliveryAlreadyPaidException, NotEnoughMoneyException {
        log.debug("userId" + userId + "billId" + billId);

        Bill bill = billRepository.findByIdAndIsDeliveryPaidFalse(billId).orElseThrow(DeliveryAlreadyPaidException::new);
        User user = userRepository.findByIdAndUserMoneyInCentsGreaterThanEqual(userId, bill.getCostInCents()).orElseThrow(NotEnoughMoneyException::new);
        user.setUserMoneyInCents(user.getUserMoneyInCents() - bill.getCostInCents());
        bill.setDeliveryPaid(true);
        bill.setDateOfPay(LocalDate.now());
        userRepository.save(user);
        billRepository.save(bill);
        return true;
    }

    @Override
    @Transactional
    public Bill initializeBill(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, NoSuchUserException, NoSuchWayException {

        log.debug("deliveryOrderCreateDto" + deliveryOrderCreateDto);

        User addressee = userRepository.findByEmail(deliveryOrderCreateDto.getAddresseeEmail()).orElseThrow(NoSuchUserException::new);
        Way way = wayRepository.findByLocalitySand_IdAndLocalityGet_Id(deliveryOrderCreateDto.getLocalitySandID()
                , deliveryOrderCreateDto.getLocalityGetID()).orElseThrow(NoSuchWayException::new);
        Delivery newDelivery = deliveryRepository.save(getBuildDelivery(deliveryOrderCreateDto, addressee, way));
        return billRepository.save(
                getBuildBill(newDelivery
                        , calculateDeliveryCost(deliveryOrderCreateDto.getDeliveryWeight(), way)
                        , userRepository.findById(initiatorId).orElseThrow(DBWorkIncorrectException::new)));
    }


    @Override
    public Page<BillDto> getBillHistoryByUserId(long userId, Pageable pageable) {
        log.debug("userId" + userId);


        return billRepository.findAllByUserIdAndIsDeliveryPaidTrue(userId, pageable).map(getBillBillDtoMapper()::map);
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

    private Bill getBuildBill(Delivery newDelivery, long cost, User sender) {
        return Bill.builder()
                .delivery(newDelivery)
                .user(sender)
                .costInCents(cost)
                .build();
    }

    private Delivery getBuildDelivery(DeliveryOrderCreateDto deliveryOrderCreateDto, User addressee, Way way) {
        return Delivery.builder()
                .addressee(addressee)
                .way(way)
                .weight(deliveryOrderCreateDto.getDeliveryWeight())
                .build();
    }

    private int calculateDeliveryCost(int deliveryWeight, Way way) throws UnsupportableWeightFactorException {
        int overPayOnKilometerForWeight = way.getWayTariffs().stream()
                .filter(x -> x.getMinWeightRange() <= deliveryWeight
                        && x.getMaxWeightRange() > deliveryWeight)
                .findFirst().orElseThrow(UnsupportableWeightFactorException::new)
                .getOverPayOnKilometer();
        return (overPayOnKilometerForWeight + way.getPriceForKilometerInCents()) * way.getDistanceInKilometres();
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
