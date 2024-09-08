package org.arc42.dokumentation.view.components.documentation;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import java.util.List;
import java.util.Optional;
import org.arc42.dokumentation.model.dao.arc42documentation.LoesungsStrategieDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.NachhaltigkeitszieleDAO;
import org.arc42.dokumentation.model.dto.documentation.LoesungsStrategieDTO;
import org.arc42.dokumentation.model.dto.documentation.NachhaltigkeitszieleDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.data.NotificationType;

@Route(value = "nachhaltigkeitsziele", layout = MainLayout.class)
public class ARC42Nachhaltigkeitsziele extends DocumentationView {

  TextField greenGoal;
  TextField motivation;

  Select<String> prio;

  Select<String> saving;
  private NachhaltigkeitszieleDTO nachhaltigkeitszieleDTO;
  private NachhaltigkeitszieleDAO dao;
  Grid<NachhaltigkeitszieleDTO> grid;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (event.getRouteParameters().get("arcId").isPresent()) {
      event.getRouteParameters().get("arcId").get();
    }
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
    getNachhaltigkeitsziele()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    this.dao = NachhaltigkeitszieleDAO.getInstance();
    LoesungsStrategieDAO loesungsStrategieDAO = LoesungsStrategieDAO.getInstance();
    List<NachhaltigkeitszieleDTO> nachhaltigkeitszieleDTOS = dao.findAll(url);
    grid = new Grid<>(NachhaltigkeitszieleDTO.class, false);
    grid.setAllRowsVisible(true);

    grid.addColumn(NachhaltigkeitszieleDTO::getId).setHeader("ID");
    grid.addColumn(NachhaltigkeitszieleDTO::getGoal).setHeader("Nachhaltigkeitsziele");
    grid.addColumn(NachhaltigkeitszieleDTO::getMotivation).setHeader("Motivation");
    grid.addColumn(NachhaltigkeitszieleDTO::getPrio).setHeader("Prioriät");
    grid.addColumn(NachhaltigkeitszieleDTO::getSaving).setHeader("Einsparung");
    grid.setItems(nachhaltigkeitszieleDTOS);

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    greenGoal = new TextField("Nachhaltigkeitsziel");
    greenGoal.setRequired(true);
    motivation = new TextField("Motivation");
    motivation.setRequired(true);
    prio = new Select<>();
    prio.setItems("sehr niedrig", "niedrig", "normal", "hoch", "sehr hoch");
    prio.setLabel("Priorität");
    prio.setEmptySelectionAllowed(true);
    saving = new Select<>();
    saving.setItems("0%", "10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "100%");
    saving.setLabel("Einsparung");
    saving.setEmptySelectionAllowed(true);

    Button codeZiel = new Button("Energy Smell freier Code als Ziel hinzufügen");
    codeZiel.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
    codeZiel.addClickListener(
        e -> {
          NachhaltigkeitszieleDTO cleanCodeZiel =
              new NachhaltigkeitszieleDTO(
                  "Keine Energy Smells",
                  "Ohne Energy Smells ist Code energieeffizienter.",
                  null,
                  null);
          cleanCodeZiel = dao.save(cleanCodeZiel);
          loesungsStrategieDAO.save(
              new LoesungsStrategieDTO(
                  "Mithilfe der Energy Smell Analyse sollen Smells gefunden und behoben werden."),
              cleanCodeZiel);
          updateList();
          codeZiel.setEnabled(false);
        });
    if (nachhaltigkeitszieleDTOS.stream()
        .anyMatch(ziel -> "Keine Energy Smells".equals(ziel.getGoal()))) {
      codeZiel.setEnabled(false);
    }
    Icon tooltip = new Icon(VaadinIcon.INFO_CIRCLE);
    tooltip.setColor("white");
    Button button = new Button(tooltip);
    button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_ICON);
    codeZiel.setSuffixComponent(button);
    codeZiel.setTooltipText(
        "Dieses Ziel wird in der Analyse auf die Anzahl der im Code gefundenen Energy Smells"
            + " analysiert und evaluiert.");

    Button addNachhaltigkeitsziele = new Button("Hinzufügen");
    addNachhaltigkeitsziele.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    addNachhaltigkeitsziele.addClickShortcut(Key.ENTER);
    horizontalLayout.add(greenGoal, motivation, prio, saving);

    Button edit = new Button("Ändern");
    Button delete = new Button("Löschen");
    edit.setVisible(false);
    delete.setVisible(false);
    addNachhaltigkeitsziele.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    addNachhaltigkeitsziele.addClickShortcut(Key.ENTER);

    HorizontalLayout buttonLayout =
        new HorizontalLayout(addNachhaltigkeitsziele, edit, delete, codeZiel);
    HorizontalLayout gridLayout = new HorizontalLayout(grid);

    VerticalLayout main = new VerticalLayout();
    main.setHorizontalComponentAlignment(Alignment.STRETCH, gridLayout);
    main.add(
        new BreadCrumbComponent(new String[] {"Einführung und Ziele", "Nachhaltigkeitsziele"}),
        gridLayout,
        horizontalLayout,
        buttonLayout);
    add(main);

    addNachhaltigkeitsziele.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              if (!(greenGoal.getValue().isEmpty() && motivation.getValue().isEmpty())) {
                NachhaltigkeitszieleDTO nachhaltigkeitszieleDTO =
                    new NachhaltigkeitszieleDTO(
                        greenGoal.getValue(),
                        motivation.getValue(),
                        prio.getValue(),
                        saving.getValue());
                nachhaltigkeitszieleDTO = dao.save(nachhaltigkeitszieleDTO);
                loesungsStrategieDAO.save(new LoesungsStrategieDTO(""), nachhaltigkeitszieleDTO);
                clearTextFieldValue();
                grid.deselectAll();
                updateList();
              } else {
                new NotificationWindow(
                    "Es müssen alle Pflichtfelder ausgefüllt werden!",
                    NotificationType.SHORT,
                    NotificationType.NEUTRAL);
              }
            });

    grid.addSelectionListener(
        (SelectionListener<Grid<NachhaltigkeitszieleDTO>, NachhaltigkeitszieleDTO>)
            selection -> {
              Optional<NachhaltigkeitszieleDTO> optionalDoku = selection.getFirstSelectedItem();
              if (optionalDoku.isPresent()) {
                nachhaltigkeitszieleDTO = optionalDoku.get();
                greenGoal.setValue(nachhaltigkeitszieleDTO.getGoal());
                motivation.setValue(nachhaltigkeitszieleDTO.getMotivation());
                prio.setValue(nachhaltigkeitszieleDTO.getPrio());
                saving.setValue(nachhaltigkeitszieleDTO.getSaving());
                addNachhaltigkeitsziele.setVisible(false);
                edit.setVisible(true);
                if (greenGoal.getValue().equals("Keine Energy Smells")) {
                  greenGoal.setReadOnly(true);
                  motivation.setReadOnly(true);
                } else {
                  greenGoal.setReadOnly(false);
                  motivation.setReadOnly(false);
                }
                delete.setVisible(true);
              } else {
                addNachhaltigkeitsziele.setVisible(true);
                edit.setVisible(false);
                delete.setVisible(false);
                clearTextFieldValue();
                greenGoal.setReadOnly(false);
                motivation.setReadOnly(false);
              }
            });

    edit.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              nachhaltigkeitszieleDTO.setGoal(greenGoal.getValue());
              nachhaltigkeitszieleDTO.setMotivation(motivation.getValue());
              nachhaltigkeitszieleDTO.setPrio(prio.getValue());
              nachhaltigkeitszieleDTO.setSaving(saving.getValue());
              dao.update(nachhaltigkeitszieleDTO);
              clearTextFieldValue();
              updateList();
              new NotificationWindow(
                  "Nachhaltigkeitsziel erfolgreich geändert!",
                  NotificationType.SHORT,
                  NotificationType.SUCCESS);
            });

    delete.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              dao.delete(nachhaltigkeitszieleDTO);
              loesungsStrategieDAO.deleteByNachhaltigkeitsziel(nachhaltigkeitszieleDTO);
              clearTextFieldValue();
              updateList();
              if (this.dao.findAll(url).stream()
                  .noneMatch(ziel -> "Keine Energy Smells".equals(ziel.getGoal()))) {
                codeZiel.setEnabled(true);
              }
              new NotificationWindow(
                  "Nachhaltigkeitsziel erfolgreich gelöscht!",
                  NotificationType.SHORT,
                  NotificationType.NEUTRAL);
            });
  }

  public void updateList() {
    List<NachhaltigkeitszieleDTO> nachhaltigkeitsziele = this.dao.findAll(url);
    this.grid.setItems(nachhaltigkeitsziele);
  }

  private void clearTextFieldValue() {
    greenGoal.clear();
    motivation.clear();
    prio.clear();
    saving.clear();
  }
}
