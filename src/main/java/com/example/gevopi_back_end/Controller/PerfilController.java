package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.Reporte;
import com.example.gevopi_back_end.Service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class PerfilController {

    @Autowired
    private ReporteService reporteService;

    // Obtener el último reporte de un historial clínico con observaciones
    @QueryMapping
    public Optional<Reporte> ultimoReporteVoluntario(@Argument Integer historialId) {
        return reporteService.obtenerUltimoReporteConObservaciones(historialId);
    }
}