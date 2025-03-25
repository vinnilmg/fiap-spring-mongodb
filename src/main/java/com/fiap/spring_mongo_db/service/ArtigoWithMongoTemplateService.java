package com.fiap.spring_mongo_db.service;

import com.fiap.spring_mongo_db.model.Artigo;
import com.fiap.spring_mongo_db.model.ArtigoStatusCount;
import com.fiap.spring_mongo_db.model.ArtigosPorAutorCount;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoWithMongoTemplateService {
    List<Artigo> obterPorDataMaiorQue(LocalDateTime data);

    List<Artigo> obterPorDataEStatus(LocalDateTime data, Integer status);

    @Transactional
    void atualizarArtigoUrl(String id, String novaUrl);

    @Transactional
    void deletarArtigo(String codigo);

    List<Artigo> encontrarArtigosComplexos(
            Integer status,
            LocalDateTime data,
            String titulo
    );

    List<Artigo> findByTexto(String searchTerm);

    List<ArtigoStatusCount> contarArtigosPorStatus();

    List<ArtigosPorAutorCount> contarArtigosPorAutorNoPeriodo(
            LocalDate dataInicio,
            LocalDate dataFim
    );
}
