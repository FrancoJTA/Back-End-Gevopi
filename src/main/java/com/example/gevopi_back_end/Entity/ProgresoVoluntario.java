package com.example.gevopi_back_end.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "ProgresoVoluntario")
public class ProgresoVoluntario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Usuario")
    private HistorialClinico historialClinico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_Etapa")
    private Etapas etapa;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFinalizacion;
}