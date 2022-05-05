package ru.ogneva.clubtab.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ogneva.clubtab.dto.SlotDTO;
import ru.ogneva.clubtab.service.SlotService;

import javax.management.InstanceNotFoundException;
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

    @PostMapping
    public ResponseEntity<SlotDTO> create(@RequestBody SlotDTO slotDTO) {
        if (Objects.nonNull(slotDTO.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        SlotDTO slot = slotService.create(slotDTO);
        if (Objects.isNull(slot)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(slot);
    }

    @PutMapping
    public ResponseEntity<SlotDTO> update(@RequestBody SlotDTO slotDTO) {
        if (Objects.isNull(slotDTO.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        SlotDTO slot;
        try {
            slot = slotService.update(slotDTO);
        } catch (InstanceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(slot);
    }

}
