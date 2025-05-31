package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.Evaluacion;
import com.example.gevopi_back_end.Entity.Universidad;
import com.example.gevopi_back_end.Entity.Usuario;
import com.example.gevopi_back_end.Service.UniversidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UniversidadController {
    @Autowired
    private UniversidadService universidadService;

    @QueryMapping
    public List<Universidad> obtnenerUniversidades() {
        return universidadService.obtenerUniversidades();
    }

    @MutationMapping
    public Universidad agregarUniversidad(@Argument("input") Universidad input) {
        return universidadService.crearUniversidad(input);
    }

    @MutationMapping
    public Universidad actualizarUniversidad(@Argument("input") Universidad input) {
        return universidadService.editarUniversidad(input);
    }

    @MutationMapping
    public Boolean eliminarUniversidad(@Argument int id) {
        return universidadService.eliminarUniversidad(id);
    }

}