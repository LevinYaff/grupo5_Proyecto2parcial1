package com.proyect.cinegrupo5.repository;

import java.io.IOException;

import com.proyect.cinegrupo5.data.ClientesResponse;
import com.proyect.cinegrupo5.data.TicketsResponse;
import com.proyect.cinegrupo5.data.SalasResponse;

import retrofit2.Call;
import retrofit2.Response;

public class DatabaseRepositoryImpl {

    private static DatabaseRepositoryImpl INSTANCE;
    private DatabaseClient client;

    private DatabaseRepositoryImpl(String url, Long timeout) {
        client = new DatabaseClient(url, timeout);
    }

    public static DatabaseRepositoryImpl getInstance(String url, Long timeout) {
        if (INSTANCE == null) {
            synchronized (DatabaseRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DatabaseRepositoryImpl(url, timeout);
                }
            }
        }
        return INSTANCE;
    }

    public ClientesResponse consultarClientes() throws IOException {
        Call<ClientesResponse> call = client.getDatabase().ObtenerClientes();
        Response<ClientesResponse> response = call.execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            return null;
        }
    }

    public TicketsResponse consultarTickets() throws IOException {
        Call<TicketsResponse> call = client.getDatabase().ObtenerTickets();
        Response<TicketsResponse> response = call.execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            return null;
        }
    }

    public SalasResponse consultarSalas() throws IOException {
        Call<SalasResponse> call = client.getDatabase().ObtenerSalas();
        Response<SalasResponse> response = call.execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            return null;
        }
    }
}
