package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.Capacitacion;
import com.example.gevopi_back_end.Service.CapacitacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class CapacitacionController {

    @Autowired
    private CapacitacionService capacitacionService;

    // Obtener todas las capacitaciones
    @QueryMapping
    public List<Capacitacion> obtenerCapacitaciones() {
        return capacitacionService.obtenerCapacitaciones();
    }


    // Crear una nueva capacitación
    @MutationMapping
    public Capacitacion crearCapacitacion(@Argument String nombre,@Argument String descripcion) {
        return capacitacionService.crearCapacitacion(nombre, descripcion);
    }

    // Editar una capacitación
    @MutationMapping
    public Optional<Capacitacion> editarCapacitacion(@Argument int id,@Argument String nombre,@Argument String descripcion) {
        return capacitacionService.editarCapacitacion(id, nombre, descripcion);
    }

    // Eliminar una capacitación
    @MutationMapping
    public Boolean eliminarCapacitacion(@Argument int id) {
        return capacitacionService.eliminarCapacitacion(id);
    }
}
