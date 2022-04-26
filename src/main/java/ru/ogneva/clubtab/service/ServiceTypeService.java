package ru.ogneva.clubtab.service;

import org.springframework.stereotype.Service;
import ru.ogneva.clubtab.domain.ServiceTypeEntity;
import ru.ogneva.clubtab.dto.ServiceTypeDTO;
import ru.ogneva.clubtab.repository.ServiceTypeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceTypeService {
    final private ServiceTypeRepository serviceTypeRepository;

    ServiceTypeService(ServiceTypeRepository serviceTypeRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
    }

    public List<ServiceTypeDTO> getAll() {
        return serviceTypeRepository.findAll().stream()
                .map(ServiceTypeEntity::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ServiceTypeDTO> get(Long id) {
        return serviceTypeRepository.findById(id).map(ServiceTypeEntity::toDto);
    }


}
