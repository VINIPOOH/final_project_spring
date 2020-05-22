package ua.testing.delivery.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.testing.delivery.dto.LocaliseLocalityDto;
import ua.testing.delivery.dto.mapper.Mapper;
import ua.testing.delivery.entity.Locality;
import ua.testing.delivery.repository.LocalityRepository;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class LocalityService {
    private static Logger log = LogManager.getLogger(LocalityService.class);

    private final LocalityRepository localityRepository;

    @Autowired
    public LocalityService(LocalityRepository localityRepository) {
        log.debug("created");

        this.localityRepository = localityRepository;
    }

    public List<LocaliseLocalityDto> getLocalities(Locale locale) {
        log.debug("");


        return localityRepository.findAll().stream()
                .map(getLocalityToLocaliseLocalityDto(locale)::map)
                .collect(Collectors.toList());
    }

    private Mapper<Locality, LocaliseLocalityDto> getLocalityToLocaliseLocalityDto(Locale locale) {
        return locality -> {
            LocaliseLocalityDto toReturn = LocaliseLocalityDto.builder()
                    .id(locality.getId())
                    .build();
            if (locale.getLanguage().equals("ru")) {
                toReturn.setName(locality.getNameRu());
            } else {
                toReturn.setName(locality.getNameEn());
            }
            return toReturn;
        };
    }
}