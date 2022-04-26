package ru.ogneva.clubtab.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ogneva.clubtab.dto.PersonDTO;
import ru.ogneva.clubtab.service.PersonService;

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
}
