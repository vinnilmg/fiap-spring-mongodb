package com.fiap.spring_mongo_db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public record Artigo(
        @Id
        String codigo,
        String titulo,
        LocalDateTime data,
        String texto,
        String url,
        Integer status
) {
}
