package ua.testing.delivery.service;

import org.springframework.stereotype.Service;
import ua.testing.delivery.dto.LocaliseLocalityDto;

import java.util.List;
import java.util.Locale;

@Service
public interface LocalityService {

    List<LocaliseLocalityDto> getLocalities(Locale locale);

}
