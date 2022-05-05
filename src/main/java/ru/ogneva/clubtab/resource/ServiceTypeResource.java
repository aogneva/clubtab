package ru.ogneva.clubtab.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ServiceTypeDTO> get(@PathVariable("id") Long id) {
        Optional<ServiceTypeDTO> dto = serviceTypeService.get(id);
        if(dto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(dto.get());
    }

}
