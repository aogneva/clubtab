package ru.ogneva.clubtab.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceType {
    private Long id;
    private String name;
    private String tag;
    private String gist;
}
