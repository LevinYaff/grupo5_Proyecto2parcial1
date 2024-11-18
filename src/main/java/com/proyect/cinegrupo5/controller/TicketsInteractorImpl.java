package com.proyect.cinegrupo5.controller;

import com.proyect.cinegrupo5.data.TicketsResponse;
import com.proyect.cinegrupo5.repository.DatabaseRepositoryImpl;
import com.proyect.cinegrupo5.views.tickets.TicketViewModel;

public class TicketsInteractorImpl implements TicketsInteractor {

	private DatabaseRepositoryImpl modelo;
	private TicketViewModel vista;
	
	
	
	
	

	public TicketsInteractorImpl(TicketViewModel vista) {
		super();
		this.vista = vista;
		this.modelo = DatabaseRepositoryImpl.getInstance("https://apex.oracle.com", 30000L);
	}






	@Override
	public void consultarTickets() {
		try {
			TicketsResponse respuesta = this.modelo.consultarTickets();
			if(respuesta == null || respuesta.getCount() == 0 || respuesta.getItems() == null) {
				this.vista.mostrarMensajeError("No hay Tickets disponibles");
			}else {
				this.vista.mostrarTicketsEnGrid(respuesta.getItems());
			}
		}catch(Exception error) {
			error.printStackTrace();
		}
	}
		
	
	

}
