package ru.ogneva.clubtab.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.ogneva.clubtab.dto.ServiceTypeDTO;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="service_type")
public class ServiceTypeEntity {
    @Id
    @Column(name="id")
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name")
    @NotNull
    private String name;

    @Column(name="tag")
    @NotNull
    private String tag;

    @Column(name="gist")
    @NotNull
    private String gist;

    @Column(name="duration")
    @NotNull
    private Long duration;

    @Column(name="capacity")
    @NotNull
    private Integer capacity;

    public ServiceTypeDTO toDto() {
        return new ServiceTypeDTO(id, name, tag, gist, duration, capacity);
    }

    public static ServiceTypeEntity toEntity(ServiceTypeDTO serviceTypeDTO) {
        return new ServiceTypeEntity(
                serviceTypeDTO.getId(),
                serviceTypeDTO.getName(),
                serviceTypeDTO.getTag(),
                serviceTypeDTO.getGist(),
                serviceTypeDTO.getDuration(),
                serviceTypeDTO.getCapacity()
        );
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ServiceTypeEntity other = (ServiceTypeEntity) obj;
        if (name == null) {
            if (other.getName() != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (tag == null) {
            if (other.getTag() != null)
                return false;
        } else if (!tag.equals(other.tag))
            return false;
        if (duration == null) {
            if (other.getDuration() != null)
                return false;
        } else if (!duration.equals(other.duration))
            return false;
        if (gist == null && other.getGist() != null) {
            return false;
        }
        return (gist.equals(other.gist));
    }

    public String toString() {
        return String.format("%d %s %s %s %d",
                id, name, tag, gist, duration);
    }

}
