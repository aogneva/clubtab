package ru.ogneva.clubtab.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ogneva.clubtab.dto.SlotDTO;
import ru.ogneva.clubtab.service.SlotService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "/slot")
public class SlotResource {

    final public SlotService slotService;

    SlotResource(SlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping
    public List<SlotDTO> getAll() {
        return slotService.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<SlotDTO> getOne(@PathVariable("id") Long id) {
        if (Objects.isNull(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<SlotDTO> dto = slotService.findOne(id);
        return dto.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() :
                ResponseEntity.status(HttpStatus.OK)
                .body(dto.get());
    }
}
