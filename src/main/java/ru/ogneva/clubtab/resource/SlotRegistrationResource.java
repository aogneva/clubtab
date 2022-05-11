package ru.ogneva.clubtab.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.ogneva.clubtab.dto.SlotRegistrationDTO;
import ru.ogneva.clubtab.service.SlotRegistrationService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value="/slot-reg")
public class SlotRegistrationResource {
    private final SlotRegistrationService slotRegistrationService;

    SlotRegistrationResource(final SlotRegistrationService slotRegistrationService) {
        this.slotRegistrationService = slotRegistrationService;
    }

    @PostMapping(value="/{slotId}/{personId}")
    public ResponseEntity<SlotRegistrationDTO> register(@PathVariable("slotId") Long slotId,
                                                     @PathVariable("personId") Long personId) {
        try {
            SlotRegistrationDTO reg = slotRegistrationService.create(slotId, personId);
            return ResponseEntity.status(HttpStatus.CREATED).body(reg);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Null parameters are not expected", e);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found slot or person", e);
        }
    }

    @GetMapping
    public ResponseEntity<List<SlotRegistrationDTO>> getAll() {
        return ResponseEntity.ok().body(slotRegistrationService.findAll());
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> unregister(@PathVariable("id") Long id) {
        slotRegistrationService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping(value="/slot/{slotId}")
    public ResponseEntity<Void> unregisterSlot(@PathVariable("slotId") Long slotId) {
        slotRegistrationService.deleteAllBySlot(slotId);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping(value="/person/{customerId}")
    public ResponseEntity<Void> unregisterCustomer(@PathVariable("customerId") Long customerId) {
        slotRegistrationService.deleteAllByCustomer(customerId);
        return ResponseEntity.ok().body(null);
    }

}
