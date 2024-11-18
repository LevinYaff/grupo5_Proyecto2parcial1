package com.proyect.cinegrupo5.repository;

import com.proyect.cinegrupo5.data.ClientesResponse;
import com.proyect.cinegrupo5.data.TicketsResponse;
import com.proyect.cinegrupo5.data.SalasResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface DatabaseRepository {

    @Headers({
        "Accept: application/vnd.github.v3.full+json",
        "User-Agent: App Cine"
    })
    @GET("/pls/apex/levin241/appcine/clientes")
    Call<ClientesResponse> ObtenerClientes();

    @Headers({
        "Accept: application/vnd.github.v3.full+json",
        "User-Agent: App Cine"
    })
    @GET("/pls/apex/levin241/appcine/tickets")
    Call<TicketsResponse> ObtenerTickets();

    @Headers({
        "Accept: application/vnd.github.v3.full+json",
        "User-Agent: App Cine"
    })
    @GET("/pls/apex/levin241/appcine/salas")
    Call<SalasResponse> ObtenerSalas();
}
