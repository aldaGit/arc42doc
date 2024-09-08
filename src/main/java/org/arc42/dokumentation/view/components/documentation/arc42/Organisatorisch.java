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
import org.arc42.dokumentation.model.dao.arc42documentation.OrganisatorischDAO;
import org.arc42.dokumentation.model.dto.documentation.OrganisatorischDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.data.NotificationType;

@Route(value = "randbedingungen/organisatorisch", layout = MainLayout.class)
public class Organisatorisch extends DocumentationView {

  private OrganisatorischDAO dao;
  private Grid<OrganisatorischDTO> orgG;
  private TextField randbedingung;
  private TextField erlaueterung;
  private OrganisatorischDTO organisatorischDTO;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    event.getRouteParameters().get("arcId").get();
    super.beforeEnter(event);
  }

  @Override
  public void init() {
    super.init();
    this.dao = OrganisatorischDAO.getInstance();
    getRandbedingunen()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getRandbedingunen().setOpened(true);
    getOrganisatorisch()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    List<OrganisatorischDTO> organisatorischDTOS = dao.findAll(url);
    this.orgG = new Grid<>(OrganisatorischDTO.class, false);
    orgG.addColumn(OrganisatorischDTO::getRandbedingung).setHeader("Randbedingung");
    orgG.addColumn(OrganisatorischDTO::getHintergrund).setHeader("Hintergrund");
    orgG.setItems(organisatorischDTOS);

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    randbedingung = new TextField("Randbedingung");
    erlaueterung = new TextField("Hintergrund");
    Button addRandbedingung = new Button("Hinzufügen");
    addRandbedingung.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    Button edit = new Button("Ändern");
    Button delete = new Button("Löschen");
    edit.setVisible(false);
    delete.setVisible(false);

    HorizontalLayout buttonLayout = new HorizontalLayout(addRandbedingung, edit, delete);
    addRandbedingung.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    addRandbedingung.addClickShortcut(Key.ENTER);
    horizontalLayout.add(randbedingung, erlaueterung);

    addRandbedingung.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              if (!(randbedingung.getValue().isEmpty() || erlaueterung.getValue().isEmpty())) {
                OrganisatorischDTO organisatorischDTO =
                    new OrganisatorischDTO(randbedingung.getValue(), erlaueterung.getValue());
                dao.save(organisatorischDTO);
                clearTextFieldValue();
                updateList();
              } else {
                new NotificationWindow(
                    "Es müssen alle Eingabefelder ausgefüllt werden!",
                    NotificationType.SHORT,
                    NotificationType.NEUTRAL);
              }
            });

    HorizontalLayout gridLayout = new HorizontalLayout(orgG);

    orgG.addSelectionListener(
        (SelectionListener<Grid<OrganisatorischDTO>, OrganisatorischDTO>)
            selection -> {
              Optional<OrganisatorischDTO> optionalDoku = selection.getFirstSelectedItem();
              if (optionalDoku.isPresent()) {
                organisatorischDTO = optionalDoku.get();
                randbedingung.setValue(organisatorischDTO.getRandbedingung());
                erlaueterung.setValue(organisatorischDTO.getHintergrund());
                addRandbedingung.setVisible(false);
                edit.setVisible(true);
                delete.setVisible(true);

              } else {
                addRandbedingung.setVisible(true);
                edit.setVisible(false);
                delete.setVisible(false);
              }
            });

    edit.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              organisatorischDTO.setRandbedingung(randbedingung.getValue());
              organisatorischDTO.setHintergrund(erlaueterung.getValue());
              dao.update(organisatorischDTO);
              clearTextFieldValue();
              updateList();
              new NotificationWindow(
                  "Organisatorische Randbedingung erfolgreich geändert!",
                  NotificationType.SHORT,
                  NotificationType.SUCCESS);
            });

    delete.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              dao.delete(organisatorischDTO);
              clearTextFieldValue();
              updateList();
              new NotificationWindow(
                  "Organisatorische Randbedingung erfolgreich gelöscht!",
                  NotificationType.SHORT,
                  NotificationType.NEUTRAL);
            });

    VerticalLayout main = new VerticalLayout();
    main.setHorizontalComponentAlignment(Alignment.STRETCH, gridLayout);
    main.add(
        new BreadCrumbComponent(new String[] {"Randbedingungen", "Organisatorisch"}),
        gridLayout,
        horizontalLayout,
        buttonLayout);
    add(main);
  }

  public void updateList() {
    List<OrganisatorischDTO> organisatorischDTOS = dao.findAll(url);
    this.orgG.setItems(organisatorischDTOS);
  }

  private void clearTextFieldValue() {
    randbedingung.clear();
    erlaueterung.clear();
  }
}
