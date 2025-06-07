package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Class.CursoProgreso;
import com.example.gevopi_back_end.Entity.Cursos;
import com.example.gevopi_back_end.Service.CursosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CursoController {
    @Autowired
    private CursosService cursosService;

    @QueryMapping
    public List<CursoProgreso> obtenerCursosVoluntario(@Argument int id) {
        return cursosService.obtenerCursosYEtapasConProgreso(id);
    }
}
