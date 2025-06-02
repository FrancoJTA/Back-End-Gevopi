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
        List<Universidad> universidades = universidadService.obtenerUniversidades();
        List<Dashboard.DashUniversidades> dashUniversidades = universidades.stream()
                .map(universidad -> {
                    int count = universidad.getEvaluaciones() != null ? universidad.getEvaluaciones().size() : 0;
                    Dashboard.DashUniversidades du = new Dashboard.DashUniversidades();
                    du.setNombre(universidad.getNombre());
                    du.setCantidad(count);
                    return du;
                }).collect(Collectors.toList());


        List<Capacitacion> capacitaciones = capacitacionService.obtenerCapacitaciones();
        List<Dashboard.DashCapacitacion> dashCapacitaciones = capacitaciones.stream()
                .map(capacitacion -> {
                    int count = capacitacion.getReportes() != null ? capacitacion.getReportes().size() : 0;
                    Dashboard.DashCapacitacion dc = new Dashboard.DashCapacitacion();
                    dc.setNombre(capacitacion.getNombre());
                    dc.setCantidad(count);
                    return dc;
                }).collect(Collectors.toList());

        List<Necesidad> necesidades = necesidadService.obtenerNecesidades();
        List<Dashboard.DashNecesidades> dashNecesidades = necesidades.stream()
                .map(necesidad -> {
                    int count = necesidad.getReportes() != null ? necesidad.getReportes().size() : 0;
                    Dashboard.DashNecesidades dn = new Dashboard.DashNecesidades();
                    dn.setNombre(necesidad.getTipo());
                    dn.setCantidad(count);
                    return dn;
                }).collect(Collectors.toList());

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
