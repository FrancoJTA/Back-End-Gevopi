package com.example.gevopi_back_end.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import com.example.gevopi_back_end.Entity.Reporte;
import com.example.gevopi_back_end.Service.ReporteService;

import java.util.List;

@Controller
public class ReporteController {
    @Autowired
    private ReporteService reporteService;

    // Obtener todos los reportes de un historial clínico, ordenados por fecha
    @QueryMapping
    public List<Reporte> reportesVoluntarios(@Argument Integer historialId) {
        return reporteService.obtenerTodosLosReportesPorHistorial(historialId);
    }

    @MutationMapping
    public Boolean agregarCapacitacionesAReporte(@Argument Integer reporteId, @Argument List<Integer> capacitacionIds) {
        return reporteService.agregarCapacitacionesAReporte(reporteId, capacitacionIds);
    }

    @MutationMapping
    public Boolean agregarNecesidadesAReporte(@Argument Integer reporteId, @Argument List<Integer> necesidadIds) {
        return reporteService.agregarNecesidadesAReporte(reporteId, necesidadIds);
    }
}
