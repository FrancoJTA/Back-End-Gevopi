package com.example.gevopi_back_end.Repository;

import com.example.gevopi_back_end.Entity.Necesidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface NecesidadRepository extends JpaRepository<Necesidad, Integer> {


}