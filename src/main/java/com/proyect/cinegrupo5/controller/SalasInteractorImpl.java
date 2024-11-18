package com.proyect.cinegrupo5.controller;

import com.proyect.cinegrupo5.data.SalasResponse;
import com.proyect.cinegrupo5.repository.DatabaseRepositoryImpl;
import com.proyect.cinegrupo5.views.salas.SalasViewModel;

public class SalasInteractorImpl implements SalasInteractor {

	private DatabaseRepositoryImpl modelo;
	private SalasViewModel vista;
	
	
	
	
	

	public SalasInteractorImpl(SalasViewModel vista) {
		super();
		this.vista = vista;
		this.modelo = DatabaseRepositoryImpl.getInstance("https://apex.oracle.com", 30000L);
	}






	@Override
	public void consultarSalas() {
		try {
			SalasResponse respuesta = this.modelo.consultarSalas();
			if(respuesta == null || respuesta.getCount() == 0 || respuesta.getItems() == null) {
				this.vista.mostrarMensajeError("No hay Salas disponibles");
			}else {
				this.vista.mostrarSalasEnGrid(respuesta.getItems());
			}
		}catch(Exception error) {
			error.printStackTrace();
		}
	}
		
	
	
}
