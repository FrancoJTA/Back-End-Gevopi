package com.example.gevopi_back_end.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.gevopi_back_end.Entity.Necesidad;
import com.example.gevopi_back_end.Repository.NecesidadRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NecesidadService {

    @Autowired
    private NecesidadRepository necesidadRepository;

    // Crear una nueva necesidad
    public Necesidad crearNecesidad(String tipo, String descripcion) {
        Necesidad necesidad = new Necesidad();
        necesidad.setTipo(tipo);
        necesidad.setDescripcion(descripcion);
        return necesidadRepository.save(necesidad);
    }

    // Obtener todas las necesidades
    public List<Necesidad> obtenerNecesidades() {
        return necesidadRepository.findAll();
    }

    // Obtener una necesidad por id
    public Optional<Necesidad> obtenerNecesidadPorId(int id) {
        return necesidadRepository.findById(id);
    }

    // Editar una necesidad
    public Optional<Necesidad> editarNecesidad(int id, String tipo, String descripcion) {
        Optional<Necesidad> necesidadOptional = necesidadRepository.findById(id);
        if (necesidadOptional.isPresent()) {
            Necesidad necesidad = necesidadOptional.get();
            if (tipo != null) necesidad.setTipo(tipo);
            if (descripcion != null) necesidad.setDescripcion(descripcion);
            necesidadRepository.save(necesidad);
        }
        return necesidadOptional;
    }

    // Eliminar una necesidad
    public boolean eliminarNecesidad(int id) {
        if (necesidadRepository.existsById(id)) {
            necesidadRepository.deleteById(id);
            return true;
        }
        return false;
    }
}