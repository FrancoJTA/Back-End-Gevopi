package com.example.gevopi_back_end.Class;

import com.example.gevopi_back_end.Entity.Reporte;
import lombok.Data;

import java.util.List;


@Data
public class Dashboard {
    private List<Reporte> reportes;
    private int report_cantidad;
    private int eva_cantidad;
    private List<DashCapacitacion> capacitacion;
    private List<DashNecesidades> necesidad;
    private List<DashUniversidades> universidad;

    @Data
    public static class DashCapacitacion {
        private String nombre;
        private int cantidad;
    }

    @Data
    public static class DashNecesidades {
        private String nombre;
        private int cantidad;
    }

    @Data
    public static class DashUniversidades {
        private String nombre;
        private int cantidad;
    }
}