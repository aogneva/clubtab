package ru.ogneva.clubtab.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ogneva.clubtab.dto.PersonDTO;
import ru.ogneva.clubtab.service.PersonService;

import javax.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value="/person")
public class PersonResource {

    final private PersonService personService;

    PersonResource(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<PersonDTO> getAll() {
        return personService.findAll();
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<PersonDTO> getOne(@PathVariable("id") Long id) throws EntityNotFoundException {
        Optional<PersonDTO> personDTO = personService.findOne(id);
        if (personDTO.isEmpty()) {
            throw new EntityNotFoundException("id: "+id);
        }
        return ResponseEntity.ok().body(personDTO.get());
    }

    @PostMapping
    public ResponseEntity<PersonDTO> create(@RequestBody PersonDTO personDTO) {
        PersonDTO p = personService.save(personDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<PersonDTO> update(
            @PathVariable("id") Long id,
            @RequestBody PersonDTO personDTO
    ) throws EntityNotFoundException {
        Optional<PersonDTO> p = personService.findOne(id);
        if (p.isEmpty()) {
            throw new EntityNotFoundException("id: "+id);
        }
        Date date;
        if (!Objects.equals(personDTO.getId(), id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok().body(personService.save(personDTO));
    }

}
