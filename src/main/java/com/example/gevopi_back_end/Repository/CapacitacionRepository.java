package com.example.gevopi_back_end.Repository;

import com.example.gevopi_back_end.Entity.Capacitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CapacitacionRepository extends JpaRepository<Capacitacion, Integer> {
}
