package com.fiap.spring_mongo_db.service;

import com.fiap.spring_mongo_db.model.Autor;

public interface AutorService {
    Autor obterPorCodigo(String codigo);

    Autor criar(Autor autor);
}
