package com.example.worksync.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PostgresConfig {
    private static PostgresConfig instance;
    private static final String URL = "jdbc:postgresql://10.0.2.2:5432/worksync";
    private static final String USER = "postgres";
    private static final String PASS = "admin";
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    private PostgresConfig() {}

    public static synchronized PostgresConfig getInstance() {
        if (instance == null) instance = new PostgresConfig();
        return instance;
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = DriverManager.getConnection(URL, USER, PASS);
        if (conn != null) {
            android.util.Log.d("PostgresConfig", "Conexión establecida con éxito");
        }
        return conn;
    }

    public ExecutorService getExecutor() {
        return executor;
    }
}