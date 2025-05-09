package com.example.gevopi_back_end.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import com.example.gevopi_back_end.Entity.Necesidad;
import com.example.gevopi_back_end.Service.NecesidadService;

import java.util.List;
import java.util.Optional;

@Controller
public class NecesidadController {

    @Autowired
    private NecesidadService necesidadService;

    // Obtener todas las necesidades
    @QueryMapping
    public List<Necesidad> obtenerNecesidades() {
        return necesidadService.obtenerNecesidades();
    }

    // Crear una nueva necesidad
    @MutationMapping
    public Necesidad crearNecesidad(@Argument String tipo,@Argument String descripcion) {
        return necesidadService.crearNecesidad(tipo, descripcion);
    }

    // Editar una necesidad
    @MutationMapping
    public Optional<Necesidad> editarNecesidad(@Argument int id,@Argument String tipo,@Argument String descripcion) {
        return necesidadService.editarNecesidad(id, tipo, descripcion);
    }

    // Eliminar una necesidad
    @MutationMapping
    public Boolean eliminarNecesidad(@Argument int id) {
        return necesidadService.eliminarNecesidad(id);
    }
}
