package ru.ogneva.clubtab.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ogneva.clubtab.dto.SlotDTO;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="slot")
public class SlotEntity {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="start_time")
    private Instant startTime;

    /**
     * Длительность в минутах.
     * Если явно не указана, должна браться из serviceType
     */
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

    public SlotDTO toDto() {
        return SlotDTO.builder()
                .id(id)
                .startTime(startTime)
                .duration(duration)
                .serviceTypeId(serviceType == null ? null : serviceType.getId())
                .executorId(executor == null ? null : executor.getId())
                .stateId(state == null ? null : state.getId())
                .build();
    }

    public static SlotEntity toEntity(SlotDTO slotDTO) {
        return SlotEntity.builder()
            .id(slotDTO.getId())
            .duration(slotDTO.getDuration())
            .startTime(slotDTO.getStartTime())
            .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlotEntity that = (SlotEntity) o;
        return id.equals(that.id) && startTime.equals(that.startTime) && duration.equals(that.duration) && Objects.equals(serviceType, that.serviceType) && Objects.equals(executor, that.executor) && Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, duration, serviceType, executor, state);
    }

    @Override
    public String toString() {
        return "SlotEntity{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", serviceType=" + serviceType +
                ", executor=" + executor +
                ", state=" + state +
                '}';
    }
}
