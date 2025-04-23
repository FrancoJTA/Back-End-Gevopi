package com.example.gevopi_back_end.Repository;

import com.example.gevopi_back_end.Entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReporteRepository  extends JpaRepository<Reporte, Integer> {


    List<Reporte> findByHistorialClinico_Id(Integer idHistorial);
}
