package com.fiap.spring_mongo_db.service;

import com.fiap.spring_mongo_db.model.Artigo;

import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoService {
    List<Artigo> obterTodos();

    Artigo obterPorCodigo(String codigo);

    Artigo criar(Artigo artigo);

    List<Artigo> obterPorDataMaiorQue(LocalDateTime data);

    List<Artigo> obterPorDataEStatus(LocalDateTime data, Integer status);

    void atualizar(Artigo artigoAtualizado);

    void atualizarArtigoUrl(String id, String novaUrl);

    void deletar(String codigo);

    void deletarArtigo(String codigo);
}
