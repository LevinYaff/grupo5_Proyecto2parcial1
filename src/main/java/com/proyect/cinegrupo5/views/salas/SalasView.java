package com.proyect.cinegrupo5.views.salas;

import com.proyect.cinegrupo5.controller.SalasInteractor;
import com.proyect.cinegrupo5.controller.SalasInteractorImpl;
//import com.proyect.cinegrupo5.controller.TicketsInteractor;
import com.proyect.cinegrupo5.data.SalasInfo;
//import com.proyect.cinegrupo5.data.TicketsInfo;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.data.binder.BeanValidationBinder;
//import com.vaadin.flow.data.binder.ValidationException;
//import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.Menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@PageTitle("Salas")
@Route(value = "salas/:salasInfoID?/:action?(edit)")
@Menu(order = 1, icon = "line-awesome/svg/film-solid.svg")
public class SalasView extends Div implements BeforeEnterObserver,SalasViewModel {

    private final String SALASINFO_ID = "salasInfoID";
    private final String SALASINFO_EDIT_ROUTE_TEMPLATE = "salas/%s/edit";

    private final Grid<SalasInfo> grid = new Grid<>(SalasInfo.class, false);

    private TextField numerosala;
    private ComboBox<String> tiposala;
    private ComboBox<String> seccion;
    private TextField fila;
    private TextField asientosdisponibles;

    private final Button cancelar = new Button("Cancelar", new Icon(VaadinIcon.CLOSE_SMALL));
    private final Button guardar = new Button("Guardar", new Icon(VaadinIcon.CHECK));


    private SalasInfo salasInfo;
    private List<SalasInfo> salas;
    private SalasInteractor controlador;

    

    public SalasView() {
        addClassNames("salas-view");
        
        controlador = new SalasInteractorImpl(this);
        salas = new ArrayList<>();

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("numeroSala").setAutoWidth(true).setHeader("Número de Sala");
        grid.addColumn("tipoSala").setAutoWidth(true).setHeader("Tipo de Sala");
        grid.addColumn("seccion").setAutoWidth(true).setHeader("Sección");
        grid.addColumn("fila").setAutoWidth(true).setHeader("Fila");
        grid.addColumn("asientosDisponibles").setAutoWidth(true).setHeader("Asientos Disponibles");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SALASINFO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(SalasView.class);
            }
        });

        // Configure Form
        //binder = new BeanValidationBinder<>(SalasInfo.class);

        // Bind fields. This is where you'd define field validation
        //binder.forField(numeroSala)
                //.withConverter(new StringToIntegerConverter("Solo se permiten números"))
                //.bind("numeroSala");
        //binder.forField(asientosDisponibles)
               // .withConverter(new StringToIntegerConverter("Solo se permiten números"))
                //.bind("asientosDisponibles");

        //binder.bindInstanceFields(this);

        cancelar.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        guardar.addClickListener(e -> {
            if (this.salasInfo == null) {
			    this.salasInfo = new SalasInfo();
			}
            // binder.writeBean(this.salasInfo);
			clearForm();
			refreshGrid();
			mostrarMensajeExito("Sala guardada correctamente");
			UI.getCurrent().navigate(SalasView.class);
        });
        controlador.consultarSalas();

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> salasInfoId = event.getRouteParameters().get(SALASINFO_ID).map(Long::parseLong);
        if (salasInfoId.isPresent()) {
            // Add your logic here to load the data
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
        
        numerosala = new TextField("Número de Sala");
        numerosala.setClearButtonVisible(true);
        numerosala.setPrefixComponent(VaadinIcon.FILM.create());
        
        tiposala = new ComboBox<>("Tipo de Sala");
        tiposala.setItems("2D", "3D", "4D", "PREMIUM");
        tiposala.setPlaceholder("Seleccione tipo de sala");
        
        seccion = new ComboBox<>("Sección");
        seccion.setItems("FRONTAL", "MEDIO", "TRASERA", "DERECHA", "IZQUIERDA", "PALCO");
        seccion.setPlaceholder("Seleccione la sección");
        
        fila = new TextField("Fila");
        fila.setClearButtonVisible(true);
        fila.setPrefixComponent(VaadinIcon.GRID.create());
        
        asientosdisponibles = new TextField("Asientos Disponibles");
        asientosdisponibles.setClearButtonVisible(true);
        asientosdisponibles.setPrefixComponent(VaadinIcon.SEARCH.create());        
        
        formLayout.add(numerosala, tiposala, seccion, fila, asientosdisponibles);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }
    

    private void createButtonLayout(Div editorLayoutDiv) 
    {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        
        cancelar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        guardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        buttonLayout.add(guardar, cancelar);
        editorLayoutDiv.add(buttonLayout);
    }
    
    private void createGridLayout(SplitLayout splitLayout) 
    {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }
    

    private void refreshGrid() 
    {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }
    

    private void clearForm() 
    {
        populateForm(null);
    }

    
    private void populateForm(SalasInfo value) 
    {
        this.salasInfo = value;
       // binder.readBean(this.salasInfo);
    }
    

    public void mostrarMensajeExito(String mensaje) 
    {
        Notification notification = Notification.show(mensaje);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Position.TOP_END);
    }

    public void mostrarMensajeError(String mensaje) 
    {
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);

        Div text = new Div(new Text(mensaje));

        Button closeButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.setAriaLabel("Close");
        closeButton.addClickListener(event -> notification.close());

        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
        layout.setAlignItems(Alignment.CENTER);

        notification.add(layout);
        notification.setPosition(Position.TOP_CENTER);
        notification.open();
    }

	@Override
	public void mostrarSalasEnGrid(List<SalasInfo> items) {
		Collection<SalasInfo> itemsCollection = items;
		this.salas = items;
		grid.setItems(itemsCollection);
		
	}

	//@Override
	//public void mostrarMensajeError(String ) {
	// TODO Auto-generated method stub
		
	

	//@Override
	//public void mostrarMensajeExito(String ) {
		// TODO Auto-generated method stub
		
	}
