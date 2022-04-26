package ru.ogneva.clubtab.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceTypeDTO {
    private Long id;
    private String name;
    private String tag;
    private String gist;
}
