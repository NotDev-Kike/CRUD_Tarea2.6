package com.example.crud_tarea26;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crud_tarea26.api.ApiService;
import com.example.crud_tarea26.api.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registro extends AppCompatActivity {

    EditText txtNombre, txtCorreo, txtPassword;
    Button btnRegistrar;

    private static final String TAG = "Registro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtNombre = findViewById(R.id.txtUsuarioNombre);
        txtCorreo = findViewById(R.id.txtUsuarioCorreo);
        txtPassword = findViewById(R.id.txtUsuarioPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String nombre = txtNombre.getText().toString().trim();
        String correo = txtCorreo.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if (nombre.isEmpty() || correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> datos = new HashMap<>();
        datos.put("nombre", nombre);
        datos.put("correo", correo);
        datos.put("contrasena", password);

        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.registrarUsuario(datos);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Log.e(TAG, "Error en respuesta: " + response.code());
                    Toast.makeText(Registro.this, "Error al registrar: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Falla en la conexión", t);
                Toast.makeText(Registro.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
