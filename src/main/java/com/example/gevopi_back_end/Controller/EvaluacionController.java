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
        // Validación básica del input
        if (input == null) {
            throw new DatosInvalidosException("El input de evaluación no puede ser nulo");
        }

        // Validación del historial clínico (obligatorio)
        if (input.getvoluntarioIdd() == null || input.getvoluntarioIdd() <= 0) {
            throw new DatosInvalidosException("Se requiere un ID de historial clínico válido");
        }

        // Buscar historial clínico
        HistorialClinico historial = historialRepository.findByIdVoluntario(input.getvoluntarioIdd())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Historial clínico no encontrado con ID: " + input.getvoluntarioIdd(),
                        ErrorType.NOT_FOUND));
        int fisico = 1;
        int emocional = 2;

        Evaluacion evaluacionFisica = new Evaluacion();
        Evaluacion evaluacionEmocional = new Evaluacion();
        evaluacionEmocional.setHistorialClinico(historial);
        evaluacionEmocional.setFecha(new Date());
        evaluacionFisica.setHistorialClinico(historial);
        evaluacionFisica.setFecha(new Date());
        Test test = testRepository.findById(fisico)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Test no encontrado con ID: " + input.getTestId(),
                        ErrorType.NOT_FOUND));
        evaluacionFisica.setTest(test);

        Test test2 = testRepository.findById(emocional)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Test no encontrado con ID: " + input.getTestId(),
                        ErrorType.NOT_FOUND));
        evaluacionEmocional.setTest(test2);

        try {
            evaluacionRepository.save(evaluacionEmocional);
            evaluacionRepository.save(evaluacionFisica);

            System.out.println("http://localhost:3000/Formulario?evaluacionFisica=" + evaluacionFisica.getId()
                    + "&evaluacionEmocional=" + evaluacionEmocional.getId());

            return evaluacionFisica;
        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al guardar la evaluación: " + e.getMessage(), e);
        }
    }


    // Clase interna para el input
    public static class CrearEvaluacionInput {
        private Integer testId;
        private Integer voluntarioId;

        // Getters y setters
        public Integer getTestId() { return testId; }
        public void setTestId(Integer testId) { this.testId = testId; }
        public Integer getvoluntarioIdd() { return voluntarioId; }
        public void setvoluntarioId(Integer historialId) { this.voluntarioId = historialId; }
    }
}