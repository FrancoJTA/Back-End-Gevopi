package com.example.gevopi_back_end.Service;

import com.example.gevopi_back_end.Entity.Capacitacion;
import com.example.gevopi_back_end.Repository.CapacitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CapacitacionService {
    @Autowired
    private CapacitacionRepository capacitacionRepository;

    // Crear una nueva capacitación
    public Capacitacion crearCapacitacion(String nombre, String descripcion) {
        Capacitacion capacitacion = new Capacitacion();
        capacitacion.setNombre(nombre);
        capacitacion.setDescripcion(descripcion);
        return capacitacionRepository.save(capacitacion);
    }

    // Obtener todas las capacitaciones
    public List<Capacitacion> obtenerCapacitaciones() {
        return capacitacionRepository.findAll();
    }

    // Obtener una capacitación por id
    public Optional<Capacitacion> obtenerCapacitacionPorId(int id) {
        return capacitacionRepository.findById(id);
    }

    // Editar una capacitación
    public Optional<Capacitacion> editarCapacitacion(int id, String nombre, String descripcion) {
        Optional<Capacitacion> capacitacionOptional = capacitacionRepository.findById(id);
        if (capacitacionOptional.isPresent()) {
            Capacitacion capacitacion = capacitacionOptional.get();
            if (nombre != null) capacitacion.setNombre(nombre);
            if (descripcion != null) capacitacion.setDescripcion(descripcion);
            capacitacionRepository.save(capacitacion);
        }
        return capacitacionOptional;
    }

    // Eliminar una capacitación
    public boolean eliminarCapacitacion(int id) {
        if (capacitacionRepository.existsById(id)) {
            capacitacionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
