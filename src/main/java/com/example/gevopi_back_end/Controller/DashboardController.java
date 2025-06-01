package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Class.Dashboard;
import com.example.gevopi_back_end.Entity.*;
import com.example.gevopi_back_end.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private EvaluacionService evaluacionService;

    @Autowired
    private CapacitacionService capacitacionService;

    @Autowired
    private NecesidadService necesidadService;

    @Autowired
    private UniversidadService universidadService;

    @QueryMapping
    public Dashboard obtenerDashboard() {
        List<Reporte> reportesUltimos3 = reporteService.obtenerUltimos3Reportes();

        long cantidadReportesUltimas24Horas = reporteService.countReportesUltimas24Horas();

        long cantidadEvaluacionesCompletas = evaluacionService.evaluacionesCompletas();

        // Obtener todas las evaluaciones para universidades
        List<Evaluacion> evaluaciones = evaluacionService.obtenerTodasEvaluaciones();

        // --- UNIVERSIDADES con cantidad de evaluaciones relacionadas ---
        List<Universidad> universidades = universidadService.obtenerUniversidades();
        List<Dashboard.DashUniversidades> dashUniversidades = universidades.stream()
                .map(universidad -> {
                    long count = evaluaciones.stream()
                            .filter(e -> e.getUniversidad() != null &&
                                    Objects.equals(e.getUniversidad().getId(), universidad.getId()))
                            .count();
                    Dashboard.DashUniversidades du = new Dashboard.DashUniversidades();
                    du.setNombre(universidad.getNombre());
                    du.setCantidad((int) count);
                    return du;
                }).collect(Collectors.toList());

        // Obtener todos los reportes para capacitaciones y necesidades
        List<Reporte> reportes = reporteService.obtenerTodosReportes();

        // --- CAPACITACIONES con cantidad de reportes relacionadas ---
        List<Capacitacion> capacitaciones = capacitacionService.obtenerCapacitaciones();
        List<Dashboard.DashCapacitacion> dashCapacitaciones = capacitaciones.stream()
                .map(capacitacion -> {
                    long count = reportes.stream()
                            .filter(r -> r.getCapacitaciones() != null &&
                                    r.getCapacitaciones().stream()
                                            .anyMatch(c -> Objects.equals(capacitacion.getId(), c.getId())))
                            .count();
                    Dashboard.DashCapacitacion dc = new Dashboard.DashCapacitacion();
                    dc.setNombre(capacitacion.getNombre());
                    dc.setCantidad((int) count);
                    return dc;
                }).collect(Collectors.toList());

        // --- NECESIDADES con cantidad de reportes relacionadas ---
        List<Necesidad> necesidades = necesidadService.obtenerNecesidades();
        List<Dashboard.DashNecesidades> dashNecesidades = necesidades.stream()
                .map(necesidad -> {
                    long count = reportes.stream()
                            .filter(r -> r.getNecesidades() != null &&
                                    r.getNecesidades().stream()
                                            .anyMatch(n -> n.getId().equals(necesidad.getId())))
                            .count();
                    Dashboard.DashNecesidades dn = new Dashboard.DashNecesidades();
                    dn.setNombre(necesidad.getTipo());
                    dn.setCantidad((int) count);
                    return dn;
                }).collect(Collectors.toList());

        // Armar el dashboard final
        Dashboard dashboard = new Dashboard();
        dashboard.setReportes(reportesUltimos3);
        dashboard.setReport_cantidad((int) cantidadReportesUltimas24Horas);
        dashboard.setEva_cantidad((int) cantidadEvaluacionesCompletas);
        dashboard.setCapacitacion(dashCapacitaciones);
        dashboard.setNecesidad(dashNecesidades);
        dashboard.setUniversidad(dashUniversidades);

        return dashboard;
    }

}