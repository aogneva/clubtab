package ru.ogneva.clubtab.service;

import org.springframework.stereotype.Service;
import ru.ogneva.clubtab.common.Constants;
import ru.ogneva.clubtab.common.ForbiddenAlertException;
import ru.ogneva.clubtab.domain.ServiceTypeEntity;
import ru.ogneva.clubtab.domain.SlotEntity;
import ru.ogneva.clubtab.domain.StateTypeEntity;
import ru.ogneva.clubtab.dto.SlotDTO;
import ru.ogneva.clubtab.repository.PersonRepository;
import ru.ogneva.clubtab.repository.ServiceTypeRepository;
import ru.ogneva.clubtab.repository.SlotRepository;
import ru.ogneva.clubtab.repository.StateTypeRepository;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
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

    public SlotDTO create(SlotDTO slotDTO) {
        SlotEntity entity = SlotEntity.toEntity(slotDTO);
        ServiceTypeEntity serviceType = slotDTO.getServiceTypeId()==null ? null :
                serviceTypeRepository.findById(slotDTO.getServiceTypeId()).orElse(null);
        if (slotDTO.getDuration() == null && serviceType!=null) {
            entity.setDuration(serviceType.getDuration());
        }
        if (slotDTO.getAvailableSeats() == null && serviceType!=null) {
            entity.setAvailableSeats(serviceType.getCapacity());
        }
        entity.setServiceType(serviceType);
        entity.setState(slotDTO.getStateId()==null ? null :
                stateTypeRepository.findById(slotDTO.getStateId()).orElse(null));
        entity.setExecutor(slotDTO.getExecutorId()==null ? null :
                personRepository.findById(slotDTO.getExecutorId()).orElse(null));
        return slotRepository.save(entity).toDto();
    }

    public SlotDTO update(SlotDTO slotDTO) throws InstanceNotFoundException {
        SlotEntity slotEntity = slotRepository.findById(slotDTO.getId())
                .orElseThrow(InstanceNotFoundException::new);
        slotEntity.setDuration(slotDTO.getDuration());
        // количество свободных мест рассчитывается, а не задаётся
        slotEntity.setStartTime(slotDTO.getStartTime());
        slotEntity.setServiceType(serviceTypeRepository.findById(slotDTO.getServiceTypeId()).orElse(null));
        slotEntity.setState(stateTypeRepository.findById(slotDTO.getStateId()).orElse(null));
        slotEntity.setExecutor(personRepository.findById(slotDTO.getExecutorId()).orElse(null));
        return slotRepository.save(slotEntity).toDto();
    }

    private SlotEntity changeSlotState(SlotEntity slotEntity, String stateTag) throws NoSuchElementException {
        StateTypeEntity state = stateTypeRepository.findByTag(stateTag).orElseThrow();
        slotEntity.setState(state);
        return slotRepository.save(slotEntity);
    }

    public SlotDTO complete(Long id) throws ForbiddenAlertException, InstanceNotFoundException {
        if (Objects.isNull(id)) {
            throw new ForbiddenAlertException("Не задан ID");
        }
        SlotEntity slotEntity = slotRepository.findById(id).orElseThrow(InstanceNotFoundException::new);
        if (!Constants.StateTypes.STATE_SCHEDULED.equalsIgnoreCase(slotEntity.getState().getTag())) {
            throw new ForbiddenAlertException("Неверный статус слота");
        }
        return changeSlotState(slotEntity, Constants.StateTypes.STATE_COMPLETED).toDto();
    }

    public SlotDTO cancel(Long id) throws ForbiddenAlertException, InstanceNotFoundException {
        if (Objects.isNull(id)) {
            throw new ForbiddenAlertException("Не задан ID");
        }
        SlotEntity slotEntity = slotRepository.findById(id).orElseThrow(InstanceNotFoundException::new);
        if (!Constants.StateTypes.STATE_SCHEDULED.equalsIgnoreCase(slotEntity.getState().getTag())) {
            throw new ForbiddenAlertException("Неверный статус слота");
        }
        return changeSlotState(slotEntity, Constants.StateTypes.STATE_CANCELED).toDto();
    }
}
