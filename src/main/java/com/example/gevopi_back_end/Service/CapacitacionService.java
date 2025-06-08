package com.example.gevopi_back_end.Service;

import com.example.gevopi_back_end.Class.inputCapacitacion;
import com.example.gevopi_back_end.Entity.Capacitacion;
import com.example.gevopi_back_end.Entity.Cursos;
import com.example.gevopi_back_end.Entity.Etapas;
import com.example.gevopi_back_end.Repository.CapacitacionRepository;
import com.example.gevopi_back_end.Repository.CursosRepository;
import com.example.gevopi_back_end.Repository.EtapaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CapacitacionService {
    @Autowired
    private CapacitacionRepository capacitacionRepository;

    @Autowired
    private CursosRepository cursoRepository;

    @Autowired
    private EtapaRepository etapaRepository;

    @Transactional
    public Capacitacion crearCapacitacion(inputCapacitacion input) {
        Capacitacion capacitacion = new Capacitacion();
        capacitacion.setNombre(input.getNombre());
        capacitacion.setDescripcion(input.getDescripcion());

        capacitacion = capacitacionRepository.save(capacitacion);

        if (input.getCursos() != null && !input.getCursos().isEmpty()) {
            for (inputCapacitacion.inputCurso inputCurso : input.getCursos()) {
                Cursos curso = new Cursos();
                curso.setNombre(inputCurso.getNombre());
                curso.setCapacitacion(capacitacion);

                Set<Etapas> etapas = new HashSet<>();
                System.out.println(inputCurso);
                if (inputCurso.getEtapas() != null && !inputCurso.getEtapas().isEmpty()) {
                    for (inputCapacitacion.inputCurso.inputEtapaCcapacitacion inputEtapa : inputCurso.getEtapas()) {
                        Etapas etapa = new Etapas();
                        etapa.setNombre(inputEtapa.getNombre());
                        etapa.setOrden(inputEtapa.getOrden());
                        etapa.setCurso(curso);
                        etapas.add(etapa);
                    }
                }

                curso.setEtapas(etapas);
                cursoRepository.save(curso); // Etapas se deben guardar por cascada
                capacitacion.getCursos().add(curso);
            }
        }

        return capacitacion;
    }

    public List<Capacitacion> obtenerCapacitaciones() {
        return capacitacionRepository.findAll();
    }

    public Optional<Capacitacion> obtenerCapacitacionPorId(int id) {
        return capacitacionRepository.findById(id);
    }

    @Transactional
    public Capacitacion editarCapacitacion(inputCapacitacion input) {
        // Buscar la capacitación por ID
        Capacitacion capacitacion = capacitacionRepository.findById(input.getId())
                .orElseThrow(() -> new RuntimeException("Capacitación no encontrada con ID: " + input.getId()));

        // Editar los campos básicos de la capacitación si no son nulos
        if (input.getNombre() != null && !input.getNombre().isEmpty()) {
            capacitacion.setNombre(input.getNombre());
        }
        if (input.getDescripcion() != null && !input.getDescripcion().isEmpty()) {
            capacitacion.setDescripcion(input.getDescripcion());
        }

        // Procesar los cursos y sus etapas
        if (input.getCursos() != null) {
            for (inputCapacitacion.inputCurso inputCurso : input.getCursos()) {
                // Verificar si el curso ya existe
                Optional<Cursos> cursoOptional = cursoRepository.findById(inputCurso.getId());
                Cursos curso;
                if (cursoOptional.isPresent()) {
                    curso = cursoOptional.get();
                    // Si es necesario, actualizamos el nombre del curso
                    if (inputCurso.getNombre() != null && !inputCurso.getNombre().isEmpty()) {
                        curso.setNombre(inputCurso.getNombre());
                    }
                } else {
                    // Si el curso no existe, creamos uno nuevo
                    curso = new Cursos();
                    curso.setNombre(inputCurso.getNombre());
                    curso.setCapacitacion(capacitacion);
                    capacitacion.getCursos().add(curso);  // Agregar el curso a la capacitación
                }

                // Procesar las etapas para este curso
                for (inputCapacitacion.inputCurso.inputEtapaCcapacitacion inputEtapa : inputCurso.getEtapas()) {
                    Optional<Etapas> etapaOptional = etapaRepository.findById(inputEtapa.getId());
                    Etapas etapa;

                    if (etapaOptional.isPresent()) {
                        etapa = etapaOptional.get();
                        // Si la etapa existe, actualizar su nombre y orden
                        if (inputEtapa.getNombre() != null && !inputEtapa.getNombre().isEmpty()) {
                            etapa.setNombre(inputEtapa.getNombre());
                        }
                        if (inputEtapa.getOrden() != etapa.getOrden()) {
                            etapa.setOrden(inputEtapa.getOrden());
                        }
                    } else {
                        // Si la etapa no existe, creamos una nueva
                        etapa = new Etapas();
                        etapa.setNombre(inputEtapa.getNombre());
                        etapa.setOrden(inputEtapa.getOrden());
                        etapa.setCurso(curso);
                        curso.getEtapas().add(etapa);  // Agregar la etapa al curso
                    }
                }
            }
        }

        // Guardar la capacitación actualizada (incluye cursos y etapas modificados)
        return capacitacionRepository.save(capacitacion);
    }

    public boolean eliminarCapacitacion(int id) {
        if (capacitacionRepository.existsById(id)) {
            capacitacionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
