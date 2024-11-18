package com.proyect.cinegrupo5.views.tickets;

import java.util.List;

import com.proyect.cinegrupo5.data.*;

public interface TicketViewModel {

	void mostrarTicketsEnGrid(List<TicketsInfo> items);
	void mostrarMensajeError(String mensaje);
	void mostrarMensajeExito(String mensaje);
}
