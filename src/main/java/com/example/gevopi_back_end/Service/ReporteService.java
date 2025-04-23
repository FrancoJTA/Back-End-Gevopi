package com.example.gevopi_back_end.Service;

import com.example.gevopi_back_end.Entity.Reporte;
import com.example.gevopi_back_end.Repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    public List<Reporte> getReportesByHistorialClinicoId(Integer idHistorial) {
        return reporteRepository.findByHistorialClinico_Id(idHistorial);
    }
}
