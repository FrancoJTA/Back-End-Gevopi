package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.Pregunta;
import com.example.gevopi_back_end.Service.PreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class FormularioController {
    @Autowired
    private PreguntaService preguntaService;

    // Mutación para actualizar el texto de la pregunta
    @MutationMapping
    public Pregunta actualizarPregunta(@Argument int id,@Argument String texto) {
        return preguntaService.actualizarPregunta(id, texto);
    }

    // Mutación para agregar una nueva pregunta
    @MutationMapping
    public Pregunta agregarPregunta(@Argument int testId, @Argument String texto, @Argument String tipo) {
        return preguntaService.agregarPregunta(testId, texto, tipo);
    }

    // Mutación para eliminar una pregunta
    @MutationMapping
    public Boolean eliminarPregunta(@Argument int id) {
        return preguntaService.eliminarPregunta(id);
    }

    @QueryMapping
    public Map<String, Object> obtenerPreguntasPorTests() {
        Map<String, List<Pregunta>> preguntasMap = preguntaService.obtenerPreguntasPorTests();

        // Devolver un Map<String, Object> con las colecciones test1 y test2
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("test1", preguntasMap.get("test1"));
        resultado.put("test2", preguntasMap.get("test2"));

        return resultado;
    }
}
