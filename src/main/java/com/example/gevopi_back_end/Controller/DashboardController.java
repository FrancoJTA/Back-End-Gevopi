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

        // Capacitaciones agrupadas y contadas por nombre
        List<Capacitacion> capacitaciones = capacitacionService.obtenerCapacitaciones();
        Map<String, Long> capacitacionCounts = capacitaciones.stream()
                .collect(Collectors.groupingBy(Capacitacion::getNombre, Collectors.counting()));

        List<Dashboard.DashCapacitacion> dashCapacitaciones = capacitacionCounts.entrySet().stream()
                .map(e -> {
                    Dashboard.DashCapacitacion dc = new Dashboard.DashCapacitacion();
                    dc.setNombre(e.getKey());
                    dc.setCantidad(e.getValue().intValue());
                    return dc;
                })
                .collect(Collectors.toList());

        // Necesidades agrupadas y contadas por tipo
        List<Necesidad> necesidades = necesidadService.obtenerNecesidades();
        Map<String, Long> necesidadCounts = necesidades.stream()
                .collect(Collectors.groupingBy(Necesidad::getTipo, Collectors.counting()));

        List<Dashboard.DashNecesidades> dashNecesidades = necesidadCounts.entrySet().stream()
                .map(e -> {
                    Dashboard.DashNecesidades dn = new Dashboard.DashNecesidades();
                    dn.setNombre(e.getKey());
                    dn.setCantidad(e.getValue().intValue());
                    return dn;
                })
                .collect(Collectors.toList());

        // Universidades agrupadas y contadas por nombre
        List<Universidad> universidades = universidadService.obtenerUniversidades();
        Map<String, Long> universidadCounts = universidades.stream()
                .collect(Collectors.groupingBy(Universidad::getNombre, Collectors.counting()));

        List<Dashboard.DashUniversidades> dashUniversidades = universidadCounts.entrySet().stream()
                .map(e -> {
                    Dashboard.DashUniversidades du = new Dashboard.DashUniversidades();
                    du.setNombre(e.getKey());
                    du.setCantidad(e.getValue().intValue());
                    return du;
                })
                .collect(Collectors.toList());

        // Armamos el dashboard
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