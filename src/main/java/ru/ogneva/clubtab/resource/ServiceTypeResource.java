package ru.ogneva.clubtab.resource;

import org.springframework.web.bind.annotation.*;
import ru.ogneva.clubtab.dto.ServiceTypeDTO;
import ru.ogneva.clubtab.service.ServiceTypeService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/service-type")
public class ServiceTypeResource {
    final private ServiceTypeService serviceTypeService;

    ServiceTypeResource(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @GetMapping(value="/all")
    public List<ServiceTypeDTO> getAll() {
        return serviceTypeService.getAll();
    }

    @GetMapping(value="/{id}")
    public ServiceTypeDTO get(@PathVariable("id") Long id) {
        return serviceTypeService.get(id).orElse(null);
    }

}
