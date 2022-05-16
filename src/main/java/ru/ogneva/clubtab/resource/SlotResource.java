package ru.ogneva.clubtab.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.ogneva.clubtab.common.ForbiddenAlertException;
import ru.ogneva.clubtab.dto.SlotDTO;
import ru.ogneva.clubtab.service.SlotService;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
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
        System.out.println("Getting all slots");
        return slotService.findAll();
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<SlotDTO> getOne(@PathVariable("id") Long id) {
        System.out.printf("Getting one slot by id ", id);
        if (Objects.isNull(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<SlotDTO> dto = slotService.findOne(id);
        if (dto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(dto.get());
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

    @GetMapping(value="/complete/{id}")
    public ResponseEntity<Void> complete(@PathVariable("id") Long id) throws ResponseStatusException {
        try {
            slotService.complete(id);
        } catch (ForbiddenAlertException fae) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (InstanceNotFoundException ie) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value="/cancel/{id}")
    public ResponseEntity<Void> cancel(@PathVariable("id") Long id) throws ResponseStatusException {
        try {
            slotService.cancel(id);
        } catch (ForbiddenAlertException fae) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (InstanceNotFoundException ie) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().build();
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        try {
            slotService.delete(id);
        } catch ( NoSuchElementException | ForbiddenAlertException fe) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok().build();
    }
}
