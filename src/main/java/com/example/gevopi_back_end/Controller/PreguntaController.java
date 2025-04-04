package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.*;
import com.example.gevopi_back_end.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.execution.ErrorType;

import java.util.List;

@Controller
public class PreguntaController {

    @Autowired
    private PreguntaRepository preguntaRepository;

    @QueryMapping
    public List<Pregunta> preguntasPorTest(@Argument Integer testId) {
        // Validación del ID del test
        if (testId == null) {
            throw new DatosInvalidosException("El ID del test no puede ser nulo");
        }

        // Validación de que el ID sea positivo
        if (testId <= 0) {
            throw new DatosInvalidosException("El ID del test debe ser un número positivo");
        }

        try {
            List<Pregunta> preguntas = preguntaRepository.findByTestId(testId);

            // Validación de que existan preguntas para el test
            if (preguntas.isEmpty()) {
                throw new RecursoNoEncontradoException(
                        "No se encontraron preguntas para el test con ID: " + testId,
                        ErrorType.NOT_FOUND);
            }

            return preguntas;
        } catch (Exception e) {
            throw new PersistenciaException(
                    "Error al recuperar las preguntas del test: " + e.getMessage(), e);
        }
    }

    // Excepciones personalizadas (consistentes con los otros controladores)
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