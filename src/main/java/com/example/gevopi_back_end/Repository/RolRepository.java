package com.example.gevopi_back_end.Repository;

import com.example.gevopi_back_end.Entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findById(Integer id);
    Optional<Rol> findByNombre(String nombre);
}
