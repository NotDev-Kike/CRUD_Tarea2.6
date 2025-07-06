package com.example.crud_tarea26;

public class LoginResponse {
    private boolean success;
    private String message;
    private String apiKey;
    private String nombre;
    private String id;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setId(String id) {
        this.id = id;
    }
}
