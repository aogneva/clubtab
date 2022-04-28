package ru.ogneva.clubtab.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class PersonDTO implements Serializable {

    private static final long serialVersionUID = -1052504454168250981L;

    private Long id;
    private String firstName;
    private String secondName;
    private String lastName;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy", locale = "ru-RU", timezone = "Asia/Yekaterinburg")
    private Date dob;

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
        PersonDTO other = (PersonDTO) obj;
        if (firstName == null) {
            if (other.getFirstName() != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (secondName == null) {
            if (other.getSecondName() != null)
                return false;
        } else if (!secondName.equals(other.secondName))
            return false;
        if (lastName == null) {
            if (other.getLastName() != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (dob == null) {
            return other.dob == null;
        } else if (!dob.equals(other.dob)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return String.format("%s %s %s (born: %s)",
                lastName, firstName, secondName, new SimpleDateFormat("dd-MM-yyyy").format(dob));
    }
}
