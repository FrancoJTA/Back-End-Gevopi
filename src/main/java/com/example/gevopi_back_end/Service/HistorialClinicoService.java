package com.example.gevopi_back_end.Service;

import com.example.gevopi_back_end.Entity.HistorialClinico;
import com.example.gevopi_back_end.Repository.HistorialClinicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialClinicoService {

    @Autowired
    private HistorialClinicoRepository historialClinicoRepository;

    public HistorialClinico getById(Integer id) {
        return historialClinicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial clínico no encontrado con id: " + id));
    }

    public List<HistorialClinico> getAll() {
        return historialClinicoRepository.findAll();
    }
}
