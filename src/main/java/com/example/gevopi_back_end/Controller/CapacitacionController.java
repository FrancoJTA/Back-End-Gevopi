package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.Capacitacion;
import com.example.gevopi_back_end.Class.inputCapacitacion;
import com.example.gevopi_back_end.Service.CapacitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class CapacitacionController {

    @Autowired
    private CapacitacionService capacitacionService;

    @QueryMapping
    public List<Capacitacion> obtenerCapacitaciones() {
        return capacitacionService.obtenerCapacitaciones();
    }


    @MutationMapping
    public Capacitacion crearCapacitacion(@Argument("input") inputCapacitacion inputCapacitacion ) {
        return capacitacionService.crearCapacitacion(inputCapacitacion);
    }

    @MutationMapping
    public Capacitacion editarCapacitacion(@Argument("input") inputCapacitacion inputCapacitacion ) {
        return capacitacionService.editarCapacitacion(inputCapacitacion);
    }

    @MutationMapping
    public Boolean eliminarCapacitacion(@Argument int id) {
        return capacitacionService.eliminarCapacitacion(id);
    }
}
