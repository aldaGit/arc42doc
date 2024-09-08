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
import org.arc42.dokumentation.model.dao.arc42documentation.RequirementDAO;
import org.arc42.dokumentation.model.dto.documentation.RequirementDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.data.NotificationType;

@Route(value = "aufgabenstellung", layout = MainLayout.class)
public class Aufgabenstellung extends DocumentationView {

  private final Grid<RequirementDTO> grid = new Grid<>(RequirementDTO.class, false);
  private RequirementDAO dao;
  private RequirementDTO requirementDTO;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    event.getRouteParameters().get("arcId").get();
    super.beforeEnter(event);
  }

  @Override
  public void init() {
    super.init();
    getEinfZiele().setOpened(true);
    getEinfZiele()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getAufgabenstellung()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    this.dao = RequirementDAO.getInstance();
    List<RequirementDTO> requirementDTOS = dao.findAll(url);
    grid.addColumn(RequirementDTO::getAufgabe).setHeader("Aufgaben");
    grid.setItems(requirementDTOS);

    TextField goal = new TextField("Ziel");
    goal.focus();
    Button addGoal = new Button("Hinzufügen");
    Button edit = new Button("Ändern");
    Button delete = new Button("Löschen");
    addGoal.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    edit.setVisible(false);
    delete.setVisible(false);

    HorizontalLayout buttonLayout = new HorizontalLayout(addGoal, edit, delete);

    addGoal.addClickShortcut(Key.ENTER);
    addGoal.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              if (!goal.getValue().isEmpty()) {
                requirementDTO = new RequirementDTO(goal.getValue());
                dao.save(requirementDTO);
                goal.clear();
                updateList();
              } else {
                new NotificationWindow(
                    "Sie haben kein Ziel definiert!",
                    NotificationType.SHORT,
                    NotificationType.ERROR);
              }
            });

    HorizontalLayout gridLayout = new HorizontalLayout(grid);
    this.setAlignItems(Alignment.AUTO);
    grid.addSelectionListener(
        (SelectionListener<Grid<RequirementDTO>, RequirementDTO>)
            selection -> {
              Optional<RequirementDTO> optionalDoku = selection.getFirstSelectedItem();
              if (optionalDoku.isPresent()) {
                requirementDTO = optionalDoku.get();
                goal.setValue(requirementDTO.getAufgabe());
                addGoal.setVisible(false);
                edit.setVisible(true);
                delete.setVisible(true);

              } else {
                addGoal.setVisible(true);
                edit.setVisible(false);
                delete.setVisible(false);
              }
            });

    edit.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              requirementDTO.setAufgabe(goal.getValue());
              dao.update(requirementDTO);
              goal.clear();
              updateList();
              new NotificationWindow(
                  "Aufgabe erfolgreich geändert!",
                  NotificationType.SHORT,
                  NotificationType.SUCCESS);
            });

    delete.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              dao.delete(requirementDTO);
              goal.clear();
              updateList();
              new NotificationWindow(
                  "Aufgabe erfolgreich gelöscht!",
                  NotificationType.SHORT,
                  NotificationType.NEUTRAL);
            });

    VerticalLayout main = new VerticalLayout();
    main.setHorizontalComponentAlignment(Alignment.STRETCH, gridLayout);
    main.add(
        new BreadCrumbComponent(new String[] {"Einführung und Ziele", "Aufgabenstellung"}),
        gridLayout,
        goal,
        buttonLayout);
    add(main);
  }

  public void updateList() {
    List<RequirementDTO> requirementDTOS = dao.findAll(url);
    this.grid.setItems(requirementDTOS);
  }
}
