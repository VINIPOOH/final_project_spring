package ua.testing.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.testing.authorization.dto.DeliveryCostAndTimeDto;
import ua.testing.authorization.dto.DeliveryInfoRequestDto;
import ua.testing.authorization.entity.Delivery;
import ua.testing.authorization.entity.Locality;
import ua.testing.authorization.entity.Way;
import ua.testing.authorization.exception.AskedDataIsNotExist;
import ua.testing.authorization.exception.NoSuchWayException;
import ua.testing.authorization.exception.UnsupportableWeightFactorException;
import ua.testing.authorization.repository.DeliveryRepository;
import ua.testing.authorization.repository.LocalityRepository;
import ua.testing.authorization.repository.TariffWeightFactorRepository;
import ua.testing.authorization.repository.WayRepository;

import java.util.List;

@Service
public class DeliveryProcessService {
    private final LocalityRepository localityRepository;
    private final WayRepository wayRepository;
    private final TariffWeightFactorRepository tariffWeightFactorRepository;
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryProcessService(LocalityRepository localityRepository, WayRepository wayRepository, TariffWeightFactorRepository tariffWeightFactorRepository, DeliveryRepository deliveryRepository) {
        this.localityRepository = localityRepository;
        this.wayRepository = wayRepository;
        this.tariffWeightFactorRepository = tariffWeightFactorRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public DeliveryCostAndTimeDto getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto) throws NoSuchWayException, UnsupportableWeightFactorException {
        Way way = getWay(deliveryInfoRequestDto);
        int deliveryCost;
        try {
            deliveryCost = calculateDeliveryCost(deliveryInfoRequestDto.getDeliveryWeight(), way);
        } catch (AskedDataIsNotExist askedDataIsNotExist) {
            throw new UnsupportableWeightFactorException(deliveryInfoRequestDto);
        }
        return DeliveryCostAndTimeDto.builder()
                .costInCents(deliveryCost)
                .timeOnWayInHours(way.getTimeOnWayInHours())
                .build();
    }

    private int calculateDeliveryCost(int deliveryWeight, Way way) throws AskedDataIsNotExist {
        int overPayOnKilometerForWeight = way.getWayTariffs().stream()
                .filter(x -> x.getMinWeightRange() <= deliveryWeight
                        && x.getMaxWeightRange() > deliveryWeight)
                .findFirst().orElseThrow(() -> new AskedDataIsNotExist())
                .getOverPayOnKilometer();
        int totalKilometerPrice = overPayOnKilometerForWeight + way.getPriceForKilometerInCents();
        return totalKilometerPrice * way.getDistanceInKilometres();
    }

    private Way getWay(DeliveryInfoRequestDto deliveryInfoRequestDto) throws NoSuchWayException {
        return wayRepository.findByLocalitySand_IdAndLocalityGet_Id(deliveryInfoRequestDto.getLocalitySandID()
                , deliveryInfoRequestDto.getLocalityGetID())
                .orElseThrow(() -> new NoSuchWayException(deliveryInfoRequestDto));
    }

    public List<Locality> getLocalitis() {
        return localityRepository.findAll();
    }

    public List<Delivery> getNotTakenDeliversByUserId(long userId){
        return deliveryRepository.findAllByIsPackageReceivedFalseAndAddressee_Id(userId);
    }

    public void configmGetingDelivery(long deliveryId) throws AskedDataIsNotExist {
        Delivery deliveryToUpdate=deliveryRepository.findById(deliveryId).orElseThrow(()->new AskedDataIsNotExist());
        deliveryToUpdate.setIsPackageReceived(true);
        deliveryRepository.save(deliveryToUpdate);
    }
}
