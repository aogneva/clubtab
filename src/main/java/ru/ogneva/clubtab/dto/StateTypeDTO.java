package ru.ogneva.clubtab.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StateTypeDTO {
    private Long id;
    private String name;
    private String tag;
    private String gist;
}
