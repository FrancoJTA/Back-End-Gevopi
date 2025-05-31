package com.example.gevopi_back_end.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "Universidad")
public class Universidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String nombre;
    private String direccion;
    @Column(name = "teléfono")
    private String telefono;

    @OneToMany(mappedBy = "universidad", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("universidad")
    private Set<Evaluacion> evaluaciones = new HashSet<>();
}
