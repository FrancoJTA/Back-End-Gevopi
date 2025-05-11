package com.example.gevopi_back_end.Repository;

import com.example.gevopi_back_end.Entity.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Integer> {

    @Query("SELECT e FROM Evaluacion e WHERE e.reporte.historialClinico.id = :historialId")
    List<Evaluacion> findAllByHistorialId(Integer historialId);
}
