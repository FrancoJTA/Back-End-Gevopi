package com.example.gevopi_back_end.Repository;

import com.example.gevopi_back_end.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByEmailOrCi(String email, String ci);
    Optional<Usuario> findByCi(String ci); // Usa findByCI si el campo en la entidad es "CI"

    boolean existsByEmail(String email);
}