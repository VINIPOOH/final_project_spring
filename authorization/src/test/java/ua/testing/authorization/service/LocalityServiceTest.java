package ua.testing.authorization.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ua.testing.authorization.dto.LocaliseLocalityDto;
import ua.testing.authorization.entity.Locality;
import ua.testing.authorization.repository.LocalityRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static ua.testing.authorization.service.ServisesTestConstant.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = LocalityService.class)
public class LocalityServiceTest {

    @Autowired
    LocalityService localityService;
    @MockBean
    LocalityRepository localityRepository;
    @Test
    public void getLocalitiesRu() {
        Locality locality = getLocalityGet();
        List<Locality> localities = Collections.singletonList(locality);
        LocaliseLocalityDto localiseLocalityDto = LocaliseLocalityDto.builder()
                .id(locality.getId())
                .name(locality.getNameRu())
                .build();
        when(localityRepository.findAll()).thenReturn(localities);

        List<LocaliseLocalityDto> result = localityService.getLocalities(getLocaleRu());

        verify(localityRepository, times(1)).findAll();
        assertEquals(localiseLocalityDto, result.get(0));
        assertEquals(localities.size(), result.size());
    }

    @Test
    public void getLocalitiesEn() {
        Locality locality = getLocalityGet();
        List<Locality> localities = Collections.singletonList(locality);
        LocaliseLocalityDto localiseLocalityDto = LocaliseLocalityDto.builder()
                .id(locality.getId())
                .name(locality.getNameEn())
                .build();
        when(localityRepository.findAll()).thenReturn(localities);

        List<LocaliseLocalityDto> result = localityService.getLocalities(getLocaleEn());

        verify(localityRepository, times(1)).findAll();
        assertEquals(localiseLocalityDto, result.get(0));
        assertEquals(localities.size(), result.size());
    }
}