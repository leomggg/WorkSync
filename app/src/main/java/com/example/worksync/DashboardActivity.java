package com.example.worksync;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.worksync.model.Tarea;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private TextView tvWelcome;
    private ListView lvTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvWelcome = findViewById(R.id.tvWelcome);
        lvTareas = findViewById(R.id.lvTareas);

        String nombre = getIntent().getStringExtra("empleado_nombre");
        tvWelcome.setText(getString(R.string.welcome_user, nombre));

        @SuppressWarnings("unchecked")
        List<Tarea> tareas = (List<Tarea>) getIntent().getSerializableExtra("tareas_list");
        
        List<String> titulos = new ArrayList<>();
        if (tareas != null) {
            for (Tarea t : tareas) {
                titulos.add(t.getTitulo() + "\n" + t.getDescripcion());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titulos);
        lvTareas.setAdapter(adapter);
    }
}