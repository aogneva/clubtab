package ru.ogneva.clubtab.resource;

import org.springframework.web.bind.annotation.*;
import ru.ogneva.clubtab.dto.PersonDTO;
import ru.ogneva.clubtab.service.PersonService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value="/person")
public class PersonResource {

    final private PersonService personService;

    PersonResource(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping(value="/all")
    public List<PersonDTO> getALl() {
        return personService.getAll();
    }

    @GetMapping(value="/{id}")
    public PersonDTO getOne(@PathVariable("id") Long id) {
        return personService.getOne(id).orElse(null);
    }

    @PostMapping(value="/new")
    public PersonDTO create(
            @RequestParam("firstName") String firstName,
            @RequestParam("secondName") String secondName,
            @RequestParam("lastName") String lastName,
            @RequestParam("dob") String dob
    ) {
        System.out.println("Create Person");
        try {
            Date date = (new SimpleDateFormat("dd.MM.yyyy")).parse(dob);
            PersonDTO personDTO = personService.save(null, firstName, secondName, lastName, date);
            System.out.println("New Person: " + personDTO.toString());
            return personDTO;
        } catch (ParseException e) {
            System.out.printf("Create Person Error: %s%n", e.getMessage());
        }
        return null;
    }

    @PutMapping(value="/update")
    public PersonDTO create(
            @RequestParam("id") Long id,
            @RequestParam("firstName") String firstName,
            @RequestParam("secondName") String secondName,
            @RequestParam("lastName") String lastName,
            @RequestParam("dob") String dob
    ) {
        try {
            Date date = (new SimpleDateFormat("dd.MM.yyyy")).parse(dob);
            return personService.save(id, firstName, secondName, lastName, date);
        } catch (ParseException e) {
            return null;
        }
    }

}
