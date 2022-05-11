package ru.ogneva.clubtab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ogneva.clubtab.domain.PersonEntity;
import ru.ogneva.clubtab.domain.SlotEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SlotRegistrationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4565045870175464027L;

    private Long id;

    private Long slotId;

    private Long customerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlotRegistrationDTO that = (SlotRegistrationDTO) o;
        return id.equals(that.id) && slotId.equals(that.slotId) && customerId.equals(that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, slotId, customerId);
    }

    @Override
    public String toString() {
        return "SlotRegistrationDTO{" +
                "id=" + id +
                ", slot=" + slotId +
                ", customer=" + customerId +
                '}';
    }
}
