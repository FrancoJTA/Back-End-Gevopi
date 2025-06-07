package com.example.gevopi_back_end.Service;

import com.example.gevopi_back_end.Class.inputCapacitacion;
import com.example.gevopi_back_end.Entity.Capacitacion;
import com.example.gevopi_back_end.Entity.Cursos;
import com.example.gevopi_back_end.Entity.Etapas;
import com.example.gevopi_back_end.Repository.CapacitacionRepository;
import com.example.gevopi_back_end.Repository.CursosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CapacitacionService {
    @Autowired
    private CapacitacionRepository capacitacionRepository;

    @Autowired
    private CursosRepository cursoRepository;

    public Capacitacion crearCapacitacion(inputCapacitacion input) {
        // Crear la entidad de Capacitacion
        Capacitacion capacitacion = new Capacitacion();
        capacitacion.setNombre(input.getNombre());
        capacitacion.setDescripcion(input.getDescripcion());

        // Si la lista de cursos no está vacía, crear y asociar los cursos
        if (input.getCursos() != null && !input.getCursos().isEmpty()) {
            for (inputCapacitacion.inputCurso inputCurso : input.getCursos()) {
                Cursos curso = new Cursos();
                curso.setNombre(inputCurso.getNombre());
                curso.setCapacitacion(capacitacion); // Asociar el curso a la capacitación

                // Si se incluyen etapas, crear y asociar las etapas al curso
                if (inputCurso.getEtapas() != null && !inputCurso.getEtapas().isEmpty()) {
                    for (inputCapacitacion.inputCurso.inputEtapaCcapacitacion inputEtapa : inputCurso.getEtapas()) {
                        Etapas etapa = new Etapas();
                        etapa.setNombre(inputEtapa.getNombre());
                        etapa.setOrden(inputEtapa.getOrden());
                        etapa.setCurso(curso); // Asociar la etapa al curso
                        curso.getEtapas().add(etapa);
                    }
                }

                cursoRepository.save(curso);
                capacitacion.getCursos().add(curso);
            }
        }

        return capacitacionRepository.save(capacitacion);
    }

    public List<Capacitacion> obtenerCapacitaciones() {
        return capacitacionRepository.findAll();
    }

    public Optional<Capacitacion> obtenerCapacitacionPorId(int id) {
        return capacitacionRepository.findById(id);
    }

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

    public boolean eliminarCapacitacion(int id) {
        if (capacitacionRepository.existsById(id)) {
            capacitacionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
