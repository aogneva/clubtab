package ru.ogneva.clubtab.service;

import org.springframework.stereotype.Service;
import ru.ogneva.clubtab.domain.PersonEntity;
import ru.ogneva.clubtab.domain.SlotEntity;
import ru.ogneva.clubtab.domain.SlotRegistrationEntity;
import ru.ogneva.clubtab.dto.SlotRegistrationDTO;
import ru.ogneva.clubtab.repository.PersonRepository;
import ru.ogneva.clubtab.repository.SlotRegistrationRepository;
import ru.ogneva.clubtab.repository.SlotRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SlotRegistrationService {

    private final SlotRepository slotRepository;

    private final PersonRepository personRepository;
    private final SlotRegistrationRepository slotRegistrationRepository;

    SlotRegistrationService(
            final SlotRepository slotRepository,
            final PersonRepository personRepository,
            final SlotRegistrationRepository slotRegistrationRepository) {
        this.slotRepository = slotRepository;
        this.personRepository = personRepository;
        this.slotRegistrationRepository = slotRegistrationRepository;
    }

    public List<SlotRegistrationDTO> findAll() {
        return slotRegistrationRepository.findAll()
                .stream().map(SlotRegistrationEntity::toDto).collect(Collectors.toList());
    }

    public Optional<SlotRegistrationDTO> findOne(Long id) {
        return slotRegistrationRepository.findById(id)
                .map(SlotRegistrationEntity::toDto);
    }

    public List<SlotRegistrationDTO> findBySlot(Long slotId) {
        return slotRegistrationRepository.findBySlotId(slotId)
                .stream().map(SlotRegistrationEntity::toDto).collect(Collectors.toList());
    }

    public List<SlotRegistrationDTO> findByCustomer(Long customerId) {
        return slotRegistrationRepository.findByCustomerId(customerId)
                .stream().map(SlotRegistrationEntity::toDto).collect(Collectors.toList());
    }
    public Integer countBySlot(Long slotId) {
        return slotRegistrationRepository.countBySlotId(slotId);
    }
    SlotRegistrationDTO create(SlotEntity slot, Long customerId)
            throws IllegalArgumentException, NoSuchElementException {
        if (Objects.isNull(slot) || Objects.isNull(customerId)) {
            throw new IllegalArgumentException();
        }

        PersonEntity customer = personRepository.findById(customerId).orElseThrow(() -> new NoSuchElementException());

        return slotRegistrationRepository.save(
            SlotRegistrationEntity.builder()
                .slot(slot)
                .customer(customer)
                .build()
        ).toDto();
    }

    SlotRegistrationDTO create(Long slotId, Long customerId)
        throws IllegalArgumentException, NoSuchElementException {
        if (Objects.isNull(slotId) || Objects.isNull(customerId)) {
            throw new IllegalArgumentException();
        }

        SlotEntity slot = slotRepository.findById(slotId).orElseThrow(() -> new NoSuchElementException());
        return create(slot, customerId);
    }

    SlotRegistrationDTO create(SlotRegistrationDTO dto)  throws IllegalArgumentException {
        if (Objects.nonNull(dto.getId())) {
            throw new IllegalArgumentException();
        }

        return create(dto.getSlotId(), dto.getCustomerId());
    }

    public void delete(Long id) {
        slotRegistrationRepository.deleteById(id);
    }

    public Integer deleteAllBySlot(Long slotId) {
        return slotRegistrationRepository.deleteBySlotId(slotId).size();
    }

    public int deleteAllByCustomer(Long customerId) {
        return slotRegistrationRepository.deleteByCustomerId(customerId).size();
    }

}

