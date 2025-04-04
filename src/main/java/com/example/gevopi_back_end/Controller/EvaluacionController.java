package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.*;
import com.example.gevopi_back_end.Repository.*;
import com.example.gevopi_back_end.Exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.execution.ErrorType;

import java.util.Date;

@Controller
public class EvaluacionController {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private HistorialClinicoRepository historialRepository;

    @MutationMapping
    public Evaluacion crearEvaluacion(@Argument("input") CrearEvaluacionInput input) {
        // Validación del input
        if (input == null) {
            throw new DatosInvalidosException("El input de evaluación no puede ser nulo");
        }

        // Validación de campos obligatorios
        if (input.getTestId() == null) {
            throw new DatosInvalidosException("El ID del test es requerido");
        }
        if (input.getHistorialId() == null) {
            throw new DatosInvalidosException("El ID del historial clínico es requerido");
        }

        // Validación de IDs positivos
        if (input.getTestId() <= 0) {
            throw new DatosInvalidosException("El ID del test debe ser un número positivo");
        }
        if (input.getHistorialId() <= 0) {
            throw new DatosInvalidosException("El ID del historial clínico debe ser un número positivo");
        }

        // Buscar test con manejo de excepciones
        Test test = testRepository.findById(input.getTestId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Test con ID " + input.getTestId() + " no encontrado",
                        ErrorType.NOT_FOUND));

        // Buscar historial clínico con manejo de excepciones
        HistorialClinico historial = historialRepository.findById(input.getHistorialId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Historial clínico con ID " + input.getHistorialId() + " no encontrado",
                        ErrorType.NOT_FOUND));

        // Crear y guardar la evaluación
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setTest(test);
        evaluacion.setHistorialClinico(historial);
        evaluacion.setFecha(new Date());

        try {
            return evaluacionRepository.save(evaluacion);
        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al guardar la evaluación: " + e.getMessage(), e);
        }
    }

    // Clase interna para el input
    public static class CrearEvaluacionInput {
        private Integer testId;
        private Integer historialId;

        // Getters y setters
        public Integer getTestId() { return testId; }
        public void setTestId(Integer testId) { this.testId = testId; }
        public Integer getHistorialId() { return historialId; }
        public void setHistorialId(Integer historialId) { this.historialId = historialId; }
    }
}