package ua.testing.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.testing.authorization.entity.Delivery;
import ua.testing.authorization.repository.DeliveryRepository;
import ua.testing.authorization.repository.UserRepository;
import ua.testing.authorization.repository.WayRepository;

import java.time.LocalDate;

@Service
public class DataAder {

    private WayRepository wayRepository;

    private DeliveryRepository deliveryRepository;

    private UserRepository userRepository;

    @Autowired
    public DataAder(WayRepository wayRepository, DeliveryRepository deliveryRepository, UserRepository userRepository) {
        this.wayRepository = wayRepository;
        this.deliveryRepository = deliveryRepository;
        this.userRepository = userRepository;
    }

    public void run() {
//        Locality kive = new Locality("Киев", "Kiev");
//        Locality dnepropetrovsk = new Locality("Днепропетровск", "Dnepropetrovsk");
//
//        LinkedList<TariffWeightFactor> tariffWeightFactor = new LinkedList<>();
//        tariffWeightFactor.addFirst
//                (new TariffWeightFactor(0, 100, 10));
//        tariffWeightFactor.addFirst
//                (new TariffWeightFactor(100, 200, 20));
//
//        Way kievDnepropetrovsk = Way.builder()
//                .distanceInKilometres(477)
//                .localityGet(kive)
//                .localitySand(dnepropetrovsk)
//                .priceForKilometerInCents(100)
//                .timeOnWayInHours(8)
//                .wayTariffs(tariffWeightFactor)
//                .build();
//        wayRepository.save(kievDnepropetrovsk);
        Delivery delivery = Delivery.builder()
                .addressee(userRepository.findByEmail("ivan_v123@ukr.net").get())
                .addresser(userRepository.findByEmail("a@1").get())
                .arrivalDate(LocalDate.now())
                .isPackageReceived(false)
                .way(wayRepository.findByLocalitySand_IdAndLocalityGet_Id(4, 3).get())
                .build();
        deliveryRepository.save(delivery);
    }
}
