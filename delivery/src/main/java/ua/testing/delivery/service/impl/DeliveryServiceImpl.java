package ua.testing.delivery.service.impl;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.testing.delivery.dto.DeliveryInfoRequestDto;
import ua.testing.delivery.dto.DeliveryInfoToGetDto;
import ua.testing.delivery.dto.PriceAndTimeOnDeliveryDto;
import ua.testing.delivery.dto.mapper.Mapper;
import ua.testing.delivery.entity.Delivery;
import ua.testing.delivery.entity.Way;
import ua.testing.delivery.exception.AskedDataIsNotExist;
import ua.testing.delivery.exception.NoSuchWayException;
import ua.testing.delivery.exception.UnsupportableWeightFactorException;
import ua.testing.delivery.repository.DeliveryRepository;
import ua.testing.delivery.repository.WayRepository;
import ua.testing.delivery.service.DeliveryService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Service
public class DeliveryServiceImpl implements DeliveryService {
    private static final Logger log = LogManager.getLogger(DeliveryServiceImpl.class);

    private final WayRepository wayRepository;
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryServiceImpl(WayRepository wayRepository, DeliveryRepository deliveryRepository) {
        log.debug("created");

        this.wayRepository = wayRepository;
        this.deliveryRepository = deliveryRepository;
    }

@Override
    public List<DeliveryInfoToGetDto> getDeliveryInfoToGet(long userId, Locale locale) {
        log.debug("userId" + userId);

        return deliveryRepository.findAllByAddressee_IdAndIsPackageReceivedFalseAndBill_IsDeliveryPaidTrue(userId).stream()
                .map(getDeliveryInfoToGetDtoMapper(locale)::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean confirmGettingDelivery(long userId, long deliveryId) throws AskedDataIsNotExist {
        log.debug("userId" + userId + "deliveryId" + deliveryId);

        Delivery delivery = deliveryRepository.findByIdAndAddressee_IdAndIsPackageReceivedFalse(deliveryId, userId)
                .orElseThrow(AskedDataIsNotExist::new);
        delivery.setPackageReceived(true);
        deliveryRepository.save(delivery);
        return true;
    }

@Override
    public PriceAndTimeOnDeliveryDto getDeliveryCostAndTimeDto(DeliveryInfoRequestDto deliveryInfoRequestDto)
            throws NoSuchWayException, UnsupportableWeightFactorException {
        log.debug("deliveryInfoRequestDto" + deliveryInfoRequestDto);


        Way way = getWay(deliveryInfoRequestDto.getLocalitySandID(), deliveryInfoRequestDto.getLocalityGetID());
        return PriceAndTimeOnDeliveryDto.builder()
                .costInCents(calculateDeliveryCost(deliveryInfoRequestDto.getDeliveryWeight(), way))
                .timeOnWayInHours(way.getTimeOnWayInDays())
                .build();
    }

    private Mapper<Delivery, DeliveryInfoToGetDto> getDeliveryInfoToGetDtoMapper(Locale locale) {
        return delivery -> {
            DeliveryInfoToGetDto deliveryInfo = DeliveryInfoToGetDto.builder()
                    .addresserEmail(delivery.getBill().getUser().getEmail())
                    .deliveryId(delivery.getId())
                    .localitySandName(delivery.getWay().getLocalitySand().getNameEn())
                    .localityGetName(delivery.getWay().getLocalityGet().getNameEn())
                    .build();
            if (locale.getLanguage().equals("ru")) {
                deliveryInfo.setLocalitySandName(delivery.getWay().getLocalitySand().getNameRu());
                deliveryInfo.setLocalityGetName(delivery.getWay().getLocalityGet().getNameRu());
            } else {
                deliveryInfo.setLocalitySandName(delivery.getWay().getLocalitySand().getNameEn());
                deliveryInfo.setLocalityGetName(delivery.getWay().getLocalityGet().getNameEn());
            }
            return deliveryInfo;
        };
    }

    private Way getWay(long localitySandId, long localityGetId) throws NoSuchWayException {
        return wayRepository.findByLocalitySand_IdAndLocalityGet_Id(localitySandId
                , localityGetId).orElseThrow(NoSuchWayException::new);
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
