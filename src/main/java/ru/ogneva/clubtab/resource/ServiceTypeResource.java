package ru.ogneva.clubtab.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public Optional<ServiceTypeDTO> get(@PathVariable("id") Long id) {
        return serviceTypeService.get(id);
    }
}
