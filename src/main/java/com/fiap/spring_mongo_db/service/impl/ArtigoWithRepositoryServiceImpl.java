package com.fiap.spring_mongo_db.service.impl;

import com.fiap.spring_mongo_db.model.Artigo;
import com.fiap.spring_mongo_db.model.Autor;
import com.fiap.spring_mongo_db.repository.ArtigoRepository;
import com.fiap.spring_mongo_db.repository.AutorRepository;
import com.fiap.spring_mongo_db.service.ArtigoWithRepositoryService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class ArtigoWithRepositoryServiceImpl implements ArtigoWithRepositoryService {
    private final ArtigoRepository artigoRepository;
    private final AutorRepository autorRepository;
    private final MongoTransactionManager transactionManager;

    public ArtigoWithRepositoryServiceImpl(
            ArtigoRepository artigoRepository,
            AutorRepository autorRepository,
            MongoTransactionManager transactionManager
    ) {
        this.artigoRepository = artigoRepository;
        this.autorRepository = autorRepository;
        this.transactionManager = transactionManager;
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

    @Override
    public ResponseEntity<?> atualizarArtigo(final String id, final Artigo artigo) {
        try {
            final var artigoExistente = artigoRepository.findById(id);

            if (artigoExistente.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Artigo não localizado");
            }

            final var artigoToUpdate = artigoExistente.get();
            artigoToUpdate.setTitulo(artigo.getTitulo());
            artigoToUpdate.setTexto(artigo.getTexto());
            artigoToUpdate.setData(artigo.getData());
            artigoRepository.save(artigoToUpdate);

            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao atualizar artigo: " + e.getMessage());
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

    @Override
    public ResponseEntity<?> criarArtigoComAutor(final Artigo artigo, final Autor autor) {
        final var transaction = new TransactionTemplate(transactionManager);

        return transaction.execute(status -> {
            try {
                autorRepository.save(autor);

                artigo.setData(LocalDateTime.now());
                artigo.setAutor(autor);

                artigoRepository.save(artigo);

                return ResponseEntity.status(HttpStatus.CREATED)
                        .build();
            } catch (Exception e) {
                // Realiza o rollback caso ocorra um erro
                status.setRollbackOnly();

                throw new RuntimeException(" Erro ao criar um artigo com autor: " + e.getMessage());
            }
        });
    }
}
