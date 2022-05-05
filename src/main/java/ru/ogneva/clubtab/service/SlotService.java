package ru.ogneva.clubtab.service;

import org.springframework.stereotype.Service;
import ru.ogneva.clubtab.domain.SlotEntity;
import ru.ogneva.clubtab.dto.SlotDTO;
import ru.ogneva.clubtab.repository.PersonRepository;
import ru.ogneva.clubtab.repository.ServiceTypeRepository;
import ru.ogneva.clubtab.repository.SlotRepository;
import ru.ogneva.clubtab.repository.StateTypeRepository;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SlotService {
    final private SlotRepository slotRepository;

    final private ServiceTypeRepository serviceTypeRepository;
    final private StateTypeRepository stateTypeRepository;

    final private PersonRepository personRepository;

    public SlotService(SlotRepository slotRepository,
                       ServiceTypeRepository serviceTypeRepository,
                       StateTypeRepository stateTypeRepository,
                       PersonRepository personRepository) {
        this.slotRepository = slotRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.stateTypeRepository = stateTypeRepository;
        this.personRepository = personRepository;
    }

    public List<SlotDTO> findAll() {
        return slotRepository.findAll().stream().map(SlotEntity::toDto).collect(Collectors.toList());
    }

    public Optional<SlotDTO> findOne(Long id) {
        return slotRepository.findById(id).map(SlotEntity::toDto);
    }

    public void delete(Long id) {
        slotRepository.deleteById(id);
    }

    public SlotDTO create(SlotDTO slotDTO) throws Exception {
        if (Objects.nonNull(slotDTO.getId())) {
            throw new Exception("Идентификатор нового объекта не null");
        }
        SlotEntity entity = SlotEntity.toEntity(slotDTO);
        entity.setServiceType(slotDTO.getServiceTypeId()==null ? null :
                serviceTypeRepository.findById(slotDTO.getServiceTypeId()).orElse(null));
        entity.setState(slotDTO.getStateId()==null ? null :
                stateTypeRepository.findById(slotDTO.getStateId()).orElse(null));
        entity.setExecutor(slotDTO.getExecutorId()==null ? null :
                personRepository.findById(slotDTO.getExecutorId()).orElse(null));
        return slotRepository.save(entity).toDto();
    }

    public SlotDTO update(SlotDTO slotDTO) throws InstanceNotFoundException {
        if (Objects.isNull(slotDTO.getId())) {
            throw new InstanceNotFoundException();
        }
        Optional<SlotEntity> slotEntity = slotRepository.findById(slotDTO.getId());
        if (slotEntity.isEmpty()) {
            throw new InstanceNotFoundException();
        }
        slotEntity.get().setDuration(slotDTO.getDuration());
        slotEntity.get().setStartTime(slotDTO.getStartTime());
        return slotRepository.save(slotEntity.get()).toDto();
    }
}
