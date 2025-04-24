package com.fiap.spring_mongo_db.config;

import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
public class MongoTransactionConfig {

    @Autowired
    private MongoClient mongoClient;

    @Bean
    public MongoTransactionManager transactionManager(final MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
}
