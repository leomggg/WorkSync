package com.example.worksync.dao;

import com.example.worksync.config.PostgresConfig;
import com.example.worksync.model.Empleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpleadoDAO {
    private final PostgresConfig config;

    public EmpleadoDAO() {
        this.config = PostgresConfig.getInstance();
    }

    public void registrarUsuarioPrueba() {
        android.util.Log.d("EmpleadoDAO", "Asegurando tabla y usuario de prueba...");
        
        try (Connection conn = config.getConnection();
            java.sql.Statement st = conn.createStatement()) {
            
            // Ejecutamos por separado para asegurar compatibilidad total
            st.execute("CREATE TABLE IF NOT EXISTS empleados (" +
                    "id SERIAL PRIMARY KEY, " +
                    "nombre VARCHAR(100), " +
                    "email VARCHAR(100) UNIQUE, " +
                    "password VARCHAR(100))");
            
            st.execute("INSERT INTO empleados (nombre, email, password) " +
                    "VALUES ('Usuario Prueba', 'test@worksync.com', '1234') " +
                    "ON CONFLICT (email) DO NOTHING");

            android.util.Log.i("EmpleadoDAO", "Tabla y usuario de prueba procesados correctamente.");
        } catch (Throwable e) {
            // Capturamos Throwable para evitar que errores internos maten el hilo
            android.util.Log.e("EmpleadoDAO", "Fallo silencioso en inicialización: " + e.getMessage());
        }
    }

    public Empleado login(String email, String password) throws SQLException {
        android.util.Log.d("EmpleadoDAO", "Intentando login para: " + email);
        String query = "SELECT * FROM empleados WHERE email = ? AND password = ?";
        try (Connection conn = config.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)) {
            
            android.util.Log.d("EmpleadoDAO", "Conexión a PostgreSQL establecida.");
            ps.setString(1, email);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    android.util.Log.d("EmpleadoDAO", "Usuario encontrado: " + rs.getString("nombre"));
                    return new Empleado(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                } else {
                    android.util.Log.w("EmpleadoDAO", "No se encontró ningún usuario con ese email/password.");
                }
            }
        } catch (SQLException e) {
            android.util.Log.e("EmpleadoDAO", "Error de SQL durante el login: " + e.getMessage());
            throw e;
        }
        return null;
    }
}