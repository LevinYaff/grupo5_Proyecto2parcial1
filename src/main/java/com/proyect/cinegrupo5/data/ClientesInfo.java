package com.proyect.cinegrupo5.data;

public class ClientesInfo extends AbstractEntity {

	private String nombre;
	private String identidad;
	private String correo;
	private String telefono;
	private String tickets;

	public String getNumeroIdentidad() {
		return identidad;
	}

	public void setNumeroIdentidad(String numeroIdentidad) {
		this.identidad = numeroIdentidad;
	}

	public String getNombreCompleto() {
		return nombre;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombre = nombreCompleto;
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
		return tickets;
	}

	public void setTicketsComprado(String ticketsComprado) {
		this.tickets = ticketsComprado;
	}

}
