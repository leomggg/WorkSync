package com.example.worksync;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.worksync.logic.GestorHibrido;
import com.example.worksync.logic.SyncCallback;
import com.example.worksync.model.Empleado;
import com.example.worksync.model.Tarea;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private GestorHibrido gestor;

    private final ExecutorService setupExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail    = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin   = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        // Autocompletar con datos de prueba
        etEmail.setText("test@worksync.com");
        etPassword.setText("1234");

        btnLogin.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        setupExecutor.execute(() -> {
            try {
                gestor = new GestorHibrido();
                // Intentamos asegurar el usuario, pero si falla no matamos la app
                try {
                    gestor.asegurarUsuarioPrueba();
                } catch (Exception dbError) {
                    android.util.Log.e("LoginActivity", "Fallo al asegurar usuario: " + dbError.getMessage());
                }
                
                runOnUiThread(() -> {
                    btnLogin.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                });
            } catch (Throwable e) {
                android.util.Log.e("LoginActivity", "Error crítico de inicio: " + e.getMessage());
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnLogin.setEnabled(true);
                });
            }
        });

        btnLogin.setOnClickListener(v -> {
            String email    = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
                return;
            }

            setLoading(true);
            gestor.iniciarSesionYCargarTareas(email, password, new SyncCallback() {
                @Override
                public void onLoginSuccess(Empleado empleado, List<Tarea> tareas) {
                    setLoading(false);
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.putExtra("empleado_nombre", empleado.getNombre());
                    intent.putExtra("empleado_id", empleado.getId());
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String mensaje) {
                    setLoading(false);
                    Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setupExecutor.shutdown();
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!loading);
        etEmail.setEnabled(!loading);
        etPassword.setEnabled(!loading);
    }
}