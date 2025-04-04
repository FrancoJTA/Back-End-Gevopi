package com.example.gevopi_back_end.Controller;

import com.example.gevopi_back_end.Entity.Pregunta;
import com.example.gevopi_back_end.Entity.Test;
import com.example.gevopi_back_end.Repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class TestController {

    @Autowired
    private TestRepository testRepository;

    @QueryMapping
    public List<Test> tests() {
        return testRepository.findAll();
    }

    @QueryMapping
    public Optional<Test> test(@Argument int id) {
        return testRepository.findById(id);
    }
}