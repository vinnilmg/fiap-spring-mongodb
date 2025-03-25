package com.fiap.spring_mongo_db.service.impl;

import com.fiap.spring_mongo_db.model.Artigo;
import com.fiap.spring_mongo_db.repository.ArtigoRepository;
import com.fiap.spring_mongo_db.repository.AutorRepository;
import com.fiap.spring_mongo_db.service.ArtigoWithRepositoryService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class ArtigoWithRepositoryServiceImpl implements ArtigoWithRepositoryService {
    private final ArtigoRepository artigoRepository;
    private final AutorRepository autorRepository;

    public ArtigoWithRepositoryServiceImpl(
            ArtigoRepository artigoRepository,
            AutorRepository autorRepository
    ) {
        this.artigoRepository = artigoRepository;
        this.autorRepository = autorRepository;
    }

    @Override
    public List<Artigo> obterTodos() {
        return artigoRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Artigo obterPorCodigo(final String codigo) {
        return artigoRepository.findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Artigo não existe"));
    }

    @Transactional
    @Override
    public Artigo criar(final Artigo artigo) {
        if (nonNull(artigo.getAutor().codigo())) {
            final var autor = autorRepository.findById(artigo.getAutor().codigo())
                    .orElseThrow(() -> new IllegalArgumentException("Autor nao existe"));

            artigo.setAutor(autor);
        } else {
            artigo.setAutor(null);
        }

        return artigoRepository.save(artigo);
    }

    @Override
    public ResponseEntity<?> criarArtigo(final Artigo artigo) {
        if (nonNull(artigo.getAutor().codigo())) {
            final var autor = autorRepository.findById(artigo.getAutor().codigo())
                    .orElseThrow(() -> new IllegalArgumentException("Autor nao existe"));

            artigo.setAutor(autor);
        } else {
            artigo.setAutor(null);
        }

        try {
            artigoRepository.save(artigo);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .build();
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Artigo já existe");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao criar artigo: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void atualizar(final Artigo artigoAtualizado) {
        try {
            artigoRepository.save(artigoAtualizado);
        } catch (OptimisticLockingFailureException e) {
            final var artigoExistente = artigoRepository.findById(artigoAtualizado.getCodigo())
                    .orElseThrow(() -> new IllegalArgumentException("Artigo não localizado: " + artigoAtualizado.getCodigo()));

            artigoExistente.setTitulo(artigoAtualizado.getTitulo());
            artigoExistente.setTexto(artigoAtualizado.getTexto());
            artigoExistente.setStatus(artigoAtualizado.getStatus());
            artigoExistente.setVersion(artigoExistente.getVersion() + 1);
            artigoRepository.save(artigoAtualizado);
        }
    }

    @Transactional
    @Override
    public void deletar(final String codigo) {
        artigoRepository.deleteById(codigo);
    }

    @Override
    public List<Artigo> findByStatusAndDataGreaterThan(final Integer status, final LocalDateTime data) {
        return artigoRepository.findByStatusAndDataGreaterThan(status, data);
    }

    @Override
    public List<Artigo> findByDataBetween(final LocalDateTime de, final LocalDateTime ate) {
        return artigoRepository.findByDataBetween(de, ate);
    }

    @Override
    public Page<Artigo> findAllWithPagination(final Pageable pageable) {
        return artigoRepository.findAll(pageable);
    }

    @Override
    public List<Artigo> findByStatusOrderByTituloAsc(final Integer status) {
        return artigoRepository.findByStatusOrderByTituloAsc(status);
    }

    @Override
    public List<Artigo> findByStatusOrderByTituloDesc(Integer status) {
        return artigoRepository.findByStatusOrderByTituloDesc(status);
    }

    @Override
    public Page<Artigo> findAllWithPaginationAndSort(final Pageable pageable) {
        final var sort = Sort.by("titulo").ascending();
        final var pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return artigoRepository.findAll(pageableWithSort);
    }
}
