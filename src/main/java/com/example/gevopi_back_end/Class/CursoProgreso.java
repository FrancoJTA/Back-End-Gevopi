package com.example.gevopi_back_end.Class;

import lombok.Data;

import java.util.List;

@Data
public class CursoProgreso {
    private Integer id;
    private String nombre;
    private List<Etapa> etapas;
    @Data
    public static class Etapa {
        private Integer id;
        private String nombre;
        private Integer orden;
        private String estado;
        private String fechaInicio;
        private String fechaFinalizacion;
    }
}
