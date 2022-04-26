package ru.ogneva.clubtab.service;

import org.springframework.stereotype.Service;
import ru.ogneva.clubtab.domain.PersonEntity;
import ru.ogneva.clubtab.dto.PersonDTO;
import ru.ogneva.clubtab.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {
    final private PersonRepository personRepository;

    PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<PersonDTO> getAll() {
        return personRepository.findAll()
                .stream()
                .map(PersonEntity::toDto)
                .collect(Collectors.toList());
    }

    public Optional<PersonDTO> getOne(Long id) {
        return personRepository.findById(id)
                .map(PersonEntity::toDto);
    }

    public void deleteOne(Long id) {
        personRepository.deleteById(id);
    }

}
