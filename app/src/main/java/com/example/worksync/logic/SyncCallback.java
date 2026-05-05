package com.example.worksync.logic;

import com.example.worksync.model.Empleado;
import com.example.worksync.model.Tarea;
import java.util.List;

public interface SyncCallback {
    void onLoginSuccess(Empleado empleado, List<Tarea> tareas);
    void onError(String mensaje);
}