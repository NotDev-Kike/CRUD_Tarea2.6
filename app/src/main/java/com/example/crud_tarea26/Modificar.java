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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Modificar extends AppCompatActivity {

    private EditText txtnombre, txtprecio, txtId;
    private static final int IMAGE_REQUEST = 1;
    ImageView imageView;
    Spinner spcategoria;
    private Uri imagenUri;
    private String imagen64;
    ArrayAdapter<String> adaptador;
    Button btnModificar;
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
        llenarDatos();
        // Me quede despierto contigo porque me enc
        imageView.setOnClickListener(v -> seleccionarImagen());

    }

    private void llenarDatos(){
        Intent previousIntent = getIntent();
        String idProducto = previousIntent.getStringExtra("Id");
        String nombreProducto = previousIntent.getStringExtra("Nombre");
        String precioProducto = previousIntent.getStringExtra("Precio");
        String categoriaProducto = previousIntent.getStringExtra("Categoria");
        String imagenProducto = previousIntent.getStringExtra("Imagen");

        txtId.setText(idProducto);
        txtnombre.setText(nombreProducto);
        txtprecio.setText(precioProducto);
        imageView.setImageBitmap(imageUtil.decodeFromBase64(imagenProducto));


    }

    private void llenarSpinner(){

        List<String> categorias = Arrays.asList("Tiramisú", "Cheesecake", "Flan", "Brownies","Panacota");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcategoria.setAdapter(adapter);
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
                imageView.setImageBitmap(bitmap);

                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}