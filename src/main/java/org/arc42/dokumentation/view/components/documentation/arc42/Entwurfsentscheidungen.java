package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.Route;
import java.util.List;
import java.util.Optional;
import org.arc42.dokumentation.model.dao.arc42documentation.EntwurfsentscheidungDAO;
import org.arc42.dokumentation.model.dto.documentation.EntwurfsentscheidungDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.data.NotificationType;

@Route(value = "architekturentscheidungen", layout = MainLayout.class)
public class Entwurfsentscheidungen extends DocumentationView {

  private EntwurfsentscheidungDTO dto;
  private EntwurfsentscheidungDAO dao;

  private Grid<EntwurfsentscheidungDTO> grid;
  private TextField entscheidung;
  private TextField konsequenz;
  private TextField begruendung;
  private Select<EntwurfsentscheidungDTO.Wichtigkeit> wichtigkeitSelect;

  @Override
  public void init() {
    super.init();
    getArchitekturentscheidungen()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getArchitekturentscheidungen().setOpened(true);

    dao = EntwurfsentscheidungDAO.getInstance();
    List<EntwurfsentscheidungDTO> entwurfsentscheidungenDTOS = this.dao.findAll(url);

    this.grid = new Grid<>(EntwurfsentscheidungDTO.class, false);
    grid.addColumn(EntwurfsentscheidungDTO::getEntscheidung).setHeader("Entwurfsentscheidung");
    grid.addColumn(EntwurfsentscheidungDTO::getKonsequenz).setHeader("Konsequenz");
    grid.addColumn(EntwurfsentscheidungDTO::getBegruendung).setHeader("Begründung");
    grid.addColumn(EntwurfsentscheidungDTO::getWichtigkeit).setHeader("Wichtigkeit");
    grid.setItems(entwurfsentscheidungenDTOS);

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    entscheidung = new TextField("Entwurfsentscheidung");
    entscheidung.addFocusShortcut(Key.ENTER);
    entscheidung.focus();
    konsequenz = new TextField("Konsequenz");
    begruendung = new TextField("Begründung");

    wichtigkeitSelect = new Select<>();
    wichtigkeitSelect.setEmptySelectionAllowed(false);
    wichtigkeitSelect.setLabel("Wichtigkeit");
    wichtigkeitSelect.setItems(EntwurfsentscheidungDTO.Wichtigkeit.values());
    wichtigkeitSelect.setValue(EntwurfsentscheidungDTO.Wichtigkeit.NICHT_ERFASST);

    Button addEntwurfsentscheidung = new Button("Hinzufügen");
    addEntwurfsentscheidung.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    addEntwurfsentscheidung.addClickShortcut(Key.ENTER);
    horizontalLayout.add(entscheidung, konsequenz, begruendung, wichtigkeitSelect);

    Button edit = new Button("Ändern");
    Button delete = new Button("Löschen");
    edit.setVisible(false);
    delete.setVisible(false);
    addEntwurfsentscheidung.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    addEntwurfsentscheidung.addClickShortcut(Key.ENTER);

    HorizontalLayout buttonLayout = new HorizontalLayout(addEntwurfsentscheidung, edit, delete);
    HorizontalLayout gridLayout = new HorizontalLayout(grid);

    addEntwurfsentscheidung.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              if (!(entscheidung.getValue().isEmpty())) {
                EntwurfsentscheidungDTO entwurfsentscheidungDTO =
                    new EntwurfsentscheidungDTO(
                        entscheidung.getValue(),
                        konsequenz.getValue(),
                        begruendung.getValue(),
                        wichtigkeitSelect.getValue());
                dao.save(entwurfsentscheidungDTO);
                clearTextFieldValue();
                grid.deselectAll();
                updateList();
              } else {
                new NotificationWindow(
                    "Das Feld Entwurfsentscheidung muss ausgefüllt werden!",
                    NotificationType.SHORT,
                    NotificationType.NEUTRAL);
              }
            });

    grid.addSelectionListener(
        (SelectionListener<Grid<EntwurfsentscheidungDTO>, EntwurfsentscheidungDTO>)
            selection -> {
              Optional<EntwurfsentscheidungDTO> optionalDoku = selection.getFirstSelectedItem();
              if (optionalDoku.isPresent()) {
                dto = optionalDoku.get();
                entscheidung.setValue(dto.getEntscheidung());
                konsequenz.setValue(dto.getKonsequenz());
                begruendung.setValue(dto.getBegruendung());
                wichtigkeitSelect.setValue(dto.getWichtigkeit());
                addEntwurfsentscheidung.setVisible(false);
                edit.setVisible(true);
                delete.setVisible(true);
              } else {
                addEntwurfsentscheidung.setVisible(true);
                edit.setVisible(false);
                delete.setVisible(false);
              }
            });

    edit.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              dto.setEntscheidung(entscheidung.getValue());
              dto.setKonsequenz(konsequenz.getValue());
              dto.setBegruendung(begruendung.getValue());
              dto.setWichtigkeit(wichtigkeitSelect.getValue().getLabel());
              dao.update(dto);
              clearTextFieldValue();
              updateList();
              new NotificationWindow(
                  "Entwurfsentscheidung erfolgreich geändert!",
                  NotificationType.SHORT,
                  NotificationType.SUCCESS);
            });

    delete.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              dao.delete(dto);
              clearTextFieldValue();
              updateList();
              new NotificationWindow(
                  "Entwurfsentscheidung erfolgreich gelöscht!",
                  NotificationType.SHORT,
                  NotificationType.NEUTRAL);
            });

    VerticalLayout main = new VerticalLayout();
    main.setHorizontalComponentAlignment(Alignment.STRETCH, gridLayout);
    main.add(
        new BreadCrumbComponent(new String[] {"Entwurfsentscheidungen"}),
        gridLayout,
        horizontalLayout,
        buttonLayout);
    add(main);
  }

  public void updateList() {
    List<EntwurfsentscheidungDTO> entwurfsentscheidungenDTOS = this.dao.findAll(url);
    this.grid.setItems(entwurfsentscheidungenDTOS);
  }

  private void clearTextFieldValue() {
    entscheidung.clear();
    konsequenz.clear();
    begruendung.clear();
    wichtigkeitSelect.setValue(EntwurfsentscheidungDTO.Wichtigkeit.NICHT_ERFASST);
  }
}
