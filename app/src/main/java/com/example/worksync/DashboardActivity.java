package com.example.worksync;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.worksync.model.Tarea;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private TextView tvWelcome;
    private ListView lvTareas;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvWelcome = findViewById(R.id.tvWelcome);
        lvTareas = findViewById(R.id.lvTareas);
        btnLogout = findViewById(R.id.btnLogout);

        String nombre = getIntent().getStringExtra("empleado_nombre");
        tvWelcome.setText(getString(R.string.welcome_user, nombre));

        @SuppressWarnings("unchecked")
        List<Tarea> tareas = (List<Tarea>) getIntent().getSerializableExtra("tareas_list");
        
        List<String> titulos = new ArrayList<>();
        if (tareas != null && !tareas.isEmpty()) {
            for (Tarea t : tareas) {
                titulos.add("📌 " + t.getTitulo() + "\n   " + t.getDescripcion());
            }
        } else {
            titulos.add(getString(R.string.empty_tasks));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titulos);
        lvTareas.setAdapter(adapter);

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
        });
    }
}