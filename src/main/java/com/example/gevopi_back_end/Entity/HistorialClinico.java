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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "Historial_Clinico")
public class HistorialClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "historialClinico", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("historialClinico")
    private Set<Reporte> reportes = new HashSet<>();
}