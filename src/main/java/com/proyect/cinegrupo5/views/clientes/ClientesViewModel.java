package com.proyect.cinegrupo5.views.clientes;

import java.util.List;

import com.proyect.cinegrupo5.data.ClientesInfo;

public interface ClientesViewModel {

	
	void mostrarClientesEnGrid(List<ClientesInfo> Clientes);
	void mostrarMensajeError(String mensaje);
	void mostrarMensajeExito(String mensaje);
}
