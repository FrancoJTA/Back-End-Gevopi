package com.example.gevopi_back_end.Class;

import lombok.Data;

@Data
public class Acceso {
    public String token;
    public int idUsuario;
    public boolean acceso;
}
