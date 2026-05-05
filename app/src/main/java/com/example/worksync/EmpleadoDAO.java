package com.example.worksync;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmpleadoDAO {
    private final PostgresConfig config;

    public EmpleadoDAO() {
        this.config = PostgresConfig.getInstance();
    }

    public Empleado login(String email, String password) {
        String query = "SELECT * FROM empleados WHERE email = ? AND password = ?";
        try (Connection conn = config.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Empleado(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}