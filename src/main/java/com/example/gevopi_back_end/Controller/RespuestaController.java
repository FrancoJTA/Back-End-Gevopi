package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.*;
import com.example.gevopi_back_end.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.execution.ErrorType;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class RespuestaController {

    @Autowired
    private RespuestaRepository respuestaRepository;
    @Autowired
    private EvaluacionRepository evaluacionRepository;
    @Autowired
    private PreguntaRepository preguntaRepository;
    @Autowired
    private ReporteRepository reporteRepository;
    @Autowired
    private HistorialClinicoRepository historialRepository;

    @MutationMapping
    public List<Respuesta> crearRespuestas(@Argument("input") List<CrearRespuestaInput> inputs) {
        // Validaciones iniciales
        if (inputs == null || inputs.isEmpty()) {
            throw new DatosInvalidosException("Debe proporcionar al menos una respuesta");
        }

        // Agrupar respuestas por evaluación
        Map<Integer, List<CrearRespuestaInput>> respuestasPorEvaluacion = inputs.stream()
                .collect(Collectors.groupingBy(CrearRespuestaInput::getEvaluacionId));

        // Validar que hay exactamente 2 evaluaciones (física y emocional)
        if (respuestasPorEvaluacion.size() != 2) {
            throw new DatosInvalidosException("Se requieren respuestas para exactamente dos evaluaciones (física y emocional)");
        }
        System.out.println("1");
        List<Respuesta> respuestasGuardadas = new ArrayList<>();
        List<Evaluacion> evaluaciones = new ArrayList<>();

        // Procesar cada grupo de respuestas por evaluación
        for (Map.Entry<Integer, List<CrearRespuestaInput>> entry : respuestasPorEvaluacion.entrySet()) {
            Integer evaluacionId = entry.getKey();
            List<CrearRespuestaInput> respuestasEvaluacion = entry.getValue();

            Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId)
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Evaluación con ID " + evaluacionId + " no encontrada",
                            ErrorType.NOT_FOUND));

            evaluaciones.add(evaluacion);

            for (CrearRespuestaInput input : respuestasEvaluacion) {
                // Validar campos obligatorios
                if (input.getPreguntaId() == null) {
                    throw new DatosInvalidosException("El ID de pregunta es requerido");
                }
                if (input.getRespuestaTexto() == null || input.getRespuestaTexto().trim().isEmpty()) {
                    throw new DatosInvalidosException("El texto de respuesta no puede estar vacío");
                }

                Pregunta pregunta = preguntaRepository.findById(input.getPreguntaId())
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                                "Pregunta con ID " + input.getPreguntaId() + " no encontrada",
                                ErrorType.NOT_FOUND));

                Respuesta respuesta = new Respuesta();
                respuesta.setEvaluacion(evaluacion);
                respuesta.setPregunta(pregunta);
                respuesta.setTextoPregunta(pregunta.getTexto());
                respuesta.setRespuestaTexto(input.getRespuestaTexto());

                respuestasGuardadas.add(respuestaRepository.save(respuesta));
            }
        }

        // Verificar que tenemos una evaluación física y una emocional
        if (evaluaciones.size() != 2) {
            throw new DatosInvalidosException("Se requieren ambas evaluaciones (física y emocional)");
        }

        System.out.println("2");
        // Generar el reporte automático
        generarReporteAutomatico(evaluaciones.get(0), evaluaciones.get(1));


        return respuestasGuardadas;
    }

    private void generarReporteAutomatico(Evaluacion evaluacionFisica, Evaluacion evaluacionEmocional) {
        // 1. Obtener respuestas y generar contenido del reporte
        List<Respuesta> respuestasFisicas = respuestaRepository.findByEvaluacionId(evaluacionFisica.getId());
        List<Respuesta> respuestasEmocionales = respuestaRepository.findByEvaluacionId(evaluacionEmocional.getId());

        String resumenFisico = generarResumenFisico(respuestasFisicas);
        String resumenEmocional = generarResumenEmocional(respuestasEmocionales);
        String estadoGeneral = determinarEstadoGeneral(resumenFisico, resumenEmocional);
        String recomendaciones = generarRecomendaciones(respuestasFisicas, respuestasEmocionales);

        // 2. Crear el reporte
        Reporte reporte = new Reporte();
        reporte.setHistorialClinico(evaluacionFisica.getHistorialClinico());
        reporte.setFechaGenerado(new Date());
        reporte.setResumenFisico(resumenFisico);
        reporte.setResumenEmocional(resumenEmocional);
        reporte.setEstadoGeneral(estadoGeneral);
        reporte.setRecomendaciones(recomendaciones);
        reporte.setObservaciones("Reporte generado automáticamente");

        // 3. Inicializar la colección de evaluaciones
        reporte.setEvaluaciones(new HashSet<>());

        // 4. Establecer relaciones bidireccionales
//        reporte.getEvaluaciones().add(evaluacionFisica);
//        reporte.getEvaluaciones().add(evaluacionEmocional);
//
//        evaluacionFisica.getReportes().add(reporte);
//        evaluacionEmocional.getReportes().add(reporte);

        // 5. Guardar todo
        Reporte reporteGuardado = reporteRepository.save(reporte);
        evaluacionRepository.save(evaluacionFisica);
        evaluacionRepository.save(evaluacionEmocional);
    }

    private String generarResumenFisico(List<Respuesta> respuestas) {
        // Lógica para generar resumen físico basado en las respuestas
        long respuestasGraves = respuestas.stream()
                .filter(r -> Integer.parseInt(r.getRespuestaTexto()) >= 4) // "Frecuentemente" o "Siempre"
                .count();

        if (respuestasGraves >= 4) {
            return "El voluntario presenta múltiples síntomas físicos graves que requieren atención inmediata";
        } else if (respuestasGraves >= 2) {
            return "El voluntario muestra varios síntomas físicos preocupantes que deben ser monitoreados";
        } else {
            return "El estado físico del voluntario es satisfactorio, con pocos o ningún síntoma preocupante";
        }
    }

    private String generarResumenEmocional(List<Respuesta> respuestas) {
        // Lógica para generar resumen emocional
        long respuestasGraves = respuestas.stream()
                .filter(r -> Integer.parseInt(r.getRespuestaTexto()) >= 4)
                .count();

        if (respuestasGraves >= 5) {
            return "El voluntario muestra signos severos de estrés emocional o trauma que requieren intervención profesional";
        } else if (respuestasGraves >= 3) {
            return "El voluntario presenta varios síntomas emocionales que sugieren un impacto psicológico significativo";
        } else {
            return "El estado emocional del voluntario parece estable, con síntomas menores dentro de lo esperado";
        }
    }

    private String determinarEstadoGeneral(String resumenFisico, String resumenEmocional) {
        // Lógica simple para determinar estado general
        if (resumenFisico.contains("grave") || resumenEmocional.contains("severos")) {
            return "CRÍTICO";
        } else if (resumenFisico.contains("preocupantes") || resumenEmocional.contains("significativo")) {
            return "MODERADO";
        } else {
            return "ESTABLE";
        }
    }

    private String generarRecomendaciones(List<Respuesta> respuestasFisicas, List<Respuesta> respuestasEmocionales) {
        List<String> recomendaciones = new ArrayList<>();

        // Recomendaciones físicas
        long sintomasFisicosGraves = respuestasFisicas.stream()
                .filter(r -> Integer.parseInt(r.getRespuestaTexto()) >= 4)
                .count();

        if (sintomasFisicosGraves > 0) {
            recomendaciones.add("Evaluación médica completa para descartar afectaciones físicas");
            if (respuestasFisicas.stream().anyMatch(r -> r.getPregunta().getTexto().contains("respirar"))) {
                recomendaciones.add("Examen pulmonar y evaluación de función respiratoria");
            }
        }

        // Recomendaciones emocionales
        long sintomasEmocionalesGraves = respuestasEmocionales.stream()
                .filter(r -> Integer.parseInt(r.getRespuestaTexto()) >= 4)
                .count();

        if (sintomasEmocionalesGraves > 0) {
            recomendaciones.add("Sesiones de apoyo psicológico o consejería");
            if (respuestasEmocionales.stream().anyMatch(r -> r.getPregunta().getTexto().contains("insomnio"))) {
                recomendaciones.add("Evaluación de hábitos de sueño y posible terapia del sueño");
            }
        }

        if (recomendaciones.isEmpty()) {
            return "Continuar con monitoreo regular. No se requieren acciones inmediatas.";
        }

        return String.join("\n", recomendaciones);
    }

    // Clases de excepción y CrearRespuestaInput permanecen igual
    public static class RecursoNoEncontradoException extends RuntimeException {
        private final ErrorType errorType;
        public RecursoNoEncontradoException(String message, ErrorType errorType) {
            super(message);
            this.errorType = errorType;
        }
        public ErrorType getErrorType() { return errorType; }
    }

    public static class DatosInvalidosException extends RuntimeException {
        public DatosInvalidosException(String message) { super(message); }
    }

    public static class CrearRespuestaInput {
        private Integer evaluacionId;
        private Integer preguntaId;
        private String respuestaTexto;
        // Getters y setters
        public Integer getEvaluacionId() { return evaluacionId; }
        public void setEvaluacionId(Integer evaluacionId) { this.evaluacionId = evaluacionId; }
        public Integer getPreguntaId() { return preguntaId; }
        public void setPreguntaId(Integer preguntaId) { this.preguntaId = preguntaId; }
        public String getRespuestaTexto() { return respuestaTexto; }
        public void setRespuestaTexto(String respuestaTexto) { this.respuestaTexto = respuestaTexto; }
    }
}