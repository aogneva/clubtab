package ru.ogneva.clubtab.domain;

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
@Entity
@Table(name="service_type")
public class ServiceTypeEntity {
    @Id
    @Column(name="id")
    @NotNull
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
