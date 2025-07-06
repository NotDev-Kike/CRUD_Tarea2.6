package com.example.crud_tarea26;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crud_tarea26.adaptador.listAdapter;
import com.example.crud_tarea26.api.ApiService;
import com.example.crud_tarea26.api.RetrofitClient;
import com.example.crud_tarea26.modelo.ProductoModel;
import com.example.crud_tarea26.modelo.ProductoResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Listado extends AppCompatActivity {

    private listAdapter mAdapter;
    private List<ProductoModel> lista = new ArrayList<>();
    private ProductoModel selectedProducto;
    private ListView miLista;

    private ApiService apiService;
    private String apiKey, nombreUsuario, contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        miLista = findViewById(R.id.listView);

        apiKey = getIntent().getStringExtra("apiKey");
        if (apiKey == null) apiKey = "";
        nombreUsuario = getIntent().getStringExtra("nombre");
        if (nombreUsuario == null) nombreUsuario = "";
        contrasena = getIntent().getStringExtra("contrasena");
        if (contrasena == null) contrasena = "";

        Log.d("Listado", "API Key: " + apiKey);
        Log.d("Listado", "Usuario: " + nombreUsuario);

        apiService = RetrofitClient.getAuthenticatedApiService(nombreUsuario, contrasena, apiKey);

        mAdapter = new listAdapter(getApplicationContext(), R.layout.item_row, lista);
        miLista.setAdapter(mAdapter);

        cargarProductosDesdeApi();

        miLista.setOnItemClickListener((parent, view, position, id) -> {
            selectedProducto = lista.get(position);
            AlertDialog dialog = createDialog("¿Qué desea hacer?", selectedProducto);
            dialog.show();
        });
    }

    private void cargarProductosDesdeApi() {
        Call<ProductoResponse> call = apiService.getProductos();
        call.enqueue(new Callback<ProductoResponse>() {
            @Override
            public void onResponse(Call<ProductoResponse> call, Response<ProductoResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    lista.clear();
                    lista.addAll(response.body().getData());
                    runOnUiThread(() -> mAdapter.notifyDataSetChanged());
                } else {
                    Toast.makeText(Listado.this, "Error en la respuesta del servidor", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProductoResponse> call, Throwable t) {
                Toast.makeText(Listado.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private AlertDialog createDialog(String mensaje, ProductoModel producto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje);

        builder.setPositiveButton("Modificar", (dialog, which) -> {
            Intent modificar = new Intent(Listado.this, Modificar.class);
            modificar.putExtra("Id", String.valueOf(producto.getId()));
            modificar.putExtra("Nombre", producto.getNombre());
            modificar.putExtra("Precio", String.valueOf(producto.getPrecio()));
            modificar.putExtra("Categoria", producto.getCategoria());
            modificar.putExtra("Imagen", producto.getImagenProducto());
            modificar.putExtra("apiKey", apiKey);
            modificar.putExtra("nombre", nombreUsuario);
            modificar.putExtra("contrasena", contrasena);
            startActivity(modificar);
        });

        builder.setNegativeButton("Eliminar", (dialog, which) -> eliminarProducto(producto.getId()));

        return builder.create();
    }

    private void eliminarProducto(int id) {
        Map<String, Integer> idProducto = new HashMap<>();
        idProducto.put("id", id);

        Call<Void> call = apiService.eliminarProducto(idProducto);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Listado.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < lista.size(); i++) {
                        if (lista.get(i).getId() == id) {
                            lista.remove(i);
                            break;
                        }
                    }
                    runOnUiThread(() -> mAdapter.notifyDataSetChanged());
                } else {
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null)
                            errorBody = response.errorBody().string();
                    } catch (Exception e) {
                        errorBody = "Error al leer errorBody: " + e.getMessage();
                    }
                    Toast.makeText(Listado.this, "Error al eliminar producto: " + response.code() + "\n" + errorBody, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(Listado.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
