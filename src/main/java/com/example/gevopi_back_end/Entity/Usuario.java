package com.example.gevopi_back_end.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String nombre;
    private String apellido;
    private String email;

    @Column(name = "CI", unique = true)
    private String ci;

    @Column(name = "teléfono")
    private String telefono;

    private String password;
    private Boolean activo;


    @ManyToMany
    @JoinTable(
            name = "Usuario_Rol",
            joinColumns = @JoinColumn(name = "ID_Usuario"),
            inverseJoinColumns = @JoinColumn(name = "ID_Rol")
    )
    @JsonIgnoreProperties("")
    private Set<Rol> roles = new HashSet<>();

}