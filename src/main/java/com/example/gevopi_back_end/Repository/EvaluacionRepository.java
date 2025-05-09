package com.example.gevopi_back_end.Repository;

import com.example.gevopi_back_end.Entity.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Integer> {

    @Query("SELECT e FROM Evaluacion e " +
            "JOIN FETCH e.reporte r " +
            "JOIN FETCH r.historialClinico hc " +
            "LEFT JOIN FETCH Respuesta res ON res.evaluacion.id = e.id " +  // Cambiar la forma de acceder a respuestas
            "WHERE hc.id = :historialId")
    List<Evaluacion> findEvaluacionesWithRespuestasByHistorialId(Integer historialId);
}
