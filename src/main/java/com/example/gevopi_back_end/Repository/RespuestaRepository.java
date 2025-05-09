package com.example.gevopi_back_end.Repository;

import com.example.gevopi_back_end.Entity.Evaluacion;
import com.example.gevopi_back_end.Entity.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RespuestaRepository extends JpaRepository<Respuesta, Integer> {

    List<Respuesta> findByEvaluacionId(int evaluacionId);

}
