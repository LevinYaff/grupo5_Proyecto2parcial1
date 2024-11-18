package com.proyect.cinegrupo5.data;

public class SalasInfo extends AbstractEntity {
    private Integer numerosala;
    private String tiposala;
    private String seccion;
    private String fila;
    private Integer asientosdisponibles;

    // Getters and Setters
    public Integer getNumeroSala() {
        return numerosala;
    }

    public void setNumeroSala(Integer numeroSala) {
        this.numerosala = numeroSala;
    }

    public String getTipoSala() {
        return tiposala;
    }

    public void setTipoSala(String tipoSala) {
        this.tiposala = tipoSala;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public Integer getAsientosDisponibles() {
        return asientosdisponibles;
    }

    public void setAsientosDisponibles(Integer asientosDisponibles) {
        this.asientosdisponibles = asientosDisponibles;
    }
}