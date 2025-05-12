package com.example.gevopi_back_end.Entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "Reporte")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Historial")
    @JsonIgnoreProperties("reportes")
    private HistorialClinico historialClinico;

    private LocalDateTime fechaGenerado;
    @Column(length = 2000)
    private String observaciones;

    @Column(length = 1000)
    private String resumenFisico;

    @Column(length = 1000)
    private String resumenEmocional;
    private String estadoGeneral;
    private String recomendaciones;

    @OneToMany(mappedBy = "reporte", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("reporte")
    private Set<Evaluacion> evaluaciones = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "Reporte_Capacitacion",
            joinColumns = @JoinColumn(name = "ID_Reporte"),
            inverseJoinColumns = @JoinColumn(name = "ID_Capacitacion")
    )
    @JsonIgnoreProperties("reportes")
    private Set<Capacitacion> capacitaciones = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "Reporte_Necesidad",
            joinColumns = @JoinColumn(name = "ID_Reporte"),
            inverseJoinColumns = @JoinColumn(name = "ID_Necesidad")
    )
    @JsonIgnoreProperties("reportes")
    private Set<Necesidad> necesidades = new HashSet<>();
}