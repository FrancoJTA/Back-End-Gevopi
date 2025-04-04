package com.example.gevopi_back_end.Controller;


import com.example.gevopi_back_end.Entity.*;
import com.example.gevopi_back_end.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
public class CapacitacionController {
    @Autowired
    private CapacitacionRepository capacitacionRepository;

    @QueryMapping
    public List<Capacitacion> todasLasCapacitaciones() {
        return capacitacionRepository.findAll();
    }
}