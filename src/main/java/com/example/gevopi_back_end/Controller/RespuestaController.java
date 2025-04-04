package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.*;
import com.example.gevopi_back_end.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.execution.ErrorType;

@Controller
public class RespuestaController {

    @Autowired
    private RespuestaRepository respuestaRepository;
    @Autowired
    private EvaluacionRepository evaluacionRepository;
    @Autowired
    private PreguntaRepository preguntaRepository;

    @MutationMapping
    public Respuesta crearRespuesta(@Argument("input") CrearRespuestaInput input) {
        // Validar que el input no sea nulo
        if (input == null) {
            throw new DatosInvalidosException("El input no puede ser nulo");
        }

        // Validar campos obligatorios
        if (input.getEvaluacionId() == null) {
            throw new DatosInvalidosException("El ID de evaluación es requerido");
        }
        if (input.getPreguntaId() == null) {
            throw new DatosInvalidosException("El ID de pregunta es requerido");
        }
        if (input.getRespuestaTexto() == null || input.getRespuestaTexto().trim().isEmpty()) {
            throw new DatosInvalidosException("El texto de respuesta no puede estar vacío");
        }

        // Buscar evaluación con manejo de excepción específica
        Evaluacion evaluacion = evaluacionRepository.findById(input.getEvaluacionId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Evaluación con ID " + input.getEvaluacionId() + " no encontrada",
                        ErrorType.NOT_FOUND));

        // Buscar pregunta con manejo de excepción específica
        Pregunta pregunta = preguntaRepository.findById(input.getPreguntaId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Pregunta con ID " + input.getPreguntaId() + " no encontrada",
                        ErrorType.NOT_FOUND));

        // Crear y guardar la respuesta
        Respuesta respuesta = new Respuesta();
        respuesta.setEvaluacion(evaluacion);
        respuesta.setPregunta(pregunta);
        respuesta.setTextoPregunta(input.getTextoPregunta());
        respuesta.setRespuestaTexto(input.getRespuestaTexto());

        try {
            return respuestaRepository.save(respuesta);
        } catch (Exception e) {
            throw new PersistenciaException("Error al guardar la respuesta: " + e.getMessage(), e);
        }
    }

    // Excepciones personalizadas
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

    // Clase interna para el input (sin cambios)
    public static class CrearRespuestaInput {
        private Integer evaluacionId;
        private Integer preguntaId;
        private String textoPregunta;
        private String respuestaTexto;

        // Getters y setters
        public Integer getEvaluacionId() { return evaluacionId; }
        public void setEvaluacionId(Integer evaluacionId) { this.evaluacionId = evaluacionId; }
        public Integer getPreguntaId() { return preguntaId; }
        public void setPreguntaId(Integer preguntaId) { this.preguntaId = preguntaId; }
        public String getTextoPregunta() { return textoPregunta; }
        public void setTextoPregunta(String textoPregunta) { this.textoPregunta = textoPregunta; }
        public String getRespuestaTexto() { return respuestaTexto; }
        public void setRespuestaTexto(String respuestaTexto) { this.respuestaTexto = respuestaTexto; }
    }
}