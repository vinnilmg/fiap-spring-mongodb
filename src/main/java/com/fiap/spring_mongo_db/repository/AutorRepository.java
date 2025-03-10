package com.fiap.spring_mongo_db.repository;

import com.fiap.spring_mongo_db.model.Autor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AutorRepository extends MongoRepository<Autor, String> {
}
