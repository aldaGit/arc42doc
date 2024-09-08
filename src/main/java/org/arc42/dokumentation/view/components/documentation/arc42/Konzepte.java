package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.arc42.dokumentation.model.dao.arc42documentation.KonzepteDAO;
import org.arc42.dokumentation.model.dto.documentation.KonzeptDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.BadgeGlossary;
import org.arc42.dokumentation.view.util.SelectBadgeComponent;
import org.arc42.dokumentation.view.util.data.NotificationType;

@Route(value = "konzepte", layout = MainLayout.class)
public class Konzepte extends DocumentationView {

  private Grid<KonzeptDTO> grid;
  private KonzeptDTO dto;
  private TextField name;
  private TextField text;
  private SelectBadgeComponent badgeComponent;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    event.getRouteParameters().get("arcId").get();
    super.beforeEnter(event);
  }

  @Override
  public void init() {
    super.init();
    getKonzepte()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getKonzepte().setOpened(true);

    KonzepteDAO dao = KonzepteDAO.getInstance();
    VerticalLayout verticalLayout = new VerticalLayout();

    grid = new Grid<>(KonzeptDTO.class, false);
    grid.addColumn(KonzeptDTO::getName).setHeader("Name");
    grid.addColumn(KonzeptDTO::getText).setHeader("Beschreibung");
    grid.addColumn(KonzeptDTO::getConceptCategories).setHeader("Kategorie");
    grid.setItems(dao.findAll(url));
    HorizontalLayout gridLayout = new HorizontalLayout(grid);

    name = new TextField("Name");
    text = new TextField("Beschreibung");
    badgeComponent = new SelectBadgeComponent(new ArrayList<>(), BadgeGlossary.CONCEPTBADGES);

    HorizontalLayout inputLayout = new HorizontalLayout(name, text);

    Button addKonzept = new Button("Hinzufügen");
    addKonzept.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    addKonzept.addClickShortcut(Key.ENTER);
    Button edit = new Button("Ändern");
    edit.setVisible(false);
    Button delete = new Button("Löschen");
    delete.setVisible(false);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    HorizontalLayout buttonLayout = new HorizontalLayout(addKonzept, edit, delete);

    addKonzept.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              if (!(name.getValue().isEmpty())) {
                if (badgeComponent.getSelectedBatches().size() == 0) {
                  badgeComponent.setSelectedBatches(
                      List.of(BadgeGlossary.CONCEPTBADGES.get(0)), BadgeGlossary.CONCEPTBADGES);
                }
                KonzeptDTO konzeptDTO =
                    new KonzeptDTO(
                        name.getValue(), text.getValue(), badgeComponent.getSelectedBatches());
                dao.save(konzeptDTO);
                clearTextFieldValues();
                grid.deselectAll();
                grid.setItems(dao.findAll(url));
              } else {
                new NotificationWindow(
                    "Das Feld Name muss ausgefüllt werden!",
                    NotificationType.SHORT,
                    NotificationType.NEUTRAL);
              }
            });

    grid.addSelectionListener(
        (SelectionListener<Grid<KonzeptDTO>, KonzeptDTO>)
            selection -> {
              Optional<KonzeptDTO> optionalDoku = selection.getFirstSelectedItem();
              if (optionalDoku.isPresent()) {
                dto = optionalDoku.get();
                name.setValue(dto.getName());
                text.setValue(dto.getText());
                badgeComponent.setSelectedBatches(
                    dto.getConceptCategories(), BadgeGlossary.CONCEPTBADGES);
                addKonzept.setVisible(false);
                edit.setVisible(true);
                delete.setVisible(true);
              } else {
                addKonzept.setVisible(true);
                edit.setVisible(false);
                delete.setVisible(false);
              }
            });

    edit.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              dto.setName(name.getValue());
              dto.setText(text.getValue());
              dto.setConceptCategories(badgeComponent.getSelectedBatches());
              dao.update(dto);
              clearTextFieldValues();
              grid.setItems(dao.findAll(url));
              new NotificationWindow(
                  "Konzept erfolgreich geändert!",
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
                  "Konzept erfolgreich gelöscht!",
                  NotificationType.SHORT,
                  NotificationType.NEUTRAL);
            });

    verticalLayout.setHorizontalComponentAlignment(Alignment.STRETCH, gridLayout);
    VerticalLayout left = new VerticalLayout(inputLayout, buttonLayout);
    VerticalLayout right = new VerticalLayout(badgeComponent);
    HorizontalLayout underGrid = new HorizontalLayout(left, right);
    verticalLayout.add(new BreadCrumbComponent(new String[] {"Konzepte"}), gridLayout, underGrid);
    add(verticalLayout);
  }

  private void clearTextFieldValues() {
    name.clear();
    text.clear();
    badgeComponent.setSelectedBatches(new ArrayList<>(), BadgeGlossary.CONCEPTBADGES);
  }
}