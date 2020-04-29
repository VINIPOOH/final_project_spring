package ua.testing.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.testing.authorization.dto.DeliveryCostAndTimeDto;
import ua.testing.authorization.dto.DeliveryInfoRequestDto;
import ua.testing.authorization.dto.DeliveryOrderCreateDto;
import ua.testing.authorization.entity.Delivery;
import ua.testing.authorization.entity.User;
import ua.testing.authorization.entity.Way;
import ua.testing.authorization.exception.*;
import ua.testing.authorization.repository.DeliveryRepository;
import ua.testing.authorization.repository.UserRepository;
import ua.testing.authorization.repository.WayRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class DeliveryProcessService {
    private final UserRepository userRepository;
    private final WayRepository wayRepository;
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryProcessService(UserRepository userRepository, WayRepository wayRepository, DeliveryRepository deliveryRepository) {
        this.userRepository = userRepository;
        this.wayRepository = wayRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public Page<Delivery> findDeliveryHistoryByUserId(long userId, Pageable pageable) {
        return deliveryRepository.findAllByAddressee_IdOrAddresser_Id(userId, userId, pageable);
    }

    public List<Delivery> getPricesAndNotTakenDeliversByUserId(long userId) {
        return deliveryRepository.findAllByIsPackageReceivedFalseAndIsDeliveryPaidTrueAndAddressee_Id(userId);
    }

    @Transactional
    public void confirmGettingDelivery(long deliveryId) throws AskedDataIsNotExist {
        Delivery deliveryToUpdate = deliveryRepository.findById(deliveryId).orElseThrow(AskedDataIsNotExist::new);
        deliveryToUpdate.setIsPackageReceived(true);
        deliveryRepository.save(deliveryToUpdate);
    }

    public List<Delivery> getNotPayedDeliversByUserId(long userId) {
        return deliveryRepository.findAllByIsDeliveryPaidFalseAndAddresser_Id(userId);
    }

    @Transactional
    public Delivery payForDelivery(long deliveryId, long userId) throws AskedDataIsNotExist, DeliveryAlreadyPaidException, NoSuchUserException, NotEnoughMoneyException {
        Delivery deliveryToUpdate = getDeliveryOrException(deliveryId);
        User user = getUserOrException(userId, deliveryToUpdate);
        return deliveryRepository.save(prepareDeliverySaveData(deliveryToUpdate, user));
    }

    private Delivery prepareDeliverySaveData(Delivery deliveryToUpdate, User user) {
        user.setUserMoneyInCents(user.getUserMoneyInCents() - deliveryToUpdate.getBill().getCostInCents());
        deliveryToUpdate.getBill().setIsDeliveryPaid(true);
        deliveryToUpdate.setAddresser(user);
        deliveryToUpdate.getBill().setDateOfPay(LocalDate.now().plusDays(deliveryToUpdate.getWay().getTimeOnWayInDays()));
        return deliveryToUpdate;
    }

    //lamda
    private User getUserOrException(long userId, Delivery deliveryToUpdate) throws NoSuchUserException, NotEnoughMoneyException {
        User user = userRepository.findById(userId).orElseThrow(NoSuchUserException::new);
        if (user.getUserMoneyInCents() < deliveryToUpdate.getBill().getCostInCents()) {
            throw new NotEnoughMoneyException();
        }
        return user;
    }

    private Delivery getDeliveryOrException(long deliveryId) throws AskedDataIsNotExist, DeliveryAlreadyPaidException {
        Delivery deliveryToUpdate = deliveryRepository.findById(deliveryId).orElseThrow(AskedDataIsNotExist::new);
        if (deliveryToUpdate.getBill().getIsDeliveryPaid()) {
            throw new DeliveryAlreadyPaidException();
        }
        return deliveryToUpdate;
    }

    @Transactional
    public void createDeliveryOrder(DeliveryOrderCreateDto deliveryOrderCreateDto) throws NoSuchUserException, NoSuchWayException, UnsupportableWeightFactorException {
        deliveryRepository.save(buildDelivery(deliveryOrderCreateDto,
                getWay(deliveryOrderCreateDto.getLocalitySandID(), deliveryOrderCreateDto.getLocalityGetID())));
    }

    public DeliveryCostAndTimeDto getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto)
            throws NoSuchWayException, UnsupportableWeightFactorException {
        Way way = getWay(deliveryInfoRequestDto.getLocalitySandID(), deliveryInfoRequestDto.getLocalityGetID());
        return DeliveryCostAndTimeDto.builder()
                .costInCents(calculateDeliveryCost(deliveryInfoRequestDto.getDeliveryWeight(), way))
                .timeOnWayInHours(way.getTimeOnWayInDays())
                .build();
    }

    //разобраться с етой транзакцией (как вариант дабл чек)
    private Delivery buildDelivery(DeliveryOrderCreateDto deliveryOrderCreateDto, Way way) throws NoSuchUserException, UnsupportableWeightFactorException {
        return Delivery.builder()
                .addressee(userRepository.findByEmail(deliveryOrderCreateDto.getAddresseeEmail()).orElseThrow(NoSuchUserException::new))
                .addresser(userRepository.findByEmail(deliveryOrderCreateDto.getAddresserEmail()).orElseThrow(NoSuchUserException::new))
                .way(way)
                .isPackageReceived(false)
                .weight(deliveryOrderCreateDto.getDeliveryWeight())
//                .isDeliveryPaid(false)
//                .costInCents(calculateDeliveryCost(deliveryOrderCreateDto.getDeliveryWeight(), way))
                .build();
    }

    private Way getWay(long localitySandId, long localityGetId) throws NoSuchWayException {
        return wayRepository.findByLocalitySand_IdAndLocalityGet_Id(localitySandId
                , localityGetId)
                .orElseThrow(NoSuchWayException::new);
    }

    private int calculateDeliveryCost(int deliveryWeight, Way way) throws UnsupportableWeightFactorException {
        int overPayOnKilometerForWeight = way.getWayTariffs().stream()
                .filter(x -> x.getMinWeightRange() <= deliveryWeight
                        && x.getMaxWeightRange() > deliveryWeight)
                .findFirst().orElseThrow(UnsupportableWeightFactorException::new)
                .getOverPayOnKilometer();
        return (overPayOnKilometerForWeight + way.getPriceForKilometerInCents()) * way.getDistanceInKilometres();
    }

}
