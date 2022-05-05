package ru.ogneva.clubtab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ogneva.clubtab.domain.SlotEntity;

public interface SlotRepository extends JpaRepository<SlotEntity, Long> {
}
