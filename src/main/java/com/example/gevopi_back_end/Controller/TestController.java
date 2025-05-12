package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.Pregunta;
import com.example.gevopi_back_end.Service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class TestController {
    @Autowired
    private TestService testService;


    @QueryMapping
    public List<Pregunta> preguntasPorTest(@Argument Integer testId) {
        return testService.obtenerPreguntasPorTest(testId);
    }
}
