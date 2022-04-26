package ru.ogneva.clubtab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ogneva.clubtab.domain.ServiceTypeEntity;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceTypeEntity, Long> {
}
