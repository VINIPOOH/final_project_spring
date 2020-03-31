package ua.testing.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.testing.authorization.entity.Locality;
import ua.testing.authorization.entity.TariffWeightFactor;
import ua.testing.authorization.entity.Way;
import ua.testing.authorization.repository.WayRepository;

import java.util.LinkedList;

@Service
public class DataAdder {

    private final WayRepository wayRepository;

    @Autowired
    public DataAdder(WayRepository wayRepository) {
        this.wayRepository = wayRepository;
    }

    public void run() {
        Locality kiev = new Locality("Киев", "Kiev");
        Locality dnepropetrovsk = new Locality("Днепропетровск", "Dnepropetrovsk");

        LinkedList<TariffWeightFactor> tariffWeightFactor = new LinkedList<>();
        tariffWeightFactor.add
                (TariffWeightFactor.builder()
                        .minWeightRange(0)
                        .maxWeightRange(100)
                        .overPayOnKilometer(100)
                        .build());
        tariffWeightFactor.add
                (TariffWeightFactor.builder()
                        .minWeightRange(100)
                        .maxWeightRange(200)
                        .overPayOnKilometer(200)
                        .build());
        Way kievDnepropetrovsk = Way.builder()
                .distanceInKilometres(477)
                .localityGet(kiev)
                .localitySand(dnepropetrovsk)
                .priceForKilometerInCents(100)
                .timeOnWayInDays(8)
                .wayTariffs(tariffWeightFactor)
                .build();
        wayRepository.save(kievDnepropetrovsk);
//        Delivery delivery = Delivery.builder()
//                .addressee(userRepository.findByEmail("ivan_v123@ukr.net").get())
//                .addresser(userRepository.findByEmail("a@1").get())
//                .arrivalDate(LocalDate.now())
//                .isPackageReceived(false)
//                .way(wayRepository.findByLocalitySand_IdAndLocalityGet_Id(4, 3).get())
//                .build();
//        deliveryRepository.save(delivery);
    }
}
