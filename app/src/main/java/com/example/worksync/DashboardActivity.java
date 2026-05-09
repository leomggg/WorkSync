package com.example.worksync;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.worksync.adapter.TareaAdapter;
import com.example.worksync.dao.TareaDAO;
import com.example.worksync.model.Tarea;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardActivity extends AppCompatActivity {
    private TextView tvWelcome;
    private ListView lvTareas;
    private Button btnLogout;
    private FloatingActionButton fabAddTask;
    private TareaDAO tareaDAO;
    private int idEmpleado;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvWelcome = findViewById(R.id.tvWelcome);
        lvTareas = findViewById(R.id.lvTareas);
        btnLogout = findViewById(R.id.btnLogout);
        fabAddTask = findViewById(R.id.fabAddTask);
        tareaDAO = new TareaDAO();

        String nombre = getIntent().getStringExtra("empleado_nombre");
        idEmpleado = getIntent().getIntExtra("empleado_id", -1);
        
        tvWelcome.setText("Bienvenido, " + nombre);

        cargarTareas();

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        });

        fabAddTask.setOnClickListener(v -> mostrarDialogoNuevaTarea());
    }

    private void cargarTareas() {
        executor.execute(() -> {
            List<Tarea> tareas = tareaDAO.listarPorEmpleado(idEmpleado);
            // Ordenamos: No completadas primero
            tareas.sort((t1, t2) -> Boolean.compare(t1.isCompletada(), t2.isCompletada()));
            
            runOnUiThread(() -> {
                TareaAdapter adapter = new TareaAdapter(this, tareas, t -> {
                    executor.execute(() -> {
                        tareaDAO.actualizarEstado(t.getIdMongo(), !t.isCompletada());
                        runOnUiThread(this::cargarTareas);
                    });
                });
                lvTareas.setAdapter(adapter);
            });
        });
    }

    private void mostrarDialogoNuevaTarea() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nueva Tarea");

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_nueva_tarea, null);
        final EditText inputTitulo = dialogView.findViewById(R.id.etTituloTarea);
        final EditText inputDesc = dialogView.findViewById(R.id.etDescTarea);
        final Spinner spPrioridad = dialogView.findViewById(R.id.spPrioridad);

        builder.setView(dialogView);

        builder.setPositiveButton("Crear", (dialog, which) -> {
            String titulo = inputTitulo.getText().toString();
            String desc = inputDesc.getText().toString();
            String prioridad = spPrioridad.getSelectedItem().toString();
            if (!titulo.isEmpty()) {
                guardarTarea(titulo, desc, prioridad);
            } else {
                Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void guardarTarea(String titulo, String desc, String prioridad) {
        executor.execute(() -> {
            Tarea nueva = new Tarea(null, titulo, desc, idEmpleado, false, prioridad);
            tareaDAO.insertar(nueva);
            runOnUiThread(() -> {
                Toast.makeText(this, "Tarea creada", Toast.LENGTH_SHORT).show();
                cargarTareas();
            });
        });
    }
}