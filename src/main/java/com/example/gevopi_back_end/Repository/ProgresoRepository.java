package com.example.gevopi_back_end.Repository;

import com.example.gevopi_back_end.Entity.ProgresoVoluntario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgresoRepository extends JpaRepository<ProgresoVoluntario,Integer> {

    List<ProgresoVoluntario> findByHistorialClinicoId(Integer historialId);

}