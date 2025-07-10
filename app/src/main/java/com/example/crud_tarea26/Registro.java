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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

        // Calcular ha1 = md5(usuario + ":" + realm + ":" + contraseña)
        String realm = "API CRUD";
        String ha1 = md5(nombre + ":" + realm + ":" + password);

        Map<String, String> datos = new HashMap<>();
        datos.put("nombre", nombre);
        datos.put("correo", correo);
        datos.put("contrasena", password);
        datos.put("ha1", ha1);  // Enviar ha1 también al backend

        ApiService apiService = RetrofitClient.getApiService();
        Call<Void> call = apiService.registrarUsuario(datos);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    finish(); // Cierra la actividad y regresa
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

    private String md5(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(texto.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error al generar MD5", e);
            return null;
        }
    }
}
