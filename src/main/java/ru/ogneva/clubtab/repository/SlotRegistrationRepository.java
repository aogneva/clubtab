package ru.ogneva.clubtab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ogneva.clubtab.domain.SlotRegistrationEntity;

import java.util.List;

@Repository
public interface SlotRegistrationRepository extends JpaRepository<SlotRegistrationEntity, Long> {

    List<SlotRegistrationEntity> findByCustomerId(Long customerId);

    List<SlotRegistrationEntity> findBySlotId(Long slotId);

    List<SlotRegistrationEntity> deleteBySlotId(Long slotId);

    List<SlotRegistrationEntity> deleteByCustomerId(Long personId);

}
