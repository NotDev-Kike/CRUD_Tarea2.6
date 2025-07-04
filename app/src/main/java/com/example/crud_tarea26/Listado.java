package com.example.crud_tarea26;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.crud_tarea26.adaptador.listAdapter;
import com.example.crud_tarea26.modelo.ProductoModel;

import java.util.ArrayList;
import java.util.List;

public class Listado extends AppCompatActivity {

    listAdapter mAdapter;
    List<ProductoModel> lista = new ArrayList<>();
    ProductoModel selectedProducto;

    ListView miLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listado);

        miLista = (ListView) findViewById(R.id.listView);

        lista = new ArrayList<>();
        llenarListView();

        miLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedProducto = (ProductoModel) (miLista.getItemAtPosition(position));
                AlertDialog dialog = createDialog("¿Que desea hacer?",selectedProducto);
                dialog.show();
            }
        });
    }

   public void llenarListView() {
       String imagenDePrueba = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=";
       lista.add(new ProductoModel(1,"Pastelitos",7.4,"Cheesecake",imagenDePrueba));
       lista.add(new ProductoModel(2,"Naty",13,"Dufoir",imagenDePrueba));
       mAdapter = new listAdapter(getApplicationContext(),0,lista);
       miLista.setAdapter(mAdapter);
   }//cheetos

    AlertDialog createDialog(String mensaje, ProductoModel lista){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensaje);
        builder.setPositiveButton("Modificar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent modificar = new Intent(getApplicationContext(), Modificar.class);
                modificar.putExtra("Id",String.valueOf((lista.getId())));
                modificar.putExtra("Nombre",String.valueOf((lista.getNombre())));
                modificar.putExtra("Precio",String.valueOf((lista.getPrecio())));
                modificar.putExtra("Categoria",String.valueOf((lista.getCategoria())));
                modificar.putExtra("Imagen",String.valueOf((lista.getImagenProducto())));
                startActivity(modificar);
            }
        });
        builder.setNegativeButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}