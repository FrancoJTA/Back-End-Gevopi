package com.example.gevopi_back_end.Class;
import lombok.Data;

import java.util.List;

@Data
public class inputCapacitacion {
    private String nombre;
    private String descripcion;
    private List<inputCurso> cursos;
    @Data
    public static class inputCurso {
        private String nombre;
        private List<inputEtapaCcapacitacion> etapas;
        @Data
        public static class inputEtapaCcapacitacion {
            private String nombre;
            private int orden;
        }
    }

}

