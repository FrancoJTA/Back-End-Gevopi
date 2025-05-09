package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.Reporte;
import com.example.gevopi_back_end.Service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ListaVoluntarios {
    @Autowired
    private ReporteService reporteService;


    @QueryMapping
    public List<Reporte> listaVoluntarios() {
        return reporteService.obtenerUltimosReportesPorHistorial();
    }
}
