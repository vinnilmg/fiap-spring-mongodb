package com.fiap.spring_mongo_db.controller;

import com.fiap.spring_mongo_db.model.Autor;
import com.fiap.spring_mongo_db.service.AutorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autores")
public class AutorController {
    private final AutorService service;

    public AutorController(AutorService service) {
        this.service = service;
    }

    @GetMapping("/{codigo}")
    public Autor obterPorCodigo(@PathVariable final String codigo) {
        return service.obterPorCodigo(codigo);
    }

    @PostMapping
    public Autor criar(@RequestBody final Autor autor) {
        return service.criar(autor);
    }
}
