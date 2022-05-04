package ru.ogneva.clubtab.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="slot")
public class SlotEntity {
    @Id
    private Long id;

    @Column(name="start_time")
    private Instant startTime;

    @Column(name ="duration")
    private Long duration;

    @JoinColumn(name="service_type_id")
    @ManyToOne(targetEntity = ServiceTypeEntity.class)
    private ServiceTypeEntity serviceType;

    @JoinColumn(name="executor_id")
    @ManyToOne(targetEntity = PersonEntity.class)
    private PersonEntity executor;

    @JoinColumn(name="state_id")
    @ManyToOne(targetEntity = StateTypeEntity.class)
    private StateTypeEntity state;

}
