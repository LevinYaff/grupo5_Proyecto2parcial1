package com.proyect.cinegrupo5.data;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;

@Entity
public class ClientesInfo extends AbstractEntity {

    private String nombreCompleto;
    @Email
    private String correo;
    private String telefono;
    private String ticketsComprado;

    public String getNombreCompleto() {
        return nombreCompleto;
    }
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getTicketsComprado() {
        return ticketsComprado;
    }
    public void setTicketsComprado(String ticketsComprado) {
        this.ticketsComprado = ticketsComprado;
    }

}
