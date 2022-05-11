package ru.ogneva.clubtab.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="slot_registration")
public class SlotRegistrationEntity {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="slot_id")
    private SlotEntity slot;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private PersonEntity customer;

}
