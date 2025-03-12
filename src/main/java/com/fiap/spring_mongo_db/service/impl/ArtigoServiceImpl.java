package com.fiap.spring_mongo_db.service.impl;

import com.fiap.spring_mongo_db.model.Artigo;
import com.fiap.spring_mongo_db.repository.ArtigoRepository;
import com.fiap.spring_mongo_db.repository.AutorRepository;
import com.fiap.spring_mongo_db.service.ArtigoService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class ArtigoServiceImpl implements ArtigoService {
    private final ArtigoRepository artigoRepository;
    private final AutorRepository autorRepository;
    private final MongoTemplate mongoTemplate;

    public ArtigoServiceImpl(
            ArtigoRepository artigoRepository,
            AutorRepository autorRepository,
            MongoTemplate mongoTemplate
    ) {
        this.artigoRepository = artigoRepository;
        this.autorRepository = autorRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Artigo> obterTodos() {
        return artigoRepository.findAll();
    }

    @Override
    public Artigo obterPorCodigo(final String codigo) {
        return artigoRepository.findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Artigo não existe"));
    }

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
    public List<Artigo> obterPorDataMaiorQue(final LocalDateTime data) {
        final var query = new Query(Criteria.where("data").gt(data));
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<Artigo> obterPorDataEStatus(final LocalDateTime data, final Integer status) {
        final var query = new Query(
                Criteria.where("data")
                        .gt(data)
                        .and("status")
                        .is(status)
        );
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public void atualizar(final Artigo artigoAtualizado) {
        artigoRepository.save(artigoAtualizado);
    }

    @Override
    public void atualizarArtigoUrl(final String id, final String novaUrl) {
        final var query = new Query(Criteria.where("_id").is(id));
        final var update = new Update().set("url", novaUrl);
        mongoTemplate.updateFirst(query, update, Artigo.class);
    }

    @Override
    public void deletar(final String codigo) {
        artigoRepository.deleteById(codigo);
    }

    @Override
    public void deletarArtigo(final String codigo) {
        final var query = new Query(Criteria.where("_id").is(codigo));
        mongoTemplate.remove(query, Artigo.class);
    }
}
