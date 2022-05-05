package ru.ogneva.clubtab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ogneva.clubtab.domain.StateTypeEntity;

import java.util.Optional;

public interface StateTypeRepository extends JpaRepository<StateTypeEntity, Long> {
    Optional<StateTypeEntity> findByTag(String tag);
}
