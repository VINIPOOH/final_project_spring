package ua.testing.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.testing.authorization.dto.DeliveryCostAndTimeDto;
import ua.testing.authorization.dto.DeliveryInfoRequestDto;
import ua.testing.authorization.dto.DeliveryOrderCreateDto;
import ua.testing.authorization.entity.Delivery;
import ua.testing.authorization.entity.Locality;
import ua.testing.authorization.entity.Way;
import ua.testing.authorization.exception.AskedDataIsNotExist;
import ua.testing.authorization.exception.NoSuchUserException;
import ua.testing.authorization.exception.NoSuchWayException;
import ua.testing.authorization.exception.UnsupportableWeightFactorException;
import ua.testing.authorization.repository.*;

import java.util.List;

@Service
public class DeliveryProcessService {
    private final UserRepository userRepository;
    private final LocalityRepository localityRepository;
    private final WayRepository wayRepository;
    private final TariffWeightFactorRepository tariffWeightFactorRepository;
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryProcessService(UserRepository userRepository, LocalityRepository localityRepository, WayRepository wayRepository, TariffWeightFactorRepository tariffWeightFactorRepository, DeliveryRepository deliveryRepository) {
        this.userRepository = userRepository;
        this.localityRepository = localityRepository;
        this.wayRepository = wayRepository;
        this.tariffWeightFactorRepository = tariffWeightFactorRepository;
        this.deliveryRepository = deliveryRepository;
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
                .timeOnWayInHours(way.getTimeOnWayInHours())
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

    public List<Locality> getLocalities() {
        return localityRepository.findAll();
    }

    public List<Delivery> getNotTakenDeliversByUserId(long userId) {
        return deliveryRepository.findAllByIsPackageReceivedFalseAndIsDeliveryPaidTrueAndAddressee_Id(userId);
    }

    public void confirmGettingDelivery(long deliveryId) throws AskedDataIsNotExist {
        Delivery deliveryToUpdate = deliveryRepository.findById(deliveryId).orElseThrow(() -> new AskedDataIsNotExist());
        deliveryToUpdate.setIsPackageReceived(true);
        deliveryRepository.save(deliveryToUpdate);
    }

    public void сreateDeliveryOrder(DeliveryOrderCreateDto deliveryOrderCreateDto) throws NoSuchUserException, NoSuchWayException {

        deliveryRepository.save(Delivery.builder()
                .addressee(userRepository.findByEmail(deliveryOrderCreateDto.getAddresseeEmail()).get())
                .addresser(userRepository.findByEmail(deliveryOrderCreateDto.getAddresserEmail()).orElseThrow(NoSuchUserException::new))
                .way(getWay(deliveryOrderCreateDto.getLocalitySandID(), deliveryOrderCreateDto.getLocalityGetID()))
                .isPackageReceived(false)
                .isDeliveryPaid(false)
                .build());
    }
}
