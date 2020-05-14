package ua.testing.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.testing.authorization.dto.DeliveryInfoRequestDto;
import ua.testing.authorization.dto.PriceAndTimeOnDeliveryDto;
import ua.testing.authorization.entity.Delivery;
import ua.testing.authorization.entity.Way;
import ua.testing.authorization.exception.AskedDataIsNotExist;
import ua.testing.authorization.exception.NoSuchWayException;
import ua.testing.authorization.exception.UnsupportableWeightFactorException;
import ua.testing.authorization.repository.DeliveryRepository;
import ua.testing.authorization.repository.UserRepository;
import ua.testing.authorization.repository.WayRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DeliveryService {
    private final UserRepository userRepository;
    private final WayRepository wayRepository;
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryService(UserRepository userRepository, WayRepository wayRepository, DeliveryRepository deliveryRepository) {
        this.userRepository = userRepository;
        this.wayRepository = wayRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public Page<Delivery> findDeliveryHistoryByUserId(long userId, Pageable pageable) {
//        return deliveryRepository.findAllByAddressee_IdOrAddresser_Id(userId, userId, pageable);
        return null;
    }

    public List<Delivery> getDeliveryInfoToGet(long userId) {
        return deliveryRepository.findAllByBill_User_IdAndIsPackageReceivedFalse(userId);
    }

    @Transactional
    public void confirmGettingDelivery(long userId, long deliveryId) throws AskedDataIsNotExist {
        Delivery delivery = deliveryRepository.findByIdAndBill_User_IdAndIsPackageReceivedFalse(deliveryId, userId).orElseThrow(AskedDataIsNotExist::new);
        delivery.setPackageReceived(true);
        deliveryRepository.save(delivery);
    }


    public PriceAndTimeOnDeliveryDto getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto)
            throws NoSuchWayException, UnsupportableWeightFactorException {
        Way way = getWay(deliveryInfoRequestDto.getLocalitySandID(), deliveryInfoRequestDto.getLocalityGetID());
        return PriceAndTimeOnDeliveryDto.builder()
                .costInCents(calculateDeliveryCost(deliveryInfoRequestDto.getDeliveryWeight(), way))
                .timeOnWayInHours(way.getTimeOnWayInDays())
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
