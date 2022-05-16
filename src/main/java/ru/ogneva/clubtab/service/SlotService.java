package ru.ogneva.clubtab.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.ogneva.clubtab.common.Constants;
import ru.ogneva.clubtab.common.ForbiddenAlertException;
import ru.ogneva.clubtab.domain.ServiceTypeEntity;
import ru.ogneva.clubtab.domain.SlotEntity;
import ru.ogneva.clubtab.domain.StateTypeEntity;
import ru.ogneva.clubtab.dto.SlotDTO;
import ru.ogneva.clubtab.dto.SlotRegistrationDTO;
import ru.ogneva.clubtab.repository.*;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SlotService {
    final private SlotRepository slotRepository;
    final private ServiceTypeRepository serviceTypeRepository;
    final private StateTypeRepository stateTypeRepository;
    final private PersonRepository personRepository;
    final private SlotRegistrationService slotRegistrationService;

    public SlotService(SlotRepository slotRepository,
                       ServiceTypeRepository serviceTypeRepository,
                       StateTypeRepository stateTypeRepository,
                       PersonRepository personRepository,
                       SlotRegistrationService slotRegistrationService) {
        this.slotRepository = slotRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.stateTypeRepository = stateTypeRepository;
        this.personRepository = personRepository;
        this.slotRegistrationService = slotRegistrationService;
    }

    public List<SlotDTO> findAll() {
        return slotRepository.findAll().stream().map(SlotEntity::toDto).collect(Collectors.toList());
    }

    public Optional<SlotDTO> findOne(Long id) {
        return slotRepository.findById(id).map(SlotEntity::toDto);
    }

    public void delete(Long id)  throws NoSuchElementException, ForbiddenAlertException {
        Optional<SlotEntity> slot = slotRepository.findById(id);
        if (slot.isEmpty()) {
            throw new NoSuchElementException("Слот не найден");
        }
        if (!Constants.StateTypes.STATE_SCHEDULED.equalsIgnoreCase(slot.get().getState().getTag())) {
            throw new ForbiddenAlertException(String.format("Невозможно удалить в статусе %s", slot.get().getState().getName()));
        }

        Integer countRegs = slotRegistrationService.countBySlot(id);
        if (countRegs>0) {
            throw new ForbiddenAlertException("Имеются зарегистрированные участники");
        }

        slotRepository.deleteById(id);
    }

    public SlotDTO create(SlotDTO slotDTO) {
        SlotEntity entity = SlotEntity.toEntity(slotDTO);
        ServiceTypeEntity serviceType = slotDTO.getServiceTypeId()==null ? null :
                serviceTypeRepository.findById(slotDTO.getServiceTypeId()).orElse(null);
        if (slotDTO.getDuration() == null && serviceType!=null) {
            entity.setDuration(serviceType.getDuration());
        }
        if (slotDTO.getCapacity() == null && serviceType!=null) {
            entity.setCapacity(serviceType.getCapacity());
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

    @Transactional(isolation=Isolation.READ_COMMITTED)
    public SlotDTO cancel(Long id) throws ForbiddenAlertException, InstanceNotFoundException {
        if (Objects.isNull(id)) {
            throw new ForbiddenAlertException("Не задан ID");
        }
        SlotEntity slotEntity = slotRepository.findById(id).orElseThrow(InstanceNotFoundException::new);
        if (!Constants.StateTypes.STATE_SCHEDULED.equalsIgnoreCase(slotEntity.getState().getTag())) {
            throw new ForbiddenAlertException("Неверный статус слота");
        }
        slotRegistrationService.deleteAllBySlot(slotEntity.getId());
        return changeSlotState(slotEntity, Constants.StateTypes.STATE_CANCELED).toDto();
    }

    @Transactional(isolation=Isolation.READ_COMMITTED)
    public SlotRegistrationDTO registerCustomer(Long slotId, Long customerId)
        throws IllegalArgumentException, NoSuchElementException, ForbiddenAlertException
    {
        if (Objects.isNull(slotId) || Objects.isNull(customerId)) {
            throw new IllegalArgumentException();
        }
        SlotRegistrationDTO reg = null;
        SlotEntity slot = slotRepository.findById(slotId).orElseThrow(() -> new NoSuchElementException());
        if (!canModify(slot.getState().getTag())) {
            throw new ForbiddenAlertException("Неверный статус слота");
        }
        Integer availableSeats = getAvailableSeats(slot);
        if (availableSeats<1) {
            throw new ForbiddenAlertException("Свободных мест нет");
        }
        try {
            reg = slotRegistrationService.create(slot, customerId);
        } catch (Exception e) {
            return reg;
        }
        return reg;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    private Integer getAvailableSeats(SlotEntity slot) {
        return slot.getCapacity() - slotRegistrationService.countBySlot(slot.getId());
    }

    public Integer getAvailableSeats(Long slotId) throws NoSuchElementException {
        SlotEntity slot = slotRepository.findById(slotId).orElseThrow(() -> new NoSuchElementException());
        return getAvailableSeats(slot);
    }

    public boolean canModify(String statusTag) {
        return Constants.StateTypes.STATE_SCHEDULED.equalsIgnoreCase(statusTag);
    }
}
