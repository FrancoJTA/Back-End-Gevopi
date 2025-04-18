package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.*;
import com.example.gevopi_back_end.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.execution.ErrorType;

import java.util.*;

@Controller
public class ReporteController {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private HistorialClinicoRepository historialRepository;

    @QueryMapping
    public List<Reporte> reportesPorVoluntario(@Argument Integer voluntarioId) {
        // Validación del ID del voluntario
        if (voluntarioId == null) {
            throw new DatosInvalidosException("El ID del voluntario no puede ser nulo");
        }

        // Obtenemos el historial del voluntario con manejo de excepción específica
        HistorialClinico historial = historialRepository.findByIdVoluntario(voluntarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Historial clínico no encontrado para el voluntario con ID: " + voluntarioId,
                        ErrorType.NOT_FOUND));

        // Obtenemos los reportes asociados a ese historial
        try {
            List<Reporte> reportes = reporteRepository.findByHistorialClinicoId(historial.getId());
            if (reportes.isEmpty()) {
                throw new RecursoNoEncontradoException(
                        "No se encontraron reportes para el historial con ID: " + historial.getId(),
                        ErrorType.NOT_FOUND);
            }
            return reportes;
        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al recuperar los reportes del historial: " + e.getMessage(), e);
        }
    }

    @SchemaMapping(typeName = "Reporte", field = "evaluaciones")
    public Set<Evaluacion> evaluacionesPorReporte(Reporte reporte) {
        return reporte.getEvaluaciones(); // Puede ser null o vacío
    }

    @SchemaMapping(typeName = "Reporte", field = "capacitaciones")
    public Set<Capacitacion> capacitacionesPorReporte(Reporte reporte) {
        return reporte.getCapacitaciones();
    }

    @SchemaMapping(typeName = "Reporte", field = "necesidades")
    public Set<Necesidad> necesidadesPorReporte(Reporte reporte) {
        return reporte.getNecesidades(); // Puede ser null o vacío
    }

    // Excepciones personalizadas (pueden estar en una clase aparte para reutilizar)
    public static class RecursoNoEncontradoException extends RuntimeException {
        private final ErrorType errorType;

        public RecursoNoEncontradoException(String message, ErrorType errorType) {
            super(message);
            this.errorType = errorType;
        }

        public ErrorType getErrorType() {
            return errorType;
        }
    }

    public static class DatosInvalidosException extends RuntimeException {
        public DatosInvalidosException(String message) {
            super(message);
        }
    }

    public static class PersistenciaException extends RuntimeException {
        public PersistenciaException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}