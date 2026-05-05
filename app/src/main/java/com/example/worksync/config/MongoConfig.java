package com.example.worksync.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MongoConfig {
    private static MongoConfig instance;
    private static final String URI = "mongodb://10.0.2.2:27017";
    private final MongoClient mongoClient;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private MongoConfig() {
        mongoClient = MongoClients.create(URI);
    }

    public static synchronized MongoConfig getInstance() {
        if (instance == null) instance = new MongoConfig();
        return instance;
    }

    public MongoDatabase getDatabase() {
        return mongoClient.getDatabase("worksync");
    }

    public ExecutorService getExecutor() {
        return executor;
    }
}