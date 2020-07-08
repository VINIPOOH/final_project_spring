package ua.testing.delivery.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.testing.delivery.dto.LocaliseLocalityDto;
import ua.testing.delivery.service.LocalityService;

import java.util.List;
import java.util.Locale;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Controller
public class LocalitiesController {
    private static final Logger log = LogManager.getLogger(LocalitiesController.class);

    private final LocalityService localityService;

    @Autowired
    public LocalitiesController(LocalityService localityService) {
        this.localityService = localityService;
    }

    @ResponseBody
    @GetMapping(value = {"/get/localitiesGet/by/localitySend/id"})
    public List<LocaliseLocalityDto> error404(Locale locale, long id) {
        log.debug("");

        return localityService.findGetLocalitiesByLocalitySetId(locale, id);
    }

}
