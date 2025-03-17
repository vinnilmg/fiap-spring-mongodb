package com.fiap.spring_mongo_db.repository;

import com.fiap.spring_mongo_db.model.Artigo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoRepository extends MongoRepository<Artigo, String> {
    void deleteById(String id);

    List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);

    @Query("{ $and: [ {'data': { $gte: ?0 }}, {'data': { $lte: ?1 }} ] }")
    List<Artigo> findByDataBetween(LocalDateTime de, LocalDateTime ate);

    Page<Artigo> findAll(Pageable pageable);

    List<Artigo> findByStatusOrderByTituloAsc(Integer status);

    @Query(value = "{ 'status': { $eq: ?0 } }", sort = "{ 'titulo': -1 }")
    List<Artigo> findByStatusOrderByTituloDesc(Integer status);
}
