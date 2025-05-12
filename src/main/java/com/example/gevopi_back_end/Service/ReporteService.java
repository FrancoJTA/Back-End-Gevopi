package com.example.gevopi_back_end.Service;

import com.example.gevopi_back_end.Entity.*;
import com.example.gevopi_back_end.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;
    @Autowired
    private EvaluacionRepository evaluacionRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private PreguntaRepository preguntaRepository;
    @Autowired
    private RespuestaRepository respuestaRepository;
    @Autowired
    private CapacitacionRepository capacitacionRepository;
    @Autowired
    private NecesidadRepository necesidadRepository;
    @Autowired
    private WebClient webClient;

    public long countReportesUltimas24Horas() {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(1);
        return reporteRepository.countReportesUltimas24Horas(fechaLimite);
    }

    public List<Reporte> obtenerUltimos3Reportes() {
        Pageable pageable = PageRequest.of(0, 3);
        return reporteRepository.findUltimos3Reportes(pageable);
    }

    public List<Reporte> obtenerUltimosReportesPorHistorial() {
        return reporteRepository.findUltimosReportesPorHistorial();
    }

    public Optional<Reporte> obtenerUltimoReporteConObservaciones(Integer historialId) {
        return reporteRepository.findLatestReporteWithObservations(historialId);
    }

    public List<Reporte> obtenerTodosLosReportesPorHistorial(Integer historialId) {
        return reporteRepository.findAllReportesByHistorialClinicoIdOrderByFechaGenerado(historialId);
    }


    public Boolean crearReporte(Integer historialId) {

        Optional<Reporte> ultimoReporteOpt = reporteRepository.findTopByHistorialClinicoIdOrderByFechaGeneradoDesc(historialId);

        if (ultimoReporteOpt.isPresent()) {
            Reporte ultimoReporte = ultimoReporteOpt.get();

            // Comprobar si tiene datos en el campo observaciones
            if (ultimoReporte.getObservaciones() != null && !ultimoReporte.getObservaciones().isEmpty()) {
                // Si tiene observaciones, crear un nuevo reporte
                Reporte nuevoReporte = new Reporte();
                nuevoReporte.setHistorialClinico(ultimoReporte.getHistorialClinico());  // Usar el mismo HistorialClinico
                nuevoReporte.setFechaGenerado(LocalDateTime.now());  // Establecer la fecha de generación como la fecha actual

                // Guardar el nuevo reporte
                reporteRepository.save(nuevoReporte);

                // Obtener los Test con ID 1 y ID 2 desde la base de datos
                Optional<Test> test1Opt = testRepository.findById(3);
                Optional<Test> test2Opt = testRepository.findById(4);

                if (test1Opt.isPresent() && test2Opt.isPresent()) {
                    Test test1 = test1Opt.get();
                    Test test2 = test2Opt.get();

                    // Crear y asociar las evaluaciones al nuevo reporte con los Test correspondientes
                    Evaluacion evaluacion1 = new Evaluacion();
                    evaluacion1.setReporte(nuevoReporte);  // Asociar al nuevo reporte
                    evaluacion1.setTest(test1);  // Asociar el Test con ID 1
                    evaluacion1.setFecha(LocalDateTime.now());  // Establecer la fecha de la evaluación

                    Evaluacion evaluacion2 = new Evaluacion();
                    evaluacion2.setReporte(nuevoReporte);  // Asociar al nuevo reporte
                    evaluacion2.setTest(test2);  // Asociar el Test con ID 2
                    evaluacion2.setFecha(LocalDateTime.now());  // Establecer la fecha de la evaluación

                    // Guardar las evaluaciones en la base de datos
                    evaluacionRepository.save(evaluacion1);
                    evaluacionRepository.save(evaluacion2);
                }

                return true;  // Se creó un nuevo reporte y sus evaluaciones
            }
        }

        // Si no se crea el reporte, retornamos false
        return false;
    }

    private String construirTextoEvaluacion(List<Map<String, Object>> respuestas) {
        return respuestas.stream()
                .map(resp -> "P: " + resp.get("textoPregunta") + " R: " + resp.get("respuestaTexto"))
                .collect(Collectors.joining(" | "));
    }

    private String enviarEvaluacionAlApi(String textoEvaluacion, int tipo) {
        String endpoint = tipo == 1 ? "/generar_fisico" : "/generar_emocion";
        Map<String, Object> body = Map.of("evaluacion", textoEvaluacion);

        return WebClient.create("http://IA_API:5000")
                .post()
                .uri(endpoint)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(resp -> (String) resp.get("respuesta"))
                .block();
    }

    private void guardarRespuestas(List<Map<String, Object>> respuestas, Evaluacion evaluacion) {
        for (Map<String, Object> respuestaMap : respuestas) {
            Integer preguntaId = (Integer) respuestaMap.get("preguntaId");
            String textoPregunta = (String) respuestaMap.get("textoPregunta");
            String respuestaTexto = (String) respuestaMap.get("respuestaTexto");

            Optional<Pregunta> preguntaOpt = preguntaRepository.findById(preguntaId);
            if (preguntaOpt.isEmpty()) continue;

            Respuesta respuesta = new Respuesta();
            respuesta.setEvaluacion(evaluacion);
            respuesta.setPregunta(preguntaOpt.get());
            respuesta.setTextoPregunta(textoPregunta);
            respuesta.setRespuestaTexto(respuestaTexto);

            respuestaRepository.save(respuesta);
        }
    }
    public Boolean enviarRespuestas(Map<String, Object> input) {

        Integer reporteId = (Integer) input.get("reporteId");
        List<Map<String, Object>> evaluaciones = (List<Map<String, Object>>) input.get("evaluaciones");

        Optional<Reporte> reporteOpt = reporteRepository.findById(reporteId);
        if (reporteOpt.isEmpty()) return false;

        Reporte reporte = reporteOpt.get();
        StringBuilder resumenFisico = new StringBuilder();
        StringBuilder resumenEmocional = new StringBuilder();

        for (Map<String, Object> evaluacionMap : evaluaciones) {
            Integer evaluacionId = (Integer) evaluacionMap.get("evaluacionId");
            List<Map<String, Object>> respuestas = (List<Map<String, Object>>) evaluacionMap.get("respuestas");

            Optional<Evaluacion> evaluacionOpt = evaluacionRepository.findById(evaluacionId);
            if (evaluacionOpt.isEmpty()) continue;

            Evaluacion evaluacion = evaluacionOpt.get();
            int tipo = evaluacion.getTest().getId(); // ← Se asume tipo por id de Test

            // 1. Guardar respuestas en la base de datos
            guardarRespuestas(respuestas, evaluacion);

            // 2. Construir texto para enviar
            String textoEvaluacion = construirTextoEvaluacion(respuestas);

            // 3. Enviar al endpoint correspondiente
            String respuestaApi = enviarEvaluacionAlApi(textoEvaluacion, tipo);

            // 4. Guardar en resumen físico/emocional
            if (tipo == 1) {
                resumenFisico.append(respuestaApi);
                reporte.setResumenFisico(respuestaApi);
            } else if (tipo == 2) {
                resumenEmocional.append(respuestaApi);
                reporte.setResumenEmocional(respuestaApi);
            }
        }

        // 5. Guardar resumen combinado en observaciones
        String resumenFinal = resumenFisico + " " + resumenEmocional;
        reporte.setObservaciones(resumenFinal);
        Object estadoObj = input.get("estado");
        if (estadoObj != null) {
            reporte.setEstadoGeneral(estadoObj.toString());
        }
        reporteRepository.save(reporte);
        return true;
    }

    @Transactional
    public Boolean agregarCapacitacionesAReporte(Integer reporteId, List<Integer> capacitacionIds) {
        Optional<Reporte> reporteOpt = reporteRepository.findById(reporteId);
        if (reporteOpt.isEmpty()) return false;

        Reporte reporte = reporteOpt.get();
        List<Capacitacion> capacitaciones = capacitacionRepository.findAllById(capacitacionIds);
        reporte.getCapacitaciones().addAll(capacitaciones);

        reporteRepository.save(reporte);
        return true;
    }

    @Transactional
    public Boolean agregarNecesidadesAReporte(Integer reporteId, List<Integer> necesidadIds) {
        Optional<Reporte> reporteOpt = reporteRepository.findById(reporteId);
        if (reporteOpt.isEmpty()) return false;

        Reporte reporte = reporteOpt.get();
        List<Necesidad> necesidades = necesidadRepository.findAllById(necesidadIds);
        reporte.getNecesidades().addAll(necesidades);

        reporteRepository.save(reporte);
        return true;
    }

}
