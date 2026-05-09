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
        } catch (Exception e) {
            android.util.Log.e("PostgresConfig", "Driver not found");
        }

        try {
            // Intento 1: Conectar a la base de datos real
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            if (e.getSQLState().equals("3D000") || e.getMessage().contains("does not exist")) {
                // Si no existe, conectamos a 'postgres' para crearla
                String rootUrl = "jdbc:postgresql://10.0.2.2:5432/postgres";
                try (Connection rootConn = DriverManager.getConnection(rootUrl, USER, PASS);
                    java.sql.Statement stmt = rootConn.createStatement()) {
                    stmt.executeUpdate("CREATE DATABASE worksync");
                    android.util.Log.i("PostgresConfig", "Base de datos 'worksync' creada automáticamente");
                } catch (Exception ex) {
                    android.util.Log.e("PostgresConfig", "No se pudo crear la DB: " + ex.getMessage());
                }
                // Reintentamos la conexión original
                return DriverManager.getConnection(URL, USER, PASS);
            }
            throw e;
        }
    }

    public ExecutorService getExecutor() {
        return executor;
    }
}