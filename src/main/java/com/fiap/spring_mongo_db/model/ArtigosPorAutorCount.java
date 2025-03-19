package com.fiap.spring_mongo_db.model;

public record ArtigosPorAutorCount(
        Autor autor,
        Long totalArtigos
) {
}
