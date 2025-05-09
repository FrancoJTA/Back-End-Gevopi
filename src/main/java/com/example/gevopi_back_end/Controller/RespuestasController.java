package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.Reporte;
import com.example.gevopi_back_end.Service.EvaluacionService;
import com.example.gevopi_back_end.Service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class RespuestasController {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private EvaluacionService evaluacionService;

    @MutationMapping
    public Boolean crearEvaluacion(@Argument int id) {
        return reporteService.crearReporte(id);
    }

    @MutationMapping
    public Boolean enviarRespuestas(@Argument("input") Map<String, Object> input) {
        return reporteService.enviarRespuestas(input);
    }

}
