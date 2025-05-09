package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.*;
import com.example.gevopi_back_end.Service.EvaluacionService;
import com.example.gevopi_back_end.Service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class DashboardController {
    @Autowired
    private ReporteService reporteService;
    @Autowired
    private EvaluacionService evaluacionService;

    @QueryMapping
    public Map<String, Object> obtenerDashboard() {
        // Obtener los últimos 3 reportes generados
        List<Reporte> reportesUltimos3 = reporteService.obtenerUltimos3Reportes();

        // Obtener la cantidad de reportes generados en las últimas 24 horas
        long cantidadReportesUltimas24Horas = reporteService.countReportesUltimas24Horas();

        // Obtener la cantidad de evaluaciones completas
        long cantidadEvaluacionesCompletas = evaluacionService.evaluacionesCompletas();

        // Crear un mapa para almacenar los resultados
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("reportes", reportesUltimos3);
        dashboardData.put("report_cantidad", (int) cantidadReportesUltimas24Horas);
        dashboardData.put("eva_cantidad", (int) cantidadEvaluacionesCompletas);

        return dashboardData;
    }
}
