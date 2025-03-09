package com.fiap.spring_mongo_db.service;

import com.fiap.spring_mongo_db.model.Artigo;

import java.util.List;

public interface ArtigoService {

    List<Artigo> obterTodos();

    Artigo obterPorCodigo(String codigo);

    Artigo criar(Artigo artigo);

}
