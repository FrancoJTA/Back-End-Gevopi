package com.example.gevopi_back_end.Service;

import com.example.gevopi_back_end.Entity.Evaluacion;
import com.example.gevopi_back_end.Entity.Universidad;
import com.example.gevopi_back_end.Repository.EvaluacionRepository;
import com.example.gevopi_back_end.Repository.RespuestaRepository;
import com.example.gevopi_back_end.Repository.UniversidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private UniversidadRepository universidadRepository;

    public long evaluacionesCompletas(){
        return evaluacionRepository.findAll().stream()
                .filter(evaluacion -> !respuestaRepository.findByEvaluacionId(evaluacion.getId()).isEmpty())
                .count();
    }

    public List<Evaluacion> obtenerEvaluacionesPorHistorial(Integer historialId) {
        return evaluacionRepository.findAllByHistorialId(historialId);
    }

    public Optional<Evaluacion> obtenerEvaluacionPorId(int id) {
        return evaluacionRepository.findById(id);
    }


    public Evaluacion agregarUniversidadEvaluacion(int idEvaluacion, int idUniversidad) {
        Optional<Evaluacion> optionalEvaluacion = evaluacionRepository.findById(idEvaluacion);
        Optional<Universidad> optionalUniversidad = universidadRepository.findById(idUniversidad);

        if (optionalEvaluacion.isPresent() && optionalUniversidad.isPresent()) {
            Evaluacion evaluacion = optionalEvaluacion.get();
            Universidad universidad = optionalUniversidad.get();
            evaluacion.setUniversidad(universidad);
            return evaluacionRepository.save(evaluacion);
        }

        return null;
    }


    public Evaluacion quitarEvaluacionUniversidad(int idEvaluacion){
        Optional<Evaluacion> optionalEvaluacion = evaluacionRepository.findById(idEvaluacion);
        if(optionalEvaluacion.isPresent()){
            Evaluacion evaluacion = optionalEvaluacion.get();
            evaluacion.setUniversidad(null);
            return evaluacionRepository.save(evaluacion);
        }
        return null;
    }
}
