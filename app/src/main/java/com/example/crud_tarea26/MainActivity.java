package com.example.crud_tarea26;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crud_tarea26.api.ApiService;
import com.example.crud_tarea26.api.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText edtCorreo, edtContrasena;
    private Button btnLogin;
    private TextView btnRegistrar;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCorreo = findViewById(R.id.txtCorreo);
        edtContrasena = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrar = findViewById(R.id.btnIrARegistro);

        btnLogin.setOnClickListener(v -> iniciarSesion());
        btnRegistrar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Registro.class);
            startActivity(intent);
        });
    }

    private void iniciarSesion() {
        String correo = edtCorreo.getText().toString().trim();
        String contrasena = edtContrasena.getText().toString().trim();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService = RetrofitClient.getApiService();
        Call<LoginResponse> call = apiService.login(new LoginRequest(correo, contrasena));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    String apiKey = response.body().getApiKey();
                    String nombreUsuario = response.body().getNombre();

                    Log.d("Login", "Login correcto. API Key: " + apiKey);

                    Intent intent = new Intent(MainActivity.this, AgregarProductoActivity.class);
                    intent.putExtra("apiKey", apiKey);
                    intent.putExtra("nombre", nombreUsuario);
                    intent.putExtra("contrasena", contrasena);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
