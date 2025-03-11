package com.fiap.spring_mongo_db.controller;

import com.fiap.spring_mongo_db.model.Artigo;
import com.fiap.spring_mongo_db.service.ArtigoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/artigos")
public class ArtigoController {
    private final ArtigoService service;

    public ArtigoController(ArtigoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Artigo> obterTodos() {
        return service.obterTodos();
    }

    @GetMapping("/{codigo}")
    public Artigo obterPorCodigo(@PathVariable final String codigo) {
        return service.obterPorCodigo(codigo);
    }

    @PostMapping
    public Artigo criar(@RequestBody final Artigo artigo) {
        return service.criar(artigo);
    }

    @GetMapping("/por-data")
    public List<Artigo> obterPorDataMaiorQue(@RequestParam("data") final LocalDateTime data) {
        return service.obterPorDataMaiorQue(data);
    }

    @GetMapping("/por-data-e-status")
    public List<Artigo> obterPorDataEStatus(
            @RequestParam("data") final LocalDateTime data,
            @RequestParam("status") final Integer status
    ) {
        return service.obterPorDataEStatus(data, status);
    }
}
