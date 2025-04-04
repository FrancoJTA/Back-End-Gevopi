package com.example.gevopi_back_end.Repository;

import com.example.gevopi_back_end.Entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReporteRepository  extends JpaRepository<Reporte, Integer> {
    List<Reporte> findByHistorialClinicoId(Integer historialId);
}
