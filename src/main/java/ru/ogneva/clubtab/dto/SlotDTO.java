package ru.ogneva.clubtab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlotDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1190067368239497426L;

    private Long id;
    private Instant startTime;
    private Long duration;
    private Long serviceTypeId;
    private Long executorId;
    private Long stateId;
    private Integer capacity;

    @Override
    public SlotDTO clone(){
        return SlotDTO.builder()
                .id(id)
                .startTime(startTime)
                .duration(duration)
                .capacity(capacity)
                .serviceTypeId(serviceTypeId)
                .executorId(executorId)
                .stateId(stateId)
                .build();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlotDTO slotDTO = (SlotDTO) o;
        return id.equals(slotDTO.id)
                && startTime.equals(slotDTO.startTime)
                && duration.equals(slotDTO.duration)
                && capacity.equals(slotDTO.capacity)
                && Objects.equals(serviceTypeId, slotDTO.serviceTypeId)
                && Objects.equals(executorId, slotDTO.executorId)
                && Objects.equals(stateId, slotDTO.stateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, duration, capacity, serviceTypeId, executorId, stateId);
    }

    @Override
    public String toString() {
        return "SlotDTO{" +
                "id=" + id +
                ", start_time=" + startTime +
                ", duration=" + duration +
                ", capacity=" + capacity +
                ", serviceTypeId=" + serviceTypeId +
                ", executorId=" + executorId +
                ", StateId=" + stateId +
                '}';
    }
}
