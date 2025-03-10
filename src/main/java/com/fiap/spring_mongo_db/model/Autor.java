package com.fiap.spring_mongo_db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record Autor(
        @Id
        String codigo,
        String nome,
        String biografia,
        String imagem
) {
}
