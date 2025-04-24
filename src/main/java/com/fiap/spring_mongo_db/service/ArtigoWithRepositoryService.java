package com.fiap.spring_mongo_db.service;

import com.fiap.spring_mongo_db.model.Artigo;
import com.fiap.spring_mongo_db.model.Autor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoWithRepositoryService {
    List<Artigo> obterTodos();

    Artigo obterPorCodigo(String codigo);

    Artigo criar(Artigo artigo);

    ResponseEntity<?> criarArtigo(Artigo artigo);

    void atualizar(Artigo artigoAtualizado);

    ResponseEntity<?> atualizarArtigo(String id, Artigo artigo);

    void deletar(String codigo);

    List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);

    List<Artigo> findByDataBetween(LocalDateTime de, LocalDateTime ate);

    Page<Artigo> findAllWithPagination(Pageable pageable);

    List<Artigo> findByStatusOrderByTituloAsc(Integer status);

    List<Artigo> findByStatusOrderByTituloDesc(Integer status);

    Page<Artigo> findAllWithPaginationAndSort(Pageable pageable);

    ResponseEntity<?> criarArtigoComAutor(Artigo artigo, Autor autor);
}
