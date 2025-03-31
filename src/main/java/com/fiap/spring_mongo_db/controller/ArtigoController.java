package com.fiap.spring_mongo_db.controller;

import com.fiap.spring_mongo_db.model.Artigo;
import com.fiap.spring_mongo_db.model.ArtigoStatusCount;
import com.fiap.spring_mongo_db.model.ArtigosPorAutorCount;
import com.fiap.spring_mongo_db.service.ArtigoWithMongoTemplateService;
import com.fiap.spring_mongo_db.service.ArtigoWithRepositoryService;
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
    private final ArtigoWithRepositoryService artigoWithRepositoryService;
    private final ArtigoWithMongoTemplateService artigoWithMongoTemplateService;

    public ArtigoController(
            ArtigoWithRepositoryService artigoWithRepositoryService,
            ArtigoWithMongoTemplateService artigoWithMongoTemplateService
    ) {
        this.artigoWithRepositoryService = artigoWithRepositoryService;
        this.artigoWithMongoTemplateService = artigoWithMongoTemplateService;
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
        return artigoWithRepositoryService.obterTodos();
    }

    @GetMapping("/{codigo}")
    public Artigo obterPorCodigo(@PathVariable final String codigo) {
        return artigoWithRepositoryService.obterPorCodigo(codigo);
    }

    @PostMapping
    public Artigo criar(@RequestBody final Artigo artigo) {
        return artigoWithRepositoryService.criar(artigo);
    }

    @PostMapping("/criar")
    public ResponseEntity<?> criarArtigo(@RequestBody final Artigo artigo) {
        return artigoWithRepositoryService.criarArtigo(artigo);
    }

    @GetMapping("/por-data")
    public List<Artigo> obterPorDataMaiorQue(@RequestParam("data") final LocalDateTime data) {
        return artigoWithMongoTemplateService.obterPorDataMaiorQue(data);
    }

    @GetMapping("/por-data-e-status")
    public List<Artigo> obterPorDataEStatus(
            @RequestParam("data") final LocalDateTime data,
            @RequestParam("status") final Integer status
    ) {
        return artigoWithMongoTemplateService.obterPorDataEStatus(data, status);
    }

    @PutMapping
    public void atualizar(@RequestBody final Artigo artigo) {
        artigoWithRepositoryService.atualizar(artigo);
    }

    @PutMapping("/atualizar/{codigo}")
    public ResponseEntity<?> atualizarArtigo(
            @PathVariable final String codigo,
            @RequestBody final Artigo artigo
    ) {
        return artigoWithRepositoryService.atualizarArtigo(codigo, artigo);
    }

    @PutMapping("/{codigo}")
    public void atualizarUrl(@PathVariable final String codigo, @RequestBody final String url) {
        artigoWithMongoTemplateService.atualizarArtigoUrl(codigo, url);
    }

    @DeleteMapping("/{codigo}")
    public void deletar(@PathVariable final String codigo) {
        artigoWithRepositoryService.deletar(codigo);
    }

    @DeleteMapping("/delete/{codigo}")
    public void deletarArtigo(@PathVariable final String codigo) {
        artigoWithMongoTemplateService.deletarArtigo(codigo);
    }

    @GetMapping("/por-status-e-data-maior")
    public List<Artigo> findByStatusAndDataGreatherThan(
            @RequestParam("data") final LocalDateTime data,
            @RequestParam("status") final Integer status
    ) {
        return artigoWithRepositoryService.findByStatusAndDataGreaterThan(status, data);
    }

    @GetMapping("/por-periodo")
    public List<Artigo> findByDataBetween(
            @RequestParam("de") final LocalDateTime de,
            @RequestParam("ate") final LocalDateTime ate
    ) {
        return artigoWithRepositoryService.findByDataBetween(de, ate);
    }

    @GetMapping("/artigo-complexo")
    public List<Artigo> encontrarArtigosComplexos(
            @RequestParam(required = false) final Integer status,
            @RequestParam final LocalDateTime data,
            @RequestParam(required = false) final String titulo
    ) {
        return artigoWithMongoTemplateService.encontrarArtigosComplexos(status, data, titulo);
    }

    @GetMapping("/paginacao")
    public ResponseEntity<Page<Artigo>> findAllWithPagination(final Pageable pageable) {
        final var artigos = artigoWithRepositoryService.findAllWithPagination(pageable);
        return ResponseEntity.ok(artigos);
    }

    @GetMapping("/ordenado-por-titulo")
    public List<Artigo> findByStatusOrderByTituloAsc(@RequestParam(required = false) final Integer status) {
        return artigoWithRepositoryService.findByStatusOrderByTituloAsc(status);
    }

    @GetMapping("/ordenado-por-titulo-desc")
    public List<Artigo> findByStatusOrderByTituloDesc(@RequestParam(required = false) final Integer status) {
        return artigoWithRepositoryService.findByStatusOrderByTituloDesc(status);
    }

    @GetMapping("/paginacao-e-ordenacao")
    public ResponseEntity<Page<Artigo>> findAllWithPaginationAndSort(final Pageable pageable) {
        final var artigos = artigoWithRepositoryService.findAllWithPaginationAndSort(pageable);
        return ResponseEntity.ok(artigos);
    }

    @GetMapping("/termo")
    public List<Artigo> findByTexto(@RequestParam final String searchTerm) {
        return artigoWithMongoTemplateService.findByTexto(searchTerm);
    }

    @GetMapping("/contar-por-status")
    public List<ArtigoStatusCount> contarArtigosPorStatus() {
        return artigoWithMongoTemplateService.contarArtigosPorStatus();
    }

    @GetMapping("/contar-por-autor-no-periodo")
    public List<ArtigosPorAutorCount> contarArtigosPorAutorNoPeriodo(
            @RequestParam final LocalDate dataInicio,
            @RequestParam final LocalDate dataFim
    ) {
        return artigoWithMongoTemplateService.contarArtigosPorAutorNoPeriodo(dataInicio, dataFim);
    }
}
