package com.proyect.cinegrupo5.controller;

import com.proyect.cinegrupo5.data.ClientesResponse;
import com.proyect.cinegrupo5.repository.DatabaseRepositoryImpl;
import com.proyect.cinegrupo5.views.clientes.ClientesViewModel;

public class ClientesInteractorImpl implements ClientesInteractor{
	
	private DatabaseRepositoryImpl modelo;
	private ClientesViewModel vista;
	
	
	
	
	

	public ClientesInteractorImpl(ClientesViewModel vista) {
		super();
		this.vista = vista;
		this.modelo = DatabaseRepositoryImpl.getInstance("https://apex.oracle.com", 30000L);
	}






	@Override
	public void consultarClientes() {
		try {
			ClientesResponse respuesta = this.modelo.consultarClientes();
			if(respuesta == null || respuesta.getCount() == 0 || respuesta.getItems() == null) {
				this.vista.mostrarMensajeError("No hay Clientes disponibles");
			}else {
				this.vista.mostrarClientesEnGrid(respuesta.getItems());
			}
		}catch(Exception error) {
			error.printStackTrace();
		}
	}
		
	
	

}
