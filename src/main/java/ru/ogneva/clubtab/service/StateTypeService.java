package ru.ogneva.clubtab.service;

import org.springframework.stereotype.Service;
import ru.ogneva.clubtab.domain.StateTypeEntity;
import ru.ogneva.clubtab.dto.StateTypeDTO;
import ru.ogneva.clubtab.repository.StateTypeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StateTypeService {
    final private StateTypeRepository stateTypeRepository;

    StateTypeService(StateTypeRepository stateTypeRepository) {
        this.stateTypeRepository = stateTypeRepository;
    }

    public List<StateTypeDTO> getAll() {
        return stateTypeRepository.findAll().stream()
                .map(StateTypeEntity::toDto)
                .collect(Collectors.toList());
    }

    public Optional<StateTypeDTO> get(Long id) {
        return stateTypeRepository.findById(id).map(StateTypeEntity::toDto);
    }


}
