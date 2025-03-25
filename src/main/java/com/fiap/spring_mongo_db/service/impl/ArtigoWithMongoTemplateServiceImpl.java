package com.fiap.spring_mongo_db.service.impl;

import com.fiap.spring_mongo_db.model.Artigo;
import com.fiap.spring_mongo_db.model.ArtigoStatusCount;
import com.fiap.spring_mongo_db.model.ArtigosPorAutorCount;
import com.fiap.spring_mongo_db.service.ArtigoWithMongoTemplateService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class ArtigoWithMongoTemplateServiceImpl implements ArtigoWithMongoTemplateService {
    private final MongoTemplate mongoTemplate;

    public ArtigoWithMongoTemplateServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
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

    @Transactional
    @Override
    public void atualizarArtigoUrl(final String id, final String novaUrl) {
        final var query = new Query(Criteria.where("_id").is(id));
        final var update = new Update().set("url", novaUrl);
        mongoTemplate.updateFirst(query, update, Artigo.class);
    }

    @Transactional
    @Override
    public void deletarArtigo(final String codigo) {
        final var query = new Query(Criteria.where("_id").is(codigo));
        mongoTemplate.remove(query, Artigo.class);
    }

    @Override
    public List<Artigo> encontrarArtigosComplexos(
            final Integer status,
            final LocalDateTime data,
            final String titulo
    ) {
        final var criteria = new Criteria();
        criteria.and("data").lte(data);

        if (nonNull(status)) {
            criteria.and("status").is(status);
        }

        if (nonNull(titulo) && !titulo.isEmpty()) {
            criteria.and("titulo").regex(titulo, "i");
        }

        final var query = new Query(criteria);
        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<Artigo> findByTexto(final String searchTerm) {
        // Para filtrar por termos em um texto
        final var criteria = TextCriteria.forDefaultLanguage()
                .matchingPhrase(searchTerm);

        final var query = TextQuery.queryText(criteria)
                .sortByScore();

        return mongoTemplate.find(query, Artigo.class);
    }

    @Override
    public List<ArtigoStatusCount> contarArtigosPorStatus() {
        final var aggregation = Aggregation.newAggregation(
                Artigo.class,
                Aggregation.group("status")
                        .count()
                        .as("quantidade"),
                Aggregation.project("quantidade")
                        .and("status")
                        .previousOperation()
        );

        final var result = mongoTemplate.aggregate(aggregation, ArtigoStatusCount.class);
        return result.getMappedResults();
    }

    @Override
    public List<ArtigosPorAutorCount> contarArtigosPorAutorNoPeriodo(
            final LocalDate dataInicio,
            final LocalDate dataFim
    ) {
        final var aggregation = Aggregation.newAggregation(
                Artigo.class,
                Aggregation.match(
                        Criteria.where("data")
                                .gte(dataInicio.atStartOfDay())
                                .lt(dataFim.plusDays(1).atStartOfDay())
                ),
                Aggregation.group("autor")
                        .count()
                        .as("totalArtigos"),
                Aggregation.project("totalArtigos")
                        .and("autor")
                        .previousOperation()
        );

        final var result = mongoTemplate.aggregate(aggregation, ArtigosPorAutorCount.class);
        return result.getMappedResults();
    }
}
