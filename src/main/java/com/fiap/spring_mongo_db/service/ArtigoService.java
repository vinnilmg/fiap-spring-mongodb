package com.fiap.spring_mongo_db.service;

import com.fiap.spring_mongo_db.model.Artigo;
import com.fiap.spring_mongo_db.model.ArtigoStatusCount;
import com.fiap.spring_mongo_db.model.ArtigosPorAutorCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
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

    List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);

    List<Artigo> findByDataBetween(LocalDateTime de, LocalDateTime ate);

    List<Artigo> encontrarArtigosComplexos(Integer status, LocalDateTime data, String titulo);

    Page<Artigo> findAllWithPagination(Pageable pageable);

    List<Artigo> findByStatusOrderByTituloAsc(Integer status);

    List<Artigo> findByStatusOrderByTituloDesc(Integer status);

    Page<Artigo> findAllWithPaginationAndSort(Pageable pageable);

    List<Artigo> findByTexto(String searchTerm);

    List<ArtigoStatusCount> contarArtigosPorStatus();

    List<ArtigosPorAutorCount> contarArtigosPorAutorNoPeriodo(LocalDate dataInicio, LocalDate dataFim);
}
