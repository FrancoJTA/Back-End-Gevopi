package com.example.gevopi_back_end.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Reporte")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Historial")
    private HistorialClinico historialClinico;

    private java.util.Date fechaGenerado;

    private String resumenFisico;

    private String resumenEmocional;

    private String estadoGeneral;

    private String recomendaciones;

    private String observaciones;

    @ManyToMany
    @JoinTable(
            name = "Reporte_Evaluacion",
            joinColumns = @JoinColumn(name = "ID_Reporte"),
            inverseJoinColumns = @JoinColumn(name = "ID_Evaluacion")
    )
    private Set<Evaluacion> evaluaciones;

    @ManyToMany
    @JoinTable(
            name = "Reporte_Necesidad",
            joinColumns = @JoinColumn(name = "ID_Reporte"),
            inverseJoinColumns = @JoinColumn(name = "ID_Necesidad")
    )
    private Set<Necesidad> necesidades;

    @ManyToMany
    @JoinTable(
            name = "Reporte_Capacitacion",
            joinColumns = @JoinColumn(name = "ID_Reporte"),
            inverseJoinColumns = @JoinColumn(name = "ID_Capacitacion")
    )
    private Set<Capacitacion> capacitaciones;
}