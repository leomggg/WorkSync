package com.example.worksync.logic;

import com.example.worksync.config.MongoConfig;
import com.example.worksync.config.PostgresConfig;
import com.example.worksync.dao.EmpleadoDAO;
import com.example.worksync.dao.TareaDAO;
import com.example.worksync.model.Empleado;
import com.example.worksync.model.Tarea;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class GestorHibrido {
    private final EmpleadoDAO empleadoDAO;
    private final TareaDAO tareaDAO;
    private final ExecutorService postgresExecutor;
    private final ExecutorService mongoExecutor;

    public GestorHibrido() {
        this.empleadoDAO = new EmpleadoDAO();
        this.tareaDAO = new TareaDAO();
        this.postgresExecutor = PostgresConfig.getInstance().getExecutor();
        this.mongoExecutor = MongoConfig.getInstance().getExecutor();
    }

    public void iniciarSesionYCargarTareas(String email, String password, SyncCallback callback) {
        postgresExecutor.execute(() -> {
            Empleado empleado = empleadoDAO.login(email, password);
            if (empleado != null) {
                mongoExecutor.execute(() -> {
                    List<Tarea> tareas = tareaDAO.listarPorEmpleado(empleado.getId());
                    callback.onLoginSuccess(empleado, tareas);
                });
            } else {
                callback.onError("Credenciales incorrectas");
            }
        });
    }
}