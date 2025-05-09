package com.example.gevopi_back_end.Service;

import com.example.gevopi_back_end.Entity.Pregunta;
import com.example.gevopi_back_end.Entity.Test;
import com.example.gevopi_back_end.Repository.PreguntaRepository;
import com.example.gevopi_back_end.Repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PreguntaService {
    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private TestRepository testRepository;

    // Método para actualizar el texto de la pregunta
    public Pregunta actualizarPregunta(int id, String texto) {
        Pregunta pregunta = preguntaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada con id: " + id));
        pregunta.setTexto(texto);
        return preguntaRepository.save(pregunta);
    }

    // Método para agregar una nueva pregunta
    public Pregunta agregarPregunta(int testId, String texto, String tipo) {
        // Buscar el test con el testId
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test no encontrado con id: " + testId));

        // Crear una nueva pregunta
        Pregunta nuevaPregunta = new Pregunta();
        nuevaPregunta.setTest(test);  // Relacionamos la pregunta con el test
        nuevaPregunta.setTexto(texto);
        nuevaPregunta.setTipo(tipo);

        // Guardamos la nueva pregunta en la base de datos
        return preguntaRepository.save(nuevaPregunta);
    }

    // Método para eliminar una pregunta
    public boolean eliminarPregunta(int id) {
        if (preguntaRepository.existsById(id)) {
            preguntaRepository.deleteById(id);
            return true;
        }
        return false;  // Si no existe la pregunta, no se puede eliminar
    }

    public Map<String, List<Pregunta>> obtenerPreguntasPorTests() {
        // Obtener todas las preguntas
        List<Pregunta> preguntas = preguntaRepository.findAll();

        // Crear las dos listas de preguntas para cada test
        List<Pregunta> test1Preguntas = preguntas.stream()
                .filter(p -> p.getTest().getId() == 1) // Filtrar por el testId 1
                .collect(Collectors.toList());

        List<Pregunta> test2Preguntas = preguntas.stream()
                .filter(p -> p.getTest().getId() == 2) // Filtrar por el testId 2
                .collect(Collectors.toList());

        // Crear un mapa con los resultados
        Map<String, List<Pregunta>> resultado = new HashMap<>();
        resultado.put("test1", test1Preguntas);
        resultado.put("test2", test2Preguntas);

        return resultado;
    }
}
