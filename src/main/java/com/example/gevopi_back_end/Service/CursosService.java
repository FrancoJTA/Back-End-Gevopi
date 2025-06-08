package com.example.gevopi_back_end.Service;

import com.example.gevopi_back_end.Class.CursoProgreso;
import com.example.gevopi_back_end.Entity.Cursos;
import com.example.gevopi_back_end.Entity.Etapas;
import com.example.gevopi_back_end.Entity.ProgresoVoluntario;
import com.example.gevopi_back_end.Repository.CursosRepository;
import com.example.gevopi_back_end.Repository.ProgresoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CursosService {
    @Autowired
    private CursosRepository cursoRepository;
    @Autowired
    private ProgresoRepository progresoRepository;

    public List<CursoProgreso> obtenerCursosYEtapasConProgreso(Integer historialId) {
        List<ProgresoVoluntario> progresos = progresoRepository.findByHistorialClinicoId(historialId);

        List<CursoProgreso> cursosYProgresos = new ArrayList<>();

        for (ProgresoVoluntario progreso : progresos) {
            Etapas etapa = progreso.getEtapa();
            Cursos curso = etapa.getCurso();

            Optional<CursoProgreso> cursoProgresoOptional = cursosYProgresos.stream()
                    .filter(c -> c.getNombre().equals(curso.getNombre()))
                    .findFirst();
            if (!cursoProgresoOptional.isPresent()) {
                CursoProgreso cursoProgreso = new CursoProgreso();
                cursoProgreso.setId(curso.getId());
                cursoProgreso.setNombre(curso.getNombre());
                cursoProgreso.setEtapas(new ArrayList<>());
                cursosYProgresos.add(cursoProgreso);
                cursoProgresoOptional = Optional.of(cursoProgreso);
            }
            CursoProgreso cursoProgreso = cursoProgresoOptional.get();


            CursoProgreso.Etapa etapaDTO = new CursoProgreso.Etapa();
            etapaDTO.setId(progreso.getId());
            etapaDTO.setNombre(etapa.getNombre());
            etapaDTO.setOrden(etapa.getOrden());
            etapaDTO.setEstado(progreso.getEstado());
            etapaDTO.setFechaInicio(progreso.getFechaInicio().toString());
            etapaDTO.setFechaFinalizacion(progreso.getFechaFinalizacion() != null ? progreso.getFechaFinalizacion().toString() : null);
            cursoProgreso.getEtapas().add(etapaDTO);

        }
        return cursosYProgresos;
    }

    public Boolean cambiarEstadoCurso(int id) {
        Optional<ProgresoVoluntario> progresoVoluntario = progresoRepository.findById(id);

        if (progresoVoluntario.isEmpty()) {
            return false; // No existe
        }

        ProgresoVoluntario progreso = progresoVoluntario.get();
        LocalDateTime now = LocalDateTime.now();

        switch (progreso.getEstado()) {
            case "No Empezado" -> {
                progreso.setEstado("En Progreso");
                progreso.setFechaInicio(now);
            }
            case "En Progreso" -> {
                progreso.setEstado("Completado");
                progreso.setFechaFinalizacion(now);
            }
            default -> {
                return false;
            }
        }

        progresoRepository.save(progreso); // 🧠 importante: guardar los cambios
        return true;
    }
}
