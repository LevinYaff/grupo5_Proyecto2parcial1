package com.proyect.cinegrupo5.data;

import jakarta.persistence.Entity;

@Entity
public class TicketsInfo extends AbstractEntity {

    private String numeroTicket;
    private String pelicula;
    private Integer asiento;
    private Integer numeroSala;
    private String tipoSala;
    private Integer precio;

    public String getNumeroTicket() {
        return numeroTicket;
    }
    public void setNumeroTicket(String numeroTicket) {
        this.numeroTicket = numeroTicket;
    }
    public String getPelicula() {
        return pelicula;
    }
    public void setPelicula(String pelicula) {
        this.pelicula = pelicula;
    }
    public Integer getAsiento() {
        return asiento;
    }
    public void setAsiento(Integer asiento) {
        this.asiento = asiento;
    }
    public Integer getNumeroSala() {
        return numeroSala;
    }
    public void setNumeroSala(Integer numeroSala) {
        this.numeroSala = numeroSala;
    }
    public String getTipoSala() {
        return tipoSala;
    }
    public void setTipoSala(String tipoSala) {
        this.tipoSala = tipoSala;
    }
    public Integer getPrecio() {
        return precio;
    }
    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

}
