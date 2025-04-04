package com.example.gevopi_back_end.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Historial_Clinico")
public class HistorialClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int idVoluntario;

    private java.sql.Date fechaInicio;

    private java.sql.Date fechaActualizacion;

    @OneToMany(mappedBy = "historialClinico", fetch = FetchType.LAZY)
    private Set<Reporte> reportes;
}
