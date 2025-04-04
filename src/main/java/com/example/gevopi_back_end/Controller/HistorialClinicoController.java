package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.HistorialClinico;
import com.example.gevopi_back_end.Repository.HistorialClinicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.execution.ErrorType;
import com.example.gevopi_back_end.Exceptions.*;

@Controller
public class HistorialClinicoController {

    @Autowired
    private HistorialClinicoRepository historialRepository;

    @QueryMapping
    public HistorialClinico historialPorVoluntario(@Argument Integer voluntarioId) {
        if (voluntarioId == null || voluntarioId <= 0) {
            throw new IllegalArgumentException("ID de voluntario inválido");
        }

        return historialRepository.findByIdVoluntario(voluntarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró historial clínico para el voluntario con ID: " + voluntarioId));
        // Ahora solo pasamos el mensaje, el errorType NOT_FOUND se asigna automáticamente
    }
}