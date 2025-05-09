package com.example.gevopi_back_end.Service;

import com.example.gevopi_back_end.Entity.Evaluacion;
import com.example.gevopi_back_end.Repository.EvaluacionRepository;
import com.example.gevopi_back_end.Repository.RespuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;
    @Autowired
    private RespuestaRepository respuestaRepository;


    public long evaluacionesCompletas(){
        return evaluacionRepository.findAll().stream()
                .filter(evaluacion -> !respuestaRepository.findByEvaluacionId(evaluacion.getId()).isEmpty())
                .count();
    }

    public List<Evaluacion> obtenerEvaluacionesConRespuestas(Integer historialId) {
        return evaluacionRepository.findEvaluacionesWithRespuestasByHistorialId(historialId);
    }
}
