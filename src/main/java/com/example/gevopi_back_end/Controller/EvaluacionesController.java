package com.example.gevopi_back_end.Controller;
import com.example.gevopi_back_end.Entity.Evaluacion;
import com.example.gevopi_back_end.Service.EvaluacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class EvaluacionesController {
    @Autowired
    private EvaluacionService evaluacionService;

    // Obtener todas las evaluaciones de un historial clínico con respuestas
    @QueryMapping
    public List<Evaluacion> evaluacionesVoluntarios(@Argument Integer historialId) {
        return evaluacionService.obtenerEvaluacionesPorHistorial(historialId);
    }

    // Obtener UNA evaluación por su ID
    @QueryMapping
    public Evaluacion obtenerEvaluacionPorId(@Argument int id) {
        return evaluacionService.obtenerEvaluacionPorId(id).orElse(null);
    }

}
