package com.example.crud_tarea26.adaptador;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.crud_tarea26.R;
import com.example.crud_tarea26.imageUtil;
import com.example.crud_tarea26.modelo.ProductoModel;

import java.util.List;

public class listAdapter extends ArrayAdapter<ProductoModel> {

    private List<ProductoModel> myList;
    private Context myContext;
    private int resourceLayout;

    public listAdapter(@NonNull Context context, int resource, @NonNull List<ProductoModel> objects) {
        super(context, resource, objects);
        this.myContext = context;
        this.resourceLayout = resource;
        this.myList = objects;
    }

    private static class ViewHolder {
        ImageView imagen;
        TextView id;
        TextView nombre;
        TextView precio;
        TextView categoria;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(myContext).inflate(resourceLayout, parent, false);

            holder = new ViewHolder();
            holder.imagen = convertView.findViewById(R.id.imageView);
            holder.id = convertView.findViewById(R.id.textId);
            holder.nombre = convertView.findViewById(R.id.textNombre);
            holder.precio = convertView.findViewById(R.id.textPrecio);
            holder.categoria = convertView.findViewById(R.id.textCategoria);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProductoModel modelo = myList.get(position);

        String imagen64 = modelo.getImagenProducto();
        if (imagen64 != null && !imagen64.isEmpty()) {
            Bitmap bitmap = imageUtil.decodeFromBase64(imagen64);
            if (bitmap != null) {
                holder.imagen.setImageBitmap(bitmap);
            } else {
                holder.imagen.setImageResource(R.drawable.ic_producto);
            }
        } else {
            holder.imagen.setImageResource(R.drawable.ic_producto);
        }

        holder.id.setText(String.valueOf(modelo.getId()));
        holder.nombre.setText(modelo.getNombre());
        holder.precio.setText(String.format("$%.2f", modelo.getPrecio()));
        holder.categoria.setText(modelo.getCategoria());

        return convertView;
    }
}
