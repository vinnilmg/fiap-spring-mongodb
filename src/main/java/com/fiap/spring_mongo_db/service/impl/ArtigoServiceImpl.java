package com.fiap.spring_mongo_db.service.impl;

import com.fiap.spring_mongo_db.model.Artigo;
import com.fiap.spring_mongo_db.repository.ArtigoRepository;
import com.fiap.spring_mongo_db.service.ArtigoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtigoServiceImpl implements ArtigoService {
    private final ArtigoRepository repository;

    public ArtigoServiceImpl(ArtigoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Artigo> obterTodos() {
        return repository.findAll();
    }

    @Override
    public Artigo obterPorCodigo(final String codigo) {
        return repository.findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Artigo não existe"));
    }

    @Override
    public Artigo criar(final Artigo artigo) {
        return repository.save(artigo);
    }
}
