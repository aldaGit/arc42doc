package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.Route;
import java.util.List;
import java.util.Optional;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dao.arc42documentation.HardwareDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.ImageDAOVerteilung;
import org.arc42.dokumentation.model.dto.documentation.HardwareDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.custom.UploadComponent;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.data.NotificationType;

@Route(value = "verteilungssicht/diagramm", layout = MainLayout.class)
public class VerteilungsSicht extends DocumentationView {

  private HardwareDTO hardwareDTO;
  private HardwareDAO hardwareDAO;
  private Grid<HardwareDTO> grid;
  private TextField hardwareName;
  private NumberField powerConsumption;
  private Checkbox isRenewable;

  @Override
  public void init() {
    super.init();
    getVerteilungssicht()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getVerteilungssicht().setOpened(true);
    getVerteilungssichtDiagramm()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    ARC42DAOAbstract dao = ImageDAOVerteilung.getInstance();
    VerticalLayout verticalLayout = new VerticalLayout();
    H3 header = new H3("Verteilungssicht-Diagramm");
    UploadComponent uploadComponent = new UploadComponent(dao, url);

    // Hardware-Komponenten Tabelle
    hardwareDAO = HardwareDAO.getInstance();
    List<HardwareDTO> hardwareList = hardwareDAO.findAll(url);
    grid = new Grid<>(HardwareDTO.class, false);
    grid.addColumn(HardwareDTO::getId).setHeader("ID");
    grid.addColumn(HardwareDTO::getName).setHeader("Hardware-Komponente");
    grid.addColumn(HardwareDTO::getPowerConsumption).setHeader("Täglicher Energieverbrauch in kWh");
    grid.addColumn(HardwareDTO::isRenewable).setHeader("Verwendet erneuerbare Energien?");
    grid.setItems(hardwareList);
    grid.setAllRowsVisible(true);
    HorizontalLayout gridLayout = new HorizontalLayout(grid);
    gridLayout.setSizeFull();

    hardwareName = new TextField("Hardware-Komponente");
    hardwareName.setRequiredIndicatorVisible(true);
    hardwareName.setRequired(true);
    powerConsumption = new NumberField("Täglicher Energieverbrauch in kWh");
    isRenewable = new Checkbox("verwendet erneuerbare Energien");
    Button addHardware = new Button("Hinzufügen");
    Button edit = new Button("Ändern");
    Button delete = new Button("Löschen");
    edit.setVisible(false);
    delete.setVisible(false);
    addHardware.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    addHardware.addClickShortcut(Key.ENTER);
    edit.addClickShortcut(Key.ENTER);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    FormLayout input = new FormLayout(hardwareName, powerConsumption, isRenewable);
    input.setSizeFull();
    input.setResponsiveSteps(
        // Use one column by default
        new FormLayout.ResponsiveStep("0", 1),
        // Use two columns, if the layout's width exceeds 320px
        new FormLayout.ResponsiveStep("320px", 2),
        // Use three columns, if the layout's width exceeds 500px
        new FormLayout.ResponsiveStep("500px", 3));
    HorizontalLayout buttons = new HorizontalLayout(addHardware, edit, delete);
    buttons.setSizeFull();

    // Zusammenfügen des Layouts

    verticalLayout.setSizeFull();
    verticalLayout.add(
        new BreadCrumbComponent(new String[] {"Verteilungssicht", "Diagramm"}),
        header,
        uploadComponent,
        gridLayout);
    verticalLayout.add(input, buttons);
    add(verticalLayout);

    // Selection-Listener, Buttons
    grid.addSelectionListener(
        (SelectionListener<Grid<HardwareDTO>, HardwareDTO>)
            selection -> {
              Optional<HardwareDTO> optionalDoku = selection.getFirstSelectedItem();
              if (optionalDoku.isPresent()) {
                hardwareDTO = optionalDoku.get();
                hardwareName.setValue(hardwareDTO.getName());
                powerConsumption.setValue(hardwareDTO.getPowerConsumption());
                isRenewable.setValue(hardwareDTO.isRenewable());
                addHardware.setVisible(false);
                edit.setVisible(true);
                delete.setVisible(true);
              } else {
                addHardware.setVisible(true);
                edit.setVisible(false);
                delete.setVisible(false);
                clearTextFieldValue();
              }
            });

    addHardware.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              if (!hardwareName.isEmpty()) {
                HardwareDTO hardware =
                    new HardwareDTO(
                        hardwareName.getValue(),
                        powerConsumption.getValue(),
                        isRenewable.getValue());
                hardwareDAO.save(hardware);
                clearTextFieldValue();
                updateList();
                grid.deselectAll();
              } else {
                new NotificationWindow(
                    "Es müssen alle Pflichtfelder ausgefüllt werden!",
                    NotificationType.SHORT,
                    NotificationType.ERROR);
              }
            });

    edit.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              hardwareDTO.setName(hardwareName.getValue());
              hardwareDTO.setPowerConsumption(powerConsumption.getValue());
              hardwareDTO.setRenewable(isRenewable.getValue());
              hardwareDAO.update(hardwareDTO);
              clearTextFieldValue();
              updateList();
              new NotificationWindow(
                  "Hardware-Komponente erfolgreich geändert!",
                  NotificationType.SHORT,
                  NotificationType.SUCCESS);
            });

    delete.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              hardwareDAO.delete(hardwareDTO);
              clearTextFieldValue();
              updateList();
              new NotificationWindow(
                  "Hardware-Komponente erfolgreich gelöscht!",
                  NotificationType.SHORT,
                  NotificationType.NEUTRAL);
            });
  }

  public void updateList() {
    List<HardwareDTO> hardwareDTOS = this.hardwareDAO.findAll(url);
    this.grid.setItems(hardwareDTOS);
  }

  private void clearTextFieldValue() {
    hardwareName.clear();
    powerConsumption.clear();
    isRenewable.clear();
  }
}