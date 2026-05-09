package com.example.worksync.logic;

import android.os.Handler;
import android.os.Looper;
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
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public GestorHibrido() {
        this.empleadoDAO = new EmpleadoDAO();
        this.tareaDAO = new TareaDAO();
        this.postgresExecutor = PostgresConfig.getInstance().getExecutor();
        this.mongoExecutor = MongoConfig.getInstance().getExecutor();
    }

    public void asegurarUsuarioPrueba() {
        empleadoDAO.registrarUsuarioPrueba();
    }

    public void iniciarSesionYCargarTareas(String email, String password, SyncCallback callback) {
        postgresExecutor.execute(() -> {
            try {
                // Registro silencioso del usuario de prueba
                if ("test@worksync.com".equals(email)) {
                    try {
                        empleadoDAO.registrarUsuarioPrueba();
                    } catch (Throwable t) {
                        android.util.Log.e("GestorHibrido", "Error en auto-registro: " + t.getMessage());
                    }
                }

                Empleado empleado = empleadoDAO.login(email, password);
                if (empleado != null) {
                    mongoExecutor.execute(() -> {
                        try {
                            List<Tarea> tareas = tareaDAO.listarPorEmpleado(empleado.getId());
                            mainHandler.post(() -> callback.onLoginSuccess(empleado, tareas));
                        } catch (Throwable e) {
                            mainHandler.post(() -> callback.onError("Error MongoDB: " + e.getMessage()));
                        }
                    });
                } else {
                    mainHandler.post(() -> callback.onError("Credenciales no encontradas en la tabla"));
                }
            } catch (Throwable e) {
                // CAPTURA TOTAL: Si hay un error técnico (como el de ManagementFactory),
                // lo mostramos en el Toast en lugar de dejar que la app se cierre.
                final String errorMsg = e.toString();
                mainHandler.post(() -> callback.onError("CRASH EVITADO: " + errorMsg));
            }
        });
    }
}