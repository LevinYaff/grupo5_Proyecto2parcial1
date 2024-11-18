package com.proyect.cinegrupo5.views.salas;

import java.util.List;
import com.proyect.cinegrupo5.data.SalasInfo;
import com.proyect.cinegrupo5.data.TicketsInfo;

public interface SalasViewModel {
	void mostrarSalasEnGrid(List<SalasInfo> items);
	void mostrarMensajeError(String mensaje);
	void mostrarMensajeExito(String mensaje);
}
