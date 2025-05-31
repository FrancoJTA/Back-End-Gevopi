package com.example.gevopi_back_end.Service;

import com.example.gevopi_back_end.Entity.Capacitacion;
import com.example.gevopi_back_end.Entity.Universidad;
import com.example.gevopi_back_end.Repository.UniversidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UniversidadService {
    @Autowired
    private UniversidadRepository universidadRepository;

    public List<Universidad> obtenerUniversidades() {
        return universidadRepository.findAll();
    }

    public Universidad crearUniversidad(Universidad universidad) {
        return universidadRepository.save(universidad);
    }

    public Universidad editarUniversidad(Universidad universidad){
        Optional<Universidad> universidadFind = universidadRepository.findById(universidad.getId());
        if (universidadFind.isPresent()) {
            Universidad universidadExistente = universidadFind.get();

            if (universidad.getNombre() != null && !universidad.getNombre().isBlank()) {
                universidadExistente.setNombre(universidad.getNombre());
            }
            if (universidad.getDireccion() != null && !universidad.getDireccion().isBlank()) {
                universidadExistente.setDireccion(universidad.getDireccion());
            }
            if (universidad.getTelefono() != null && !universidad.getTelefono().isBlank()) {
                universidadExistente.setTelefono(universidad.getTelefono());
            }

            return universidadRepository.save(universidadExistente);
        }
        return null;
    }

    public boolean eliminarUniversidad(int id){
        Optional<Universidad> universidadFind = universidadRepository.findById(id);
        if (universidadFind.isPresent()) {
            universidadRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
