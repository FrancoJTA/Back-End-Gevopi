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
import java.util.function.Function;
import java.util.stream.Collectors;

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
        // Crear y guardar la capacitación primero
        Capacitacion capacitacion = new Capacitacion();
        capacitacion.setNombre(input.getNombre());
        capacitacion.setDescripcion(input.getDescripcion());
        capacitacion = capacitacionRepository.save(capacitacion);

        if (input.getCursos() != null && !input.getCursos().isEmpty()) {
            for (inputCapacitacion.inputCurso inputCurso : input.getCursos()) {
                // Crear curso, asociarlo a la capacitación y guardar primero
                Cursos curso = new Cursos();
                curso.setNombre(inputCurso.getNombre());
                curso.setCapacitacion(capacitacion);
                curso = cursoRepository.save(curso); // Guardar curso sin etapas

                // Crear y guardar cada etapa asociada al curso ya persistido
                if (inputCurso.getEtapas() != null && !inputCurso.getEtapas().isEmpty()) {
                    for (inputCapacitacion.inputCurso.inputEtapaCcapacitacion inputEtapa : inputCurso.getEtapas()) {
                        Etapas etapa = new Etapas();
                        etapa.setNombre(inputEtapa.getNombre());
                        etapa.setOrden(inputEtapa.getOrden());
                        etapa.setCurso(curso); // Asociar etapa al curso ya guardado
                        etapaRepository.save(etapa); // Guardar etapa individualmente
                    }
                }

                // Asociar curso a la capacitación después de guardar
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
        Capacitacion capacitacion = capacitacionRepository.findById(input.getId())
                .orElseThrow(() -> new RuntimeException("Capacitación no encontrada con ID: " + input.getId()));

        if (input.getNombre() != null && !input.getNombre().isEmpty()) {
            capacitacion.setNombre(input.getNombre());
        }

        if (input.getDescripcion() != null && !input.getDescripcion().isEmpty()) {
            capacitacion.setDescripcion(input.getDescripcion());
        }

        if (input.getCursos() != null) {
            Set<Cursos> cursosActualizados = new HashSet<>();

            for (inputCapacitacion.inputCurso inputCurso : input.getCursos()) {
                Cursos curso = cursoRepository.findById(inputCurso.getId()).orElse(null);
                if (curso == null) {
                    curso = new Cursos();
                    curso.setCapacitacion(capacitacion);
                }

                curso.setNombre(inputCurso.getNombre());

                if (inputCurso.getEtapas() != null) {
                    Map<Integer, Etapas> etapasExistentes = curso.getEtapas().stream()
                            .collect(Collectors.toMap(Etapas::getId, Function.identity()));

                    Set<Etapas> etapasFinales = new HashSet<>();

                    for (inputCapacitacion.inputCurso.inputEtapaCcapacitacion inputEtapa : inputCurso.getEtapas()) {
                        Etapas etapa;

                        if (inputEtapa.getId() != 0 && etapasExistentes.containsKey(inputEtapa.getId())) {
                            etapa = etapasExistentes.remove(inputEtapa.getId());
                            etapa.setNombre(inputEtapa.getNombre());
                            etapa.setOrden(inputEtapa.getOrden());
                        } else {
                            etapa = new Etapas();
                            etapa.setNombre(inputEtapa.getNombre());
                            etapa.setOrden(inputEtapa.getOrden());
                            etapa.setCurso(curso);
                        }

                        etapasFinales.add(etapa);
                    }

                    for (Etapas etapaAEliminar : etapasExistentes.values()) {
                        etapaRepository.delete(etapaAEliminar);
                    }

                    curso.getEtapas().clear();                // 🛠 Clave
                    curso.getEtapas().addAll(etapasFinales);  // 🛠 Clave
                }

                cursosActualizados.add(curso);
            }

            capacitacion.getCursos().clear();               // 🛠 Clave
            capacitacion.getCursos().addAll(cursosActualizados); // 🛠 Clave
        }


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
