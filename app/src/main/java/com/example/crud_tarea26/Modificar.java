package com.example.crud_tarea26;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.crud_tarea26.imageUtil;
import com.example.crud_tarea26.modelo.ProductoModel;
import com.example.crud_tarea26.modelo.ProductoResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Modificar extends AppCompatActivity {

    private EditText txtnombre, txtprecio, txtId;
    private static final int IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Spinner spcategoria;
    private String imagen64;
    private Button btnModificar;

    private String apiKey, nombreUsuario, contrasena;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);

        txtnombre = findViewById(R.id.txtnombre);
        txtprecio = findViewById(R.id.txtprecio);
        imageView = findViewById(R.id.imagen);
        spcategoria = findViewById(R.id.spcategoria);
        txtId = findViewById(R.id.textIdM);
        btnModificar = findViewById(R.id.btnModificar);

        txtId.setEnabled(false);

        llenarSpinner();

        apiKey = getIntent().getStringExtra("apiKey");
        nombreUsuario = getIntent().getStringExtra("nombre");
        contrasena = getIntent().getStringExtra("contrasena");

        Retrofit retrofitAutenticado = RetrofitClient.getRetrofitClient(nombreUsuario, contrasena, apiKey);
        apiService = retrofitAutenticado.create(ApiService.class);

        String idProducto = getIntent().getStringExtra("Id");
        if (idProducto != null) {
            cargarProductoDesdeAPI(idProducto);
        }

        imageView.setOnClickListener(v -> seleccionarImagen());

        btnModificar.setOnClickListener(v -> actualizarProducto());
    }

    private void llenarSpinner() {
        List<String> categorias = Arrays.asList("Tiramisú", "Cheesecake", "Flan", "Brownies", "Panacota");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcategoria.setAdapter(adapter);
    }

    private void cargarProductoDesdeAPI(String idProducto) {
        Call<ProductoResponse> call = apiService.getProductos(); // Idealmente deberías tener un endpoint getProductoPorId(id)

        call.enqueue(new Callback<ProductoResponse>() {
            @Override
            public void onResponse(Call<ProductoResponse> call, Response<ProductoResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    for (ProductoModel producto : response.body().getData()) {
                        if (String.valueOf(producto.getId()).equals(idProducto)) {
                            txtId.setText(String.valueOf(producto.getId()));
                            txtnombre.setText(producto.getNombre());
                            txtprecio.setText(String.valueOf(producto.getPrecio()));

                            int spinnerPosition = ((ArrayAdapter<String>) spcategoria.getAdapter())
                                    .getPosition(producto.getCategoria());
                            spcategoria.setSelection(spinnerPosition);

                            imagen64 = producto.getImagenProducto();
                            Bitmap bitmap = imageUtil.decodeFromBase64(imagen64);
                            if (bitmap != null) {
                                imageView.setImageBitmap(bitmap);
                            } else {
                                Toast.makeText(Modificar.this, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                    }
                } else {
                    Toast.makeText(Modificar.this, "Error al obtener producto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductoResponse> call, Throwable t) {
                Toast.makeText(Modificar.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imagenUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagenUri);
                imageView.setImageBitmap(bitmap);
                imagen64 = imageUtil.encodeToBase64(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void actualizarProducto() {
        String idStr = txtId.getText().toString().trim();
        String nombre = txtnombre.getText().toString().trim();
        String precioStr = txtprecio.getText().toString().trim();
        String categoria = spcategoria.getSelectedItem().toString();

        if (nombre.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double precio;
        int id;

        try {
            id = Integer.parseInt(idStr);
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID o Precio inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        ProductoModel producto = new ProductoModel(id, nombre, precio, categoria, imagen64);

        Call<Void> call = apiService.actualizarProducto(producto);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Modificar.this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(Modificar.this, "Error al actualizar producto. Código: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Modificar.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}