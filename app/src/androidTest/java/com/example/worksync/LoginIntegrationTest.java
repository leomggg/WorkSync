package com.example.worksync;

import static org.junit.Assert.*;

import com.example.worksync.dao.EmpleadoDAO;
import com.example.worksync.model.Empleado;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

public class LoginIntegrationTest {

    private EmpleadoDAO empleadoDAO;

    @Before
    public void setUp() {
        empleadoDAO = new EmpleadoDAO();
    }

    @Test
    public void testLoginWithTestUser() {
        try {
            // Intentamos loguear con el usuario de prueba definido en el README
            Empleado empleado = empleadoDAO.login("test@worksync.com", "1234");
            
            // Si llega aquí sin lanzar SQLException, la conexión física funciona.
            // Si el empleado es null, el usuario no existe en la tabla.
            assertNotNull("El usuario 'test@worksync.com' debería existir en la base de datos PostgreSQL", empleado);
            assertEquals("Usuario Prueba", empleado.getNombre());
            
        } catch (SQLException e) {
            fail("Error de conexión a PostgreSQL. ¿Está el servidor corriendo y permite conexiones desde 10.0.2.2? Error: " + e.getMessage());
        }
    }
}
