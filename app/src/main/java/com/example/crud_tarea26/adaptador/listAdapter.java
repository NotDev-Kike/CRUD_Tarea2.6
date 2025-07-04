package com.example.crud_tarea26.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.crud_tarea26.R;
import com.example.crud_tarea26.imageUtil;
import com.example.crud_tarea26.modelo.ProductoModel;

import java.util.List;

public class listAdapter extends ArrayAdapter<ProductoModel> {

    private List<ProductoModel> myList;
    private Context myContext;
    private int resourceLayout;
    public listAdapter(@NonNull Context context, int resource, List<ProductoModel> objects) {
        super(context, resource, objects);

    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ProductoModel modelo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_row, parent, false);
        }
        String imagen64 = modelo.getImagenProducto();

        ImageView imagen = convertView.findViewById(R.id.imageView);
        imagen.setImageBitmap(imageUtil.decodeFromBase64(imagen64));

        TextView id = convertView.findViewById(R.id.textId);
        id.setText(String.valueOf(modelo.getId()));

        TextView nombre = convertView.findViewById(R.id.textNombre);
        nombre.setText(modelo.getNombre());

        TextView precio = convertView.findViewById(R.id.textPrecio);
        precio.setText(String.valueOf(modelo.getPrecio()));

        TextView categoria = convertView.findViewById(R.id.textCategoria);
        categoria.setText(modelo.getCategoria());


        return convertView;
    }
    }