package ua.testing.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import ua.testing.authorization.entity.Locality;
import ua.testing.authorization.entity.TariffWeightFactor;
import ua.testing.authorization.entity.Way;
import ua.testing.authorization.repository.WayRepository;

import java.util.LinkedList;

public class dataAder {
    private  WayRepository wayRepository;

    @Autowired
    public  void setWayRepository(WayRepository wayRepository) {
        this.wayRepository = wayRepository;
    }

    public void run() {

        Locality kive=new Locality("Киев","Kiev");
        Locality dnepropetrovsk=new Locality("Днепропетровск", "Dnepropetrovsk");

        LinkedList<TariffWeightFactor> tariffWeightFactor=new LinkedList<>();
        tariffWeightFactor.addFirst
                (new TariffWeightFactor(0,100,10));
        tariffWeightFactor.addFirst
                (new TariffWeightFactor(100,200,20));

        Way kievDnepropetrovsk = Way.builder()
                .distanceInKilometres(477)
                .localityGet(kive)
                .localitySand(dnepropetrovsk)
                .priceForKilometerInCents(100)
                .timeOnWayInHours(8)
                .wayTariffs(tariffWeightFactor)
                .build();
        wayRepository.save(kievDnepropetrovsk);

    }
}
