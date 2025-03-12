package com.fiap.spring_mongo_db.repository;

import com.fiap.spring_mongo_db.model.Artigo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArtigoRepository extends MongoRepository<Artigo, String> {
    void deleteById(String id);
}
