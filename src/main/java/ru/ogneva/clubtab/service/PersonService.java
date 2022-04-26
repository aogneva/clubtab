package ru.ogneva.clubtab.service;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.ogneva.clubtab.dto.PersonDTO;
import ru.ogneva.clubtab.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {
    final private PersonRepository personRepository;

    final private ModelMapper mapper = new ModelMapper();

    PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonDTO> getAll() {
        return personRepository.findAll()
                .stream()
                .map(personEntity ->
                    mapper.map(personEntity, PersonDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<PersonDTO> getOne(Long id) {
        return personRepository.findById(id)
                .map(personEntity -> mapper.map(personEntity, PersonDTO.class));
    }

    public void deleteOne(Long id) {
        personRepository.deleteById(id);
    }

}
