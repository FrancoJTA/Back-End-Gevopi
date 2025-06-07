package com.example.gevopi_back_end.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "Capacitacion")
public class Capacitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String nombre;
    private String descripcion;

    @ManyToMany(mappedBy = "capacitaciones")
    @JsonIgnore
    private Set<Reporte> reportes = new HashSet<>();

    @OneToMany(mappedBy = "capacitacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("capacitacion")
    private Set<Cursos> cursos = new HashSet<>();
}