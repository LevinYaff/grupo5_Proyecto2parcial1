package com.proyect.cinegrupo5.data;


public class TicketsInfo extends AbstractEntity {

    private Integer ticketsid;
    private String pelicula;
    private String asiento;
    private String numerosala;
    private String tiposala;
    private String precio;

    public Integer getNumeroTicket() {
        return ticketsid;
    }
    public void setNumeroTicket(int numeroTicket) {
        this.ticketsid = numeroTicket;
    }
    public String getPelicula() {
        return pelicula;
    }
    public void setPelicula(String pelicula) {
        this.pelicula = pelicula;
    }
    public String getAsiento() {
        return asiento;
    }
    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }
    public String getNumeroSala() {
        return numerosala;
    }
    public void setNumeroSala(String numeroSala) {
        this.numerosala = numeroSala;
    }
    public String getTipoSala() {
        return tiposala;
    }
    public void setTipoSala(String tipoSala) {
        this.tiposala = tipoSala;
    }
    public String getPrecio() {
        return precio;
    }
    public void setPrecio(String precio) {
        this.precio = precio;
    }

}
