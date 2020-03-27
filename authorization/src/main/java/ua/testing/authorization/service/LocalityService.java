package ua.testing.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.testing.authorization.entity.Locality;
import ua.testing.authorization.repository.LocalityRepository;

import java.util.List;

@Service
public class LocalityService {
    private final LocalityRepository localityRepository;

    @Autowired
    public LocalityService(LocalityRepository localityRepository) {
        this.localityRepository = localityRepository;
    }

    public List<Locality> getLocalities() {
        return localityRepository.findAll();
    }
}
