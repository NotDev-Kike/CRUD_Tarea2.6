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

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Productos extends AppCompatActivity {

    private static final int IMAGE_REQUEST = 1;
    EditText txtNombre, txtPrecio;
    Spinner spinner;
    Button btnGuardar, btnListado;
    ImageView imagen;
    private Uri imagenUri;
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

    }

    private void llenarSpinner(){

        List<String> categorias = Arrays.asList("Tiramisú", "Cheesecake", "Flan", "Brownies","Panacota");
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}