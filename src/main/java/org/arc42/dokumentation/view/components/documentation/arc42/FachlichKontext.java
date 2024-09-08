package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import java.util.List;
import java.util.Optional;
import org.arc42.dokumentation.model.dao.arc42documentation.FachlichKontextDAO;
import org.arc42.dokumentation.model.dto.documentation.FachlicherKontextDTO;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.data.NotificationType;

@Route(value = "kontextabgrenzung/fachlicher-kontext", layout = MainLayout.class)
public class FachlichKontext extends DocumentationView {

  private Grid<FachlicherKontextDTO> grid;
  private FachlichKontextDAO dao;
  private FachlicherKontextDTO dto;

  private TextField partner;
  private TextField input;
  private TextField output;
  private TextField beschreibung;
  private TextField risiken;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    event.getRouteParameters().get("arcId").get();
    super.beforeEnter(event);
  }

  @Override
  public void init() {
    super.init();
    getKontextabgrenzung()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getKontextabgrenzung().setOpened(true);
    getFachlicherKontext()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    dao = FachlichKontextDAO.getInstance();

    List<FachlicherKontextDTO> kontextDTOS = dao.findAll(url);

    grid = new Grid<>(FachlicherKontextDTO.class, false);
    grid.addColumn(FachlicherKontextDTO::getKommunikationspartner)
        .setHeader("Kommunikationspartner");
    grid.addColumn(FachlicherKontextDTO::getInput).setHeader("Input");
    grid.addColumn(FachlicherKontextDTO::getOutput).setHeader("Output");
    grid.addColumn(FachlicherKontextDTO::getBeschreibung).setHeader("Beschreibung");
    grid.addColumn(FachlicherKontextDTO::getRisiken).setHeader("Risiken");
    grid.setItems(kontextDTOS);

    String fieldWidth = "170px";
    partner = new TextField("Kommunikationspartner");
    partner.setWidth(fieldWidth);
    input = new TextField("Input");
    input.setWidth(fieldWidth);
    output = new TextField("Output");
    output.setWidth(fieldWidth);
    beschreibung = new TextField("Beschreibung");
    beschreibung.setWidth(fieldWidth);
    risiken = new TextField("Risiken");
    risiken.setWidth(fieldWidth);

    Button addPartner = new Button("Hinzufügen");
    addPartner.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    addPartner.addClickShortcut(Key.ENTER);

    Button edit = new Button("Ändern");
    Button delete = new Button("Löschen");
    edit.setVisible(false);
    delete.setVisible(false);
    addPartner.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    addPartner.addClickShortcut(Key.ENTER);

    HorizontalLayout gridLayout = new HorizontalLayout(grid);
    gridLayout.setWidth("97%");
    HorizontalLayout fieldLayout =
        new HorizontalLayout(partner, input, output, beschreibung, risiken);
    HorizontalLayout buttonLayout = new HorizontalLayout(addPartner, edit, delete);

    VerticalLayout main = new VerticalLayout();
    main.setHorizontalComponentAlignment(Alignment.STRETCH, gridLayout);
    main.add(gridLayout, fieldLayout, buttonLayout);
    add(main);

    addPartner.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              if (!(partner.getValue().isEmpty())) {
                FachlicherKontextDTO dto =
                    new FachlicherKontextDTO(
                        partner.getValue(),
                        input.getValue(),
                        output.getValue(),
                        beschreibung.getValue(),
                        risiken.getValue());
                dao.save(dto);
                clearTextFieldValues();
                grid.deselectAll();
                grid.setItems(dao.findAll(url));
              } else {
                new NotificationWindow(
                    "Kommunikationspartner muss ausgefüllt werden!",
                    NotificationType.SHORT,
                    NotificationType.NEUTRAL);
              }
            });

    grid.addSelectionListener(
        (SelectionListener<Grid<FachlicherKontextDTO>, FachlicherKontextDTO>)
            selection -> {
              Optional<FachlicherKontextDTO> optionalDoku = selection.getFirstSelectedItem();
              if (optionalDoku.isPresent()) {
                dto = optionalDoku.get();
                partner.setValue(dto.getKommunikationspartner());
                input.setValue(dto.getInput());
                output.setValue(dto.getOutput());
                beschreibung.setValue(dto.getBeschreibung());
                risiken.setValue(dto.getRisiken());
                addPartner.setVisible(false);
                edit.setVisible(true);
                delete.setVisible(true);
              } else {
                addPartner.setVisible(true);
                edit.setVisible(false);
                delete.setVisible(false);
              }
            });

    edit.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              dto.setKommunikationspartner(partner.getValue());
              dto.setInput(input.getValue());
              dto.setOutput(output.getValue());
              dto.setBeschreibung(beschreibung.getValue());
              dto.setRisiken(risiken.getValue());
              dao.update(dto);
              clearTextFieldValues();
              grid.setItems(dao.findAll(url));
              new NotificationWindow(
                  "Fachlicher Kontext erfolgreich geändert!",
                  NotificationType.SHORT,
                  NotificationType.SUCCESS);
            });

    delete.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              dao.delete(dto);
              clearTextFieldValues();
              grid.setItems(dao.findAll(url));
              new NotificationWindow(
                  "Fachlicher Kontext erfolgreich gelöscht!",
                  NotificationType.SHORT,
                  NotificationType.NEUTRAL);
            });
  }

  private void clearTextFieldValues() {
    partner.clear();
    input.clear();
    output.clear();
    beschreibung.clear();
    risiken.clear();
  }
}
