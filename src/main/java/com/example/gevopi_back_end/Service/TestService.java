package com.example.gevopi_back_end.Service;

import com.example.gevopi_back_end.Entity.Pregunta;
import com.example.gevopi_back_end.Repository.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    @Autowired
    private PreguntaRepository preguntaRepository;


    public List<Pregunta> obtenerPreguntasPorTest(Integer testId) {
        return preguntaRepository.findByTestId(testId);
    }

}
