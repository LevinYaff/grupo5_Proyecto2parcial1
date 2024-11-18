package com.proyect.cinegrupo5.views.tickets;

import com.proyect.cinegrupo5.controller.TicketsInteractor;
import com.proyect.cinegrupo5.controller.TicketsInteractorImpl;
import com.proyect.cinegrupo5.data.ClientesInfo;
import com.proyect.cinegrupo5.data.TicketsInfo;
//import com.proyect.cinegrupo5.services.ClientesInfoService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
//import com.vaadin.flow.component.datepicker.DatePicker;
//import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
//import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
//mport org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Tickets")
@Route("/:ticketsInfoID?/:action?(edit)")
@Menu(order = 0, icon = "line-awesome/svg/ticket-alt-solid.svg")
public class TicketsView extends Div implements BeforeEnterObserver, TicketViewModel {

    private final String TICKETSINFO_ID = "ticketsInfoID";
    private final String TICKETSINFO_EDIT_ROUTE_TEMPLATE = "tickets-detail/%s/edit";

    private final Grid<TicketsInfo> grid = new Grid<>(TicketsInfo.class, false);

    private TextField ticketsid;
    private TextField pelicula;
    private TextField asiento;
    private TextField numeroSala;
    private TextField tipoSala;
    private TextField precio;

    private final Button cancel = new Button("Cancelar", new Icon(VaadinIcon.CLOSE_SMALL));
    private final Button save = new Button("Guardar", new Icon(VaadinIcon.CHECK));

    private TicketsInfo ticketsInfo;
    private List<TicketsInfo> tickets;
    private TicketsInteractor controlador;

    public TicketsView() {
        addClassNames("tickets-view");

        controlador = new TicketsInteractorImpl(this);
        tickets = new ArrayList<>();

        // Crear diseño principal
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configurar la tabla
        grid.addColumn("numeroTicket").setAutoWidth(true).setHeader("Número de Ticket");
        grid.addColumn("pelicula").setAutoWidth(true);
        grid.addColumn("asiento").setAutoWidth(true).setHeader("Asiento");
        grid.addColumn("numeroSala").setAutoWidth(true).setHeader("Sala");
        grid.addColumn("tipoSala").setAutoWidth(true).setHeader("Tipo de Sala");
        grid.addColumn("precio").setAutoWidth(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(TICKETSINFO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(TicketsView.class);
            }
        });

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.ticketsInfo == null) {
                    this.ticketsInfo = new TicketsInfo();
                }
                clearForm();
                refreshGrid();
                mostrarMensajeExito("Datos de ticket actualizados correctamente.");
                UI.getCurrent().navigate(TicketsView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                mostrarMensajeError("Error al actualizar los datos. Otra persona modificó el registro.");
            }
        });

        controlador.consultarTickets();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> ticketsInfoIdString = event.getRouteParameters().get(TICKETSINFO_ID);
        if (ticketsInfoIdString.isPresent()) {
            try {
                Long ticketsInfoId = Long.parseLong(ticketsInfoIdString.get());
                Optional<TicketsInfo> ticketsInfoFromBackend = Optional.empty(); 
                if (ticketsInfoFromBackend.isPresent()) {
                    populateForm(ticketsInfoFromBackend.get());
                } else {
                    mostrarMensajeError(String.format("No se encontró el ticket solicitado, ID = %s", ticketsInfoId));
                    refreshGrid();
                    event.forwardTo(TicketsView.class);
                }
            } catch (NumberFormatException e) {
                mostrarMensajeError("El ID proporcionado no es válido.");
                refreshGrid();
                event.forwardTo(TicketsView.class);
            }
        } else {
            clearForm();
            refreshGrid();
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();

        ticketsid = new TextField("Número de Ticket");
        ticketsid.setClearButtonVisible(true);
        ticketsid.setPrefixComponent(VaadinIcon.TICKET.create());

        pelicula = new TextField("Película");
        pelicula.setClearButtonVisible(true);
        pelicula.setPrefixComponent(VaadinIcon.FILM.create());

        asiento = new TextField("Asiento");
        asiento.setClearButtonVisible(true);
        //asiento.setPrefixComponent(VaadinIcon.SEAT.create());

        numeroSala = new TextField("Sala");
        numeroSala.setClearButtonVisible(true);
        numeroSala.setPrefixComponent(VaadinIcon.HOME_O.create());

        tipoSala = new TextField("Tipo de Sala");
        tipoSala.setClearButtonVisible(true);
        //tipoSala.setPrefixComponent(VaadinIcon.LAYERS.create());

        precio = new TextField("Precio");
        precio.setClearButtonVisible(true);
        precio.setPrefixComponent(VaadinIcon.MONEY.create());

        formLayout.add(ticketsid, pelicula, asiento, numeroSala, tipoSala, precio);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(TicketsInfo value) {
        this.ticketsInfo = value;
        if (value == null) {
        	ticketsid.clear();
            pelicula.clear();
            asiento.clear();
            numeroSala.clear();
            tipoSala.clear();
            precio.clear();
        } else {
            // Utiliza un valor predeterminado si `numeroTicket` es 0
        	ticketsid.setValue(value.getNumeroTicket() != 0 ? String.valueOf(value.getNumeroTicket()) : "");
            pelicula.setValue(value.getPelicula() != null ? value.getPelicula() : "");
            asiento.setValue(value.getAsiento() != null ? value.getAsiento() : "");
            numeroSala.setValue(value.getNumeroSala() != null ? value.getNumeroSala() : "");
            tipoSala.setValue(value.getTipoSala() != null ? value.getTipoSala() : "");
            precio.setValue(value.getPrecio() != null ? value.getPrecio().toString() : "");
        }

    }

    public void mostrarMensajeError(String mensaje) {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        Div text = new Div(new Text(mensaje));
        Button closeButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.setAriaLabel("Cerrar");
        closeButton.addClickListener(event -> notification.close());
        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setAlignItems(Alignment.CENTER);
        notification.add(layout);
        notification.open();
    }

    public void mostrarMensajeExito(String mensaje) {
        Notification notification = Notification.show(mensaje);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.open();
    }

	@Override
	public void mostrarTicketsEnGrid(List<TicketsInfo> items) {
		Collection<TicketsInfo> itemsCollection = items;
		this.tickets = items;
		grid.setItems(itemsCollection);
		
	}

	
}
