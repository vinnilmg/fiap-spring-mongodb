package com.fiap.spring_mongo_db.repository;

import com.fiap.spring_mongo_db.model.Artigo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ArtigoRepository extends MongoRepository<Artigo, String> {
    void deleteById(String id);

    List<Artigo> findByStatusAndDataGreaterThan(Integer status, LocalDateTime data);
}
