package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.Route;
import java.util.List;
import java.util.Optional;
import org.arc42.dokumentation.model.dao.arc42documentation.InterfaceDAO;
import org.arc42.dokumentation.model.dto.documentation.InterfaceDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.data.NotificationType;

@Route(value = "kontextabgrenzung/technischer-kontext", layout = MainLayout.class)
public class TechnischKontext extends DocumentationView {

  private Grid<InterfaceDTO> interfaceTable;
  private TextField nameField;
  private TextArea documentationArea;
  private IntegerField callsPerMonthField;
  private NumberField emmissionsField;
  private InterfaceDAO dao;
  private InterfaceDTO selected;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    super.beforeEnter(event);
  }

  @Override
  public void beforeLeave(BeforeLeaveEvent event) {
    super.beforeLeave(event);
  }

  @Override
  public void init() {
    super.init();
    // Brotkrümelnavigation
    getKontextabgrenzung().setOpened(true);
    getKontextabgrenzung()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getTechnischerKontext()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    // View-Inhalt
    dao = InterfaceDAO.getInstance();

    // Form-Layout
    nameField = new TextField("Schnittstelle");
    nameField.setRequiredIndicatorVisible(true);
    nameField.setRequired(true);
    documentationArea = new TextArea("Schnittstellendokumentation");
    callsPerMonthField = new IntegerField("Aufrufe pro Monat");
    callsPerMonthField.setStepButtonsVisible(true);
    callsPerMonthField.setHelperText("1 Monat entspricht ungefähr 4,3 Wochen");
    emmissionsField = new NumberField("CO₂-Emmissionen pro Aufruf in g");

    FormLayout interfaceForm = new FormLayout();
    interfaceForm.add(nameField, documentationArea, callsPerMonthField, emmissionsField);
    interfaceForm.setColspan(nameField, 2);
    interfaceForm.setColspan(documentationArea, 2);

    // Buttons
    Button addInterface = new Button("Hinzufügen");
    addInterface.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    addInterface.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    addInterface.addClickShortcut(Key.ENTER);
    Button edit = new Button("Ändern");
    edit.setVisible(false);
    Button delete = new Button("Löschen");
    delete.setVisible(false);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

    // Tabelle
    interfaceTable = new Grid<>(InterfaceDTO.class, false);
    interfaceTable.addColumn(InterfaceDTO::getId).setHeader("ID");
    interfaceTable.addColumn(InterfaceDTO::getName).setHeader("Schnittstelle");
    interfaceTable.addColumn(InterfaceDTO::getCallsPerMonth).setHeader("Aufrufe pro Monat");
    interfaceTable
        .addColumn(InterfaceDTO::getEmissions)
        .setHeader("CO₂-Emissionen pro Aufruf in g");
    interfaceTable
        .addColumn(InterfaceDTO::getEmissionsPerMonth)
        .setHeader("CO₂-Emissionen pro Monat in g");
    interfaceTable.setAllRowsVisible(true);

    List<InterfaceDTO> interfaces = dao.findAll(url);
    interfaceTable.setItems(interfaces);

    // Hinzufügen der Komponenten
    HorizontalLayout buttonLayout = new HorizontalLayout(addInterface, edit, delete);
    VerticalLayout verticalLayout =
        new VerticalLayout(
            new BreadCrumbComponent(new String[] {"Kontextabgrenzung", "Technisch"}),
            interfaceTable,
            interfaceForm,
            buttonLayout);
    verticalLayout.setSizeFull();
    add(verticalLayout);

    // EventListener
    interfaceTable.addSelectionListener(
        selectionEvent -> {
          Optional<InterfaceDTO> optionalInterface = selectionEvent.getFirstSelectedItem();
          if (optionalInterface.isPresent()) {
            selected = optionalInterface.get();
            nameField.setValue(selected.getName());
            documentationArea.setValue(selected.getDocumentation());
            callsPerMonthField.setValue(selected.getCallsPerMonth());
            emmissionsField.setValue(selected.getEmissions());
            addInterface.setVisible(false);
            edit.setVisible(true);
            delete.setVisible(true);
          } else {
            addInterface.setVisible(true);
            edit.setVisible(false);
            delete.setVisible(false);
            clearTextFieldValue();
          }
        });

    addInterface.addClickListener(
        buttonClickEvent -> {
          if (!nameField.getValue().isEmpty()) {
            InterfaceDTO interfaceDTO =
                new InterfaceDTO(
                    nameField.getValue(),
                    documentationArea.getValue(),
                    callsPerMonthField.getValue(),
                    emmissionsField.getValue());
            dao.save(interfaceDTO);
            clearTextFieldValue();
            interfaceTable.deselectAll();
            updateList();
            new NotificationWindow(
                "Schnittstellendokumentation erfolgreich hinzugefügt",
                NotificationType.SHORT,
                NotificationType.SUCCESS);
          } else {
            new NotificationWindow(
                "Es müssen alle Pflichtfelder ausgefüllt werden!",
                NotificationType.SHORT,
                NotificationType.NEUTRAL);
          }
        });

    edit.addClickListener(
        buttonClickEvent -> {
          selected.setName(nameField.getValue());
          selected.setDocumentation(documentationArea.getValue());
          selected.setCallsPerMonth(callsPerMonthField.getValue());
          selected.setEmissions(emmissionsField.getValue());
          selected.setEmissionsPerMonth(callsPerMonthField.getValue() * emmissionsField.getValue());
          dao.update(selected);
          clearTextFieldValue();
          updateList();
          new NotificationWindow(
              "Schnittstellendokumentation erfolgreich geändert!",
              NotificationType.SHORT,
              NotificationType.SUCCESS);
        });

    delete.addClickListener(
        buttonClickEvent -> {
          dao.delete(selected);
          clearTextFieldValue();
          updateList();
          new NotificationWindow(
              "Schnittstellendokumentation erfolgreich gelöscht!",
              NotificationType.SHORT,
              NotificationType.NEUTRAL);
        });
  }

  public void updateList() {
    List<InterfaceDTO> interfaces = this.dao.findAll(url);
    this.interfaceTable.setItems(interfaces);
  }

  private void clearTextFieldValue() {
    nameField.clear();
    documentationArea.clear();
    callsPerMonthField.clear();
    emmissionsField.clear();
  }
}
