package ru.ogneva.clubtab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ogneva.clubtab.domain.StateTypeEntity;

public interface StateTypeRepository extends JpaRepository<StateTypeEntity, Long> {
}
