package com.proyect.cinegrupo5.views.tickets;

import com.proyect.cinegrupo5.data.TicketsInfo;
import com.proyect.cinegrupo5.services.TicketsInfoService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Tickets")
@Route("/:ticketsInfoID?/:action?(edit)")
@Menu(order = 0, icon = "line-awesome/svg/ticket-alt-solid.svg")
@RouteAlias("")
public class TicketsView extends Div implements BeforeEnterObserver {

    private final String TICKETSINFO_ID = "ticketsInfoID";
    private final String TICKETSINFO_EDIT_ROUTE_TEMPLATE = "/%s/edit";

    private final Grid<TicketsInfo> grid = new Grid<>(TicketsInfo.class, false);

    private TextField numeroTicket;
    private TextField pelicula;
    private TextField asiento;
    private TextField numeroSala;
    private TextField tipoSala;
    private TextField precio;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<TicketsInfo> binder;

    private TicketsInfo ticketsInfo;

    private final TicketsInfoService ticketsInfoService;

    public TicketsView(TicketsInfoService ticketsInfoService) {
        this.ticketsInfoService = ticketsInfoService;
        addClassNames("tickets-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("numeroTicket").setAutoWidth(true);
        grid.addColumn("pelicula").setAutoWidth(true);
        grid.addColumn("asiento").setAutoWidth(true);
        grid.addColumn("numeroSala").setAutoWidth(true);
        grid.addColumn("tipoSala").setAutoWidth(true);
        grid.addColumn("precio").setAutoWidth(true);
        grid.setItems(query -> ticketsInfoService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(TICKETSINFO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(TicketsView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(TicketsInfo.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(asiento).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("asiento");
        binder.forField(numeroSala).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("numeroSala");
        binder.forField(precio).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("precio");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.ticketsInfo == null) {
                    this.ticketsInfo = new TicketsInfo();
                }
                binder.writeBean(this.ticketsInfo);
                ticketsInfoService.update(this.ticketsInfo);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(TicketsView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> ticketsInfoId = event.getRouteParameters().get(TICKETSINFO_ID).map(Long::parseLong);
        if (ticketsInfoId.isPresent()) {
            Optional<TicketsInfo> ticketsInfoFromBackend = ticketsInfoService.get(ticketsInfoId.get());
            if (ticketsInfoFromBackend.isPresent()) {
                populateForm(ticketsInfoFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested ticketsInfo was not found, ID = %s", ticketsInfoId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(TicketsView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        numeroTicket = new TextField("Numero Ticket");
        pelicula = new TextField("Pelicula");
        asiento = new TextField("Asiento");
        numeroSala = new TextField("Numero Sala");
        tipoSala = new TextField("Tipo Sala");
        precio = new TextField("Precio");
        formLayout.add(numeroTicket, pelicula, asiento, numeroSala, tipoSala, precio);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
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
        binder.readBean(this.ticketsInfo);

    }
}
