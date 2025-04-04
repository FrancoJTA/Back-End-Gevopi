package com.example.gevopi_back_end.Repository;

import com.example.gevopi_back_end.Entity.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreguntaRepository extends JpaRepository<Pregunta, Integer> {
    List<Pregunta> findByTestId(Integer testId);
}
