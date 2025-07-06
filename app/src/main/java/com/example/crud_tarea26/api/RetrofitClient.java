package com.example.crud_tarea26.api;

import com.example.crud_tarea26.auth.DigestAuthenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://192.168.100.116/api/";

    private static OkHttpClient buildClient(String username, String password, String apiKey) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            builder.authenticator(new DigestAuthenticator(username, password));
        }

        builder.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder();

            if (apiKey != null && !apiKey.isEmpty()) {
                requestBuilder.addHeader("X-API-KEY", apiKey);
            }

            return chain.proceed(requestBuilder.build());
        });

        return builder.build();
    }

    public static Retrofit getRetrofitClient() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit getRetrofitClient(String username, String password, String apiKey) {
        OkHttpClient client = buildClient(username, password, apiKey);
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getApiService() {
        return getRetrofitClient().create(ApiService.class);
    }

    public static ApiService getAuthenticatedApiService(String username, String password, String apiKey) {
        Retrofit retrofit = getRetrofitClient(username, password, apiKey);
        return retrofit.create(ApiService.class);
    }
}
