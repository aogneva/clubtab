package ru.ogneva.clubtab.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name="person")
public class PersonEntity {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name")
    @NotNull
    private String firstName;

    @Column(name="second_name")
    private String secondName;

    @Column(name="last_name")
    @NotNull
    private String lastName;

    @Column(name="dob")
    @NotNull
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
        PersonEntity other = (PersonEntity) obj;
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
        } else return dob.equals(other.dob);
    }

    public String toString() {
        return String.format("%s %s %s (born: %s)",
                lastName, firstName, secondName, new SimpleDateFormat("dd-MM-yyyy").format(dob));
    }
}
