package com.proyect.cinegrupo5.views.clientes;

import com.proyect.cinegrupo5.controller.ClientesInteractor;
import com.proyect.cinegrupo5.controller.ClientesInteractorImpl;
import com.proyect.cinegrupo5.data.ClientesInfo;
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

@PageTitle("Clientes")
@Route("master-detail/:clientesInfoID?/:action?(edit)")
@Menu(order = 1, icon = "line-awesome/svg/user-circle-solid.svg")
public class ClientesView extends Div implements BeforeEnterObserver, ClientesViewModel {

    private final String CLIENTESINFO_ID = "clientesInfoID";
    private final String CLIENTESINFO_EDIT_ROUTE_TEMPLATE = "master-detail/%s/edit";

    private final Grid<ClientesInfo> grid = new Grid<>(ClientesInfo.class, false);

    private TextField numeroIdentidad;
    private TextField nombreCompleto;
    private TextField correo;
    private TextField telefono;
    private TextField ticketsComprado;

    private final Button cancel = new Button("Cancelar", new Icon(VaadinIcon.CLOSE_SMALL));
    private final Button save = new Button("Guardar", new Icon(VaadinIcon.CHECK));

    private ClientesInfo clientesInfo;
    private List<ClientesInfo> clientes;
    private ClientesInteractor controlador;

    public ClientesView() {
        //this.clientesInfoService = clientesInfoService;
        addClassNames("clientes-view");

        controlador = new ClientesInteractorImpl(this);
        clientes = new ArrayList<>();
        
        // Cre
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("numeroIdentidad").setAutoWidth(true).setHeader("Número de Identidad");
        grid.addColumn("nombreCompleto").setAutoWidth(true);
        grid.addColumn("correo").setAutoWidth(true);
        grid.addColumn("telefono").setAutoWidth(true);
        grid.addColumn("ticketsComprado").setAutoWidth(true);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CLIENTESINFO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ClientesView.class);
            }
        });

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.clientesInfo == null) {
                    this.clientesInfo = new ClientesInfo();
                }
  
                clearForm();
                refreshGrid();
                mostrarMensajeExito("Datos actualizados correctamente.");
                UI.getCurrent().navigate(ClientesView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                mostrarMensajeError("Error al actualizar los datos. Otra persona ha modificado el registro mientras realizabas los cambios.");
            }
        });
        
        controlador.consultarClientes();
    }

    	@Override
    	public void beforeEnter(BeforeEnterEvent event) {
    	    Optional<String> clientesInfoIdString = event.getRouteParameters().get(CLIENTESINFO_ID);
    	    if (clientesInfoIdString.isPresent()) {
    	        try {
    	            Long clientesInfoId = Long.parseLong(clientesInfoIdString.get());
    	            // Aquí puedes buscar el cliente en el backend
    	            Optional<ClientesInfo> clientesInfoFromBackend = Optional.empty(); // Reemplazar con la lógica real
    	            if (clientesInfoFromBackend.isPresent()) {
    	                populateForm(clientesInfoFromBackend.get());
    	            } else {
    	                mostrarMensajeError(String.format("El cliente solicitado no fue encontrado, ID = %s", clientesInfoId));
    	                refreshGrid();
    	                event.forwardTo(ClientesView.class);
    	            }
    	        } catch (NumberFormatException e) {
    	            mostrarMensajeError("El ID proporcionado no es válido.");
    	            refreshGrid();
    	            event.forwardTo(ClientesView.class);
    	        }
    	    } else {
    	        clearForm();
    	        refreshGrid();
    	    }
    	}


    private void createEditorLayout(SplitLayout splitLayout) 
    {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        
        numeroIdentidad = new TextField("Número de Identidad");
        numeroIdentidad.setClearButtonVisible(true);
        numeroIdentidad.setPrefixComponent(VaadinIcon.CLIPBOARD_USER.create());
        
        nombreCompleto = new TextField("Nombre Completo");
        nombreCompleto.setClearButtonVisible(true);
        nombreCompleto.setPrefixComponent(VaadinIcon.USER.create());
        
        correo = new TextField("Correo");
        correo.setClearButtonVisible(true);
        correo.setPrefixComponent(VaadinIcon.MAILBOX.create());
        
        telefono = new TextField("Teléfono");
        telefono.setClearButtonVisible(true);
        telefono.setPrefixComponent(VaadinIcon.PHONE.create());
        
        ticketsComprado = new TextField("Tickets Comprado");
        ticketsComprado.setClearButtonVisible(true);
        
        formLayout.add(numeroIdentidad, nombreCompleto, correo, telefono, ticketsComprado);

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

    private void populateForm(ClientesInfo value) {
        this.clientesInfo = value;
        if (value == null) {
            numeroIdentidad.clear();
            nombreCompleto.clear();
            correo.clear();
            telefono.clear();
            ticketsComprado.clear();
        } else {
            numeroIdentidad.setValue(value.getNumeroIdentidad() != null ? value.getNumeroIdentidad() : "");
            nombreCompleto.setValue(value.getNombreCompleto() != null ? value.getNombreCompleto() : "");
            correo.setValue(value.getCorreo() != null ? value.getCorreo() : "");
            telefono.setValue(value.getTelefono() != null ? value.getTelefono() : "");
            ticketsComprado.setValue(value.getTicketsComprado() != null ? value.getTicketsComprado().toString() : "");
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
	public void mostrarClientesEnGrid(List<ClientesInfo> items) {
		Collection<ClientesInfo> itemsCollection = items;
		this.clientes = items;
		grid.setItems(itemsCollection);
		
	}

	//@Override
	/*
	 * public void mostrarMensajeError(String mensaje) { // TODO Auto-generated
	 * method stub
	 * 
	 * }
	 */

	/*@Override
	public void mostrarMensajeExito(String mensaje) {
		// TODO Auto-generated method stub
		
	}*/
}