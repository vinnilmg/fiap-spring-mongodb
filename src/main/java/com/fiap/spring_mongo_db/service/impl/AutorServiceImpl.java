package com.fiap.spring_mongo_db.service.impl;

import com.fiap.spring_mongo_db.model.Autor;
import com.fiap.spring_mongo_db.repository.AutorRepository;
import com.fiap.spring_mongo_db.service.AutorService;
import org.springframework.stereotype.Service;

@Service
public class AutorServiceImpl implements AutorService {
    private final AutorRepository repository;

    public AutorServiceImpl(AutorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Autor obterPorCodigo(final String codigo) {
        return repository.findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Artigo nao existe"));
    }

    @Override
    public Autor criar(final Autor autor) {
        return repository.save(autor);
    }
}
