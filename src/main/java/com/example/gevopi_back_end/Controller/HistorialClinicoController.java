package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.HistorialClinico;
import com.example.gevopi_back_end.Repository.HistorialClinicoRepository;
import com.example.gevopi_back_end.Service.HistorialClinicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class HistorialClinicoController {

    @Autowired
    private HistorialClinicoService historialClinicoService;

    @QueryMapping
    public HistorialClinico obtenerHistorialClinico(@Argument Integer id) {
        return historialClinicoService.getById(id);
    }

    @QueryMapping
    public List<HistorialClinico> obtenerHistorialesClinicos() {
        return historialClinicoService.getAll();
    }
}