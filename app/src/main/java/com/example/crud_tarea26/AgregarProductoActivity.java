package com.example.crud_tarea26;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crud_tarea26.api.ApiService;
import com.example.crud_tarea26.api.RetrofitClient;
import com.example.crud_tarea26.modelo.ProductoModel;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AgregarProductoActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST = 1;

    EditText txtNombre, txtPrecio;
    Spinner spinner;
    Button btnGuardar, btnListado;
    ImageView imagen;
    private Uri imagenUri;
    private String imagen64;

    private ApiService apiService;
    private String apiKey, nombreUsuario, contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        txtNombre = findViewById(R.id.txtnombre);
        txtPrecio = findViewById(R.id.txtprecio);
        imagen = findViewById(R.id.imagen);
        spinner = findViewById(R.id.spcategoria);

        btnGuardar = findViewById(R.id.btnguardar);
        btnListado = findViewById(R.id.btnListado);

        llenarSpinner();

        imagen.setOnClickListener(v -> seleccionarImagen());

        btnGuardar.setOnClickListener(v -> guardarProducto());

        btnListado.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Listado.class);
            intent.putExtra("apiKey", apiKey);
            intent.putExtra("nombre", nombreUsuario);
            intent.putExtra("contrasena", contrasena);
            startActivity(intent);
        });

        apiKey = getIntent().getStringExtra("apiKey");
        nombreUsuario = getIntent().getStringExtra("nombre");
        contrasena = getIntent().getStringExtra("contrasena");

        Retrofit retrofitAutenticado = RetrofitClient.getRetrofitClient(nombreUsuario, contrasena, apiKey);
        apiService = retrofitAutenticado.create(ApiService.class);
    }

    private void llenarSpinner() {
        List<String> categorias = Arrays.asList("Tiramisú", "Cheesecake", "Flan", "Brownies", "Panacota");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagenUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagenUri);
                imagen.setImageBitmap(bitmap);
                imagen.setVisibility(View.VISIBLE);
                imagen64 = imageUtil.encodeToBase64(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void guardarProducto() {
        String nombre = txtNombre.getText().toString().trim();
        String precioStr = txtPrecio.getText().toString().trim();
        String categoria = spinner.getSelectedItem().toString();

        if (nombre.isEmpty() || precioStr.isEmpty() || imagen64 == null) {
            Toast.makeText(this, "Complete todos los campos y seleccione una imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        try {
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Precio inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        ProductoModel producto = new ProductoModel();
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setCategoria(categoria);
        producto.setImagenProducto(imagen64);

        Call<Void> call = apiService.crearProducto(producto);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AgregarProductoActivity.this, "Producto guardado exitosamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                } else {
                    Toast.makeText(AgregarProductoActivity.this, "Error al guardar el producto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AgregarProductoActivity.this, "Fallo la conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtPrecio.setText("");
        imagen.setImageResource(android.R.color.transparent);
        imagen64 = null;
        spinner.setSelection(0);
    }
}
