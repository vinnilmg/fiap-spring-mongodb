package com.fiap.spring_mongo_db.controller;

import com.fiap.spring_mongo_db.model.Artigo;
import com.fiap.spring_mongo_db.model.ArtigoStatusCount;
import com.fiap.spring_mongo_db.model.ArtigosPorAutorCount;
import com.fiap.spring_mongo_db.service.ArtigoService;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/artigos")
public class ArtigoController {
    private final ArtigoService service;

    public ArtigoController(ArtigoService service) {
        this.service = service;
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLockingFailureException(
            final OptimisticLockingFailureException exception
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Erro de concorrência: O artigo está sendo utilizado por outro usuário");
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

    @PutMapping
    public void atualizar(@RequestBody final Artigo artigo) {
        service.atualizar(artigo);
    }

    @PutMapping("/{codigo}")
    public void atualizarUrl(@PathVariable final String codigo, @RequestBody final String url) {
        service.atualizarArtigoUrl(codigo, url);
    }

    @DeleteMapping("/{codigo}")
    public void deletar(@PathVariable final String codigo) {
        service.deletar(codigo);
    }

    @DeleteMapping("/delete/{codigo}")
    public void deletarArtigo(@PathVariable final String codigo) {
        service.deletarArtigo(codigo);
    }

    @GetMapping("/por-status-e-data-maior")
    public List<Artigo> findByStatusAndDataGreatherThan(
            @RequestParam("data") final LocalDateTime data,
            @RequestParam("status") final Integer status
    ) {
        return service.findByStatusAndDataGreaterThan(status, data);
    }

    @GetMapping("/por-periodo")
    public List<Artigo> findByDataBetween(
            @RequestParam("de") final LocalDateTime de,
            @RequestParam("ate") final LocalDateTime ate
    ) {
        return service.findByDataBetween(de, ate);
    }

    @GetMapping("/artigo-complexo")
    public List<Artigo> encontrarArtigosComplexos(
            @RequestParam(required = false) final Integer status,
            @RequestParam final LocalDateTime data,
            @RequestParam(required = false) final String titulo
    ) {
        return service.encontrarArtigosComplexos(status, data, titulo);
    }

    @GetMapping("/paginacao")
    public ResponseEntity<Page<Artigo>> findAllWithPagination(final Pageable pageable) {
        final var artigos = service.findAllWithPagination(pageable);
        return ResponseEntity.ok(artigos);
    }

    @GetMapping("/ordenado-por-titulo")
    public List<Artigo> findByStatusOrderByTituloAsc(@RequestParam(required = false) final Integer status) {
        return service.findByStatusOrderByTituloAsc(status);
    }

    @GetMapping("/ordenado-por-titulo-desc")
    public List<Artigo> findByStatusOrderByTituloDesc(@RequestParam(required = false) final Integer status) {
        return service.findByStatusOrderByTituloDesc(status);
    }

    @GetMapping("/paginacao-e-ordenacao")
    public ResponseEntity<Page<Artigo>> findAllWithPaginationAndSort(final Pageable pageable) {
        final var artigos = service.findAllWithPaginationAndSort(pageable);
        return ResponseEntity.ok(artigos);
    }

    @GetMapping("/termo")
    public List<Artigo> findByTexto(@RequestParam final String searchTerm) {
        return service.findByTexto(searchTerm);
    }

    @GetMapping("/contar-por-status")
    public List<ArtigoStatusCount> contarArtigosPorStatus() {
        return service.contarArtigosPorStatus();
    }

    @GetMapping("/contar-por-autor-no-periodo")
    public List<ArtigosPorAutorCount> contarArtigosPorAutorNoPeriodo(
            @RequestParam final LocalDate dataInicio,
            @RequestParam final LocalDate dataFim
    ) {
        return service.contarArtigosPorAutorNoPeriodo(dataInicio, dataFim);
    }
}
