package ru.ogneva.clubtab.domain;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class SlotEntity {
    @Id
    private Long id;

    @Column(name="start_time")
    private Instant startTime;

    @Column(name ="duration")
    private Long duration;

    @ManyToOne(targetEntity = ServiceTypeEntity.class)
    private ServiceTypeEntity serviceType;

    @ManyToOne(targetEntity = PersonEntity.class)
    private PersonEntity executor;

    @ManyToOne(targetEntity = StateTypeEntity.class)
    private StateTypeEntity state;



}
