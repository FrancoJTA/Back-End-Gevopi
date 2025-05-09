package com.example.gevopi_back_end.Repository;

import com.example.gevopi_back_end.Entity.Reporte;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
@Repository
public interface ReporteRepository  extends JpaRepository<Reporte, Integer> {

    @Query("SELECT COUNT(r) FROM Reporte r WHERE r.fechaGenerado > :fechaLimite")
    long countReportesUltimas24Horas(@Param("fechaLimite") LocalDateTime fechaLimite);

    @Query("SELECT r FROM Reporte r ORDER BY r.fechaGenerado DESC")
    List<Reporte> findUltimos3Reportes(Pageable pageable);

    @Query("SELECT r FROM Reporte r WHERE r.fechaGenerado = (SELECT MAX(r2.fechaGenerado) FROM Reporte r2 WHERE r2.historialClinico.id = r.historialClinico.id)")
    List<Reporte> findUltimosReportesPorHistorial();

    @Query("SELECT r FROM Reporte r WHERE r.historialClinico.id = :historialId AND r.observaciones IS NOT NULL AND r.observaciones != '' ORDER BY r.fechaGenerado DESC")
    Optional<Reporte> findLatestReporteWithObservations(Integer historialId);

    @Query("SELECT r FROM Reporte r WHERE r.historialClinico.id = :historialId ORDER BY r.fechaGenerado DESC")
    List<Reporte> findAllReportesByHistorialClinicoIdOrderByFechaGenerado(Integer historialId);

    // Obtener el último reporte por historial clínico, ordenado por fechaGenerado
    Optional<Reporte> findTopByHistorialClinicoIdOrderByFechaGeneradoDesc(Integer historialId);

}
