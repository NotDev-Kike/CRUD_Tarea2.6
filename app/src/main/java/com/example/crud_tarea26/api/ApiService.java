package com.example.crud_tarea26.api;

import com.example.crud_tarea26.LoginRequest;
import com.example.crud_tarea26.LoginResponse;
import com.example.crud_tarea26.modelo.ProductoModel;
import com.example.crud_tarea26.modelo.ProductoResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @GET("read.php")
    Call<ProductoResponse> getProductos();

    @POST("create.php")
    Call<Void> crearProducto(@Body ProductoModel producto);

    @POST("update.php")
    Call<Void> actualizarProducto(@Body ProductoModel producto);

    @FormUrlEncoded
    @POST("delete.php")
    Call<Void> eliminarProducto(@Field("id") int id);

    @POST("login.php")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("registro.php")
    Call<Void> registrarUsuario(@Body Map<String, String> usuario);
}
