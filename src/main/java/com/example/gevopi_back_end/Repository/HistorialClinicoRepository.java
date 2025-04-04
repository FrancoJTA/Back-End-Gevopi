package com.example.gevopi_back_end.Repository;

import com.example.gevopi_back_end.Entity.HistorialClinico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistorialClinicoRepository extends JpaRepository<HistorialClinico, Integer> {
    Optional<HistorialClinico> findByIdVoluntario(Integer voluntarioId);
}
