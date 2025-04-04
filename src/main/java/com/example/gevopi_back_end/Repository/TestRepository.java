package com.example.gevopi_back_end.Repository;

import com.example.gevopi_back_end.Entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Integer> {
}
