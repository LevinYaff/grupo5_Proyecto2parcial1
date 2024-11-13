package com.proyect.cinegrupo5.views.clientes;

import com.proyect.cinegrupo5.data.ClientesInfo;
import com.proyect.cinegrupo5.services.ClientesInfoService;
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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Clientes")
@Route("master-detail/:clientesInfoID?/:action?(edit)")
@Menu(order = 1, icon = "line-awesome/svg/user-circle-solid.svg")
public class ClientesView extends Div implements BeforeEnterObserver {

    private final String CLIENTESINFO_ID = "clientesInfoID";
    private final String CLIENTESINFO_EDIT_ROUTE_TEMPLATE = "master-detail/%s/edit";

    private final Grid<ClientesInfo> grid = new Grid<>(ClientesInfo.class, false);

    private TextField nombreCompleto;
    private TextField correo;
    private TextField telefono;
    private TextField ticketsComprado;

    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    private final BeanValidationBinder<ClientesInfo> binder;

    private ClientesInfo clientesInfo;

    private final ClientesInfoService clientesInfoService;

    public ClientesView(ClientesInfoService clientesInfoService) {
        this.clientesInfoService = clientesInfoService;
        addClassNames("clientes-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("nombreCompleto").setAutoWidth(true);
        grid.addColumn("correo").setAutoWidth(true);
        grid.addColumn("telefono").setAutoWidth(true);
        grid.addColumn("ticketsComprado").setAutoWidth(true);
        grid.setItems(query -> clientesInfoService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CLIENTESINFO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ClientesView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(ClientesInfo.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.clientesInfo == null) {
                    this.clientesInfo = new ClientesInfo();
                }
                binder.writeBean(this.clientesInfo);
                clientesInfoService.update(this.clientesInfo);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(ClientesView.class);
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
        Optional<Long> clientesInfoId = event.getRouteParameters().get(CLIENTESINFO_ID).map(Long::parseLong);
        if (clientesInfoId.isPresent()) {
            Optional<ClientesInfo> clientesInfoFromBackend = clientesInfoService.get(clientesInfoId.get());
            if (clientesInfoFromBackend.isPresent()) {
                populateForm(clientesInfoFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested clientesInfo was not found, ID = %s", clientesInfoId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ClientesView.class);
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
        nombreCompleto = new TextField("Nombre Completo");
        correo = new TextField("Correo");
        telefono = new TextField("Telefono");
        ticketsComprado = new TextField("Tickets Comprado");
        formLayout.add(nombreCompleto, correo, telefono, ticketsComprado);

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

    private void populateForm(ClientesInfo value) {
        this.clientesInfo = value;
        binder.readBean(this.clientesInfo);

    }
}
