package ru.ogneva.clubtab.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class Person {
    private String firstName;
    private String secondName;
    private String lastName;
    private Date dob;
}
