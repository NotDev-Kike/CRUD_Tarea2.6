package com.example.crud_tarea26.modelo;

import java.util.List;

public class ProductoResponse {
    private boolean success;
    private List<ProductoModel> data;

    public boolean isSuccess() {
        return success;
    }

    public List<ProductoModel> getData() {
        return data;
    }
}
