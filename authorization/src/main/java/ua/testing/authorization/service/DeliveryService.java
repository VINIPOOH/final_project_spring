package ua.testing.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.testing.authorization.dto.DeliveryCostAndTimeDto;
import ua.testing.authorization.dto.DeliveryInfoRequestDto;
import ua.testing.authorization.entity.Way;
import ua.testing.authorization.exception.AskedDataIsNotExist;
import ua.testing.authorization.exception.NoSuchWayException;
import ua.testing.authorization.exception.UnsupportableWeightFactorException;
import ua.testing.authorization.repository.WayRepository;

@Service
public class DeliveryService {

    private final WayRepository wayRepository;

    @Autowired
    public DeliveryService(WayRepository wayRepository) {
        this.wayRepository = wayRepository;
    }

    public DeliveryCostAndTimeDto getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto)
            throws NoSuchWayException, UnsupportableWeightFactorException {
        Way way = getWay(deliveryInfoRequestDto);
        int deliveryCost;
        try {
            deliveryCost = calculateDeliveryCost(deliveryInfoRequestDto.getDeliveryWeight(), way);
        } catch (AskedDataIsNotExist askedDataIsNotExist) {
            System.out.println(askedDataIsNotExist.toString());
            throw new UnsupportableWeightFactorException(deliveryInfoRequestDto);
        }
        return DeliveryCostAndTimeDto.builder()
                .costInCents(deliveryCost)
                .timeOnWayInHours(way.getTimeOnWayInDays())
                .build();
    }

    private int calculateDeliveryCost(int deliveryWeight, Way way) throws AskedDataIsNotExist {
        int overPayOnKilometerForWeight = way.getWayTariffs().stream()
                .filter(x -> x.getMinWeightRange() <= deliveryWeight
                        && x.getMaxWeightRange() > deliveryWeight)
                .findFirst().orElseThrow(AskedDataIsNotExist::new)
                .getOverPayOnKilometer();
        int totalKilometerPrice = overPayOnKilometerForWeight + way.getPriceForKilometerInCents();
        return totalKilometerPrice * way.getDistanceInKilometres();
    }

    private Way getWay(DeliveryInfoRequestDto deliveryInfoRequestDto) throws NoSuchWayException {
        return getWay(deliveryInfoRequestDto.getLocalitySandID(), deliveryInfoRequestDto.getLocalityGetID());
    }

    private Way getWay(long localitySandId, long localityGetId) throws NoSuchWayException {
        return wayRepository.findByLocalitySand_IdAndLocalityGet_Id(localitySandId
                , localityGetId)
                .orElseThrow(NoSuchWayException::new);
    }

}
