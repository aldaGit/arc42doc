package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.arc42.dokumentation.model.dao.arc42documentation.RisikoDAO;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO.Eintrittswahrscheinlichkeit;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO.Schadenshoehe;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO.Status;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.HelpField;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.components.documentation.riskComponents.ChecklistenGrid;
import org.arc42.dokumentation.view.components.documentation.riskComponents.staticComponents.ChecklisteHelper;
import org.arc42.dokumentation.view.components.documentation.riskComponents.staticComponents.ViewUtils;
import org.arc42.dokumentation.view.util.data.NotificationType;

@Route(value = "risiken/checkliste", layout = MainLayout.class)
public class Checkliste extends DocumentationView {

  private TextField riskDescriptionField;

  private Select<Schadenshoehe> schadenshoeheSelect;
  private Select<Eintrittswahrscheinlichkeit> probabilitySelect;
  private Select<Status> statusSelect;
  private Dialog dialog;
  private Grid<RisikoDTO> risikenGrid;
  private RisikoDTO risikenDTO;
  GridListDataView<RisikoDTO> allGridData = null;
  private String arcId;

  VerticalLayout pageVerticalLayout;
  HorizontalLayout checklistTitleLayout;
  HorizontalLayout gridButtonsLayout;
  HorizontalLayout bottomLayout;
  HorizontalLayout helpText;

  private TextField searchField;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    arcId = event.getRouteParameters().get("arcId").get();
    super.beforeEnter(event);
  }

  @Override
  public void init() {
    super.init();
    getCheckliste()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getRisiken().setOpened(true);
    pageVerticalLayout = new VerticalLayout();
    checklistTitleLayout = new HorizontalLayout();
    gridButtonsLayout = new HorizontalLayout();
    helpText = new HorizontalLayout();

    Button speichern = new Button("Speichern");
    Button loeschen = new Button("Löschen");
    Button edit = new Button("Ändern");
    edit.setVisible(false);

    bottomLayout = new HorizontalLayout();
    bottomLayout.add(speichern, edit, loeschen, ViewUtils.backToOverview(arcId));
    bottomLayout.setWidthFull();
    bottomLayout.setVerticalComponentAlignment(Alignment.END, ViewUtils.backToOverview(arcId));

    riskDescriptionField = new TextField("Risikobeschreibung");
    riskDescriptionField.setHelperText("ca. 1-3 Wörter");
    riskDescriptionField.addFocusShortcut(Key.ENTER);
    riskDescriptionField.focus();
    riskDescriptionField.setRequired(true);

    schadenshoeheSelect = new Select<>();
    schadenshoeheSelect.setEmptySelectionAllowed(false);
    schadenshoeheSelect.setLabel("Schadenshöhe");
    schadenshoeheSelect.setItems(Schadenshoehe.values());
    schadenshoeheSelect.setRequiredIndicatorVisible(true);

    probabilitySelect = new Select<>();
    probabilitySelect.setEmptySelectionCaption("Nicht erfasst");
    probabilitySelect.setLabel("Eintrittswahrscheinlichkeit");
    probabilitySelect.setItems(Eintrittswahrscheinlichkeit.values());
    probabilitySelect.setRequiredIndicatorVisible(true);
    probabilitySelect.setWidth("210px");

    statusSelect = new Select<>();
    statusSelect.setEmptySelectionAllowed(false);
    statusSelect.setValue(Status.NICHT_ERFASST);
    statusSelect.setLabel("Status");
    statusSelect.setRequiredIndicatorVisible(true);
    statusSelect.setItems(Status.values());

    List<RisikoDTO> risiken = RisikoDAO.getInstance().findAllByArcId(arcId);
    if (risiken == null) {
      risiken = new ArrayList<>();
    }
    ChecklistenGrid checklistenGrid = new ChecklistenGrid();
    risikenGrid = checklistenGrid.generateGrid();
    allGridData = risikenGrid.setItems(risiken);
    checklistenGrid.setDrop(risikenGrid, allGridData);

    dialog = new Dialog();
    VerticalLayout dialogHelp =
        HelpField.createDialogLayout(
            dialog,
            "Tragen Sie hier Ihre Risiken in die Checkliste ein. Hierbei müssen Sie verpflichtend"
                + " alle vier Felder, die unten angeboten werden, ausfüllen. Die Checkliste dient"
                + " zur Übersicht aller eingetragenen Risiken. Die generierte"
                + " Risiko-ID identifiziert jedes Risiko. Viel Erfolg!",
            "zur Checkliste");
    Button help = ViewUtils.addHelper(dialog);
    dialog.add(dialogHelp);
    checklistTitleLayout.add(dialog);

    searchField = ChecklisteHelper.addSearchField(allGridData);

    allGridData.addFilter(
        risiko -> {
          String searchTerm = searchField.getValue().trim();
          if (searchTerm.isEmpty()) return true;
          return ViewUtils.matchesTerm(risiko.getId(), searchTerm);
        });
    speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    loeschen.setVisible(false);
    loeschen.addThemeVariants(ButtonVariant.LUMO_ERROR);

    VerticalLayout gridLayout = new VerticalLayout(searchField, risikenGrid);
    gridLayout.setPadding(false);
    gridLayout.setWidthFull();

    speichern.addClickShortcut(Key.ENTER);
    speichern.addClickListener(
        event -> {
          if (isValid()) {
            RisikoDTO dbRisiko = populateDTOFromFields();
            RisikoDAO.getInstance().save(dbRisiko);
            clearTextFieldValue();
            risikenGrid.deselectAll();
            updateList();
            new NotificationWindow(
                "Erfolgreich gespeichert!", NotificationType.SHORT, NotificationType.SUCCESS);
          } else {
            new NotificationWindow(
                "Es müssen alle Felder ausgefüllt werden!",
                NotificationType.SHORT,
                NotificationType.ERROR);
          }
        });

    risikenGrid.addSelectionListener(
        (SelectionListener<Grid<RisikoDTO>, RisikoDTO>)
            selection -> {
              Optional<RisikoDTO> optionalDoku = selection.getFirstSelectedItem();
              if (optionalDoku.isPresent()) {
                risikenDTO = optionalDoku.get();
                populateFieldsFromDTO(risikenDTO);
                speichern.setVisible(false);
                loeschen.setVisible(true);
                edit.setVisible(true);
              } else {
                speichern.setVisible(true);
                edit.setVisible(false);
                loeschen.setVisible(false);
              }
            });

    edit.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              readFieldsIntoDTO(risikenDTO);
              RisikoDAO.getInstance().update(risikenDTO);
              clearTextFieldValue();
              updateList();
              new NotificationWindow(
                  "Eintrag erfolgreich geändert!",
                  NotificationType.SHORT,
                  NotificationType.SUCCESS);
            });
    loeschen.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              System.out.println("DELETING: " + risikenDTO);
              RisikoDAO.getInstance().delete(risikenDTO);
              clearTextFieldValue();
              updateList();
              new NotificationWindow(
                  "Risiko erfolgreich gelöscht!", NotificationType.SHORT, NotificationType.SUCCESS);
            });

    MenuBar menuBar = ViewUtils.addMenuBar(arcId, 1);
    HorizontalLayout topMenu =
        new HorizontalLayout(new BreadCrumbComponent(new String[] {"Risiken", "Bogen"}), menuBar);
    topMenu.setWidthFull();

    pageVerticalLayout.add(topMenu);
    pageVerticalLayout.setWidthFull();
    pageVerticalLayout.setHorizontalComponentAlignment(Alignment.STRETCH, gridLayout);
    pageVerticalLayout.add(
        ViewUtils.addHeadline("Checkliste", help), checklistTitleLayout, gridLayout);

    gridButtonsLayout.setWidthFull();
    gridButtonsLayout.setHeight("100px");
    gridButtonsLayout.setVerticalComponentAlignment(
        Alignment.START,
        riskDescriptionField,
        schadenshoeheSelect,
        probabilitySelect,
        statusSelect);
    gridButtonsLayout.add(
        riskDescriptionField, schadenshoeheSelect, probabilitySelect, statusSelect);

    helpText.setWidthFull();
    helpText.add(ChecklisteHelper.addTextArea());
    pageVerticalLayout.add(helpText, gridButtonsLayout, bottomLayout);
    beforeLeave(null);
    this.add(pageVerticalLayout);
  }

  private RisikoDTO populateDTOFromFields() {
    return new RisikoDTO(
        riskDescriptionField.getValue(),
        schadenshoeheSelect.getValue(),
        probabilitySelect.getValue(),
        statusSelect.getValue(),
        "",
        "",
        "",
        "",
        Eintrittswahrscheinlichkeit.NICHTERFASST,
        "",
        "",
        "",
        "");
  }

  public void updateList() {
    List<RisikoDTO> stakeholders = RisikoDAO.getInstance().findAll(url);
    this.risikenGrid.setItems(stakeholders);
    this.allGridData = risikenGrid.setItems(stakeholders);

    allGridData.addFilter(
        risiko -> {
          String searchTerm = searchField.getValue().trim();
          if (searchTerm.isEmpty()) return true;
          return ViewUtils.matchesTerm(risiko.getId(), searchTerm);
        });
  }

  private void clearTextFieldValue() {
    riskDescriptionField.clear();
    schadenshoeheSelect.clear();
    probabilitySelect.clear();
    statusSelect.clear();
  }

  public boolean isValid() {
    return !isInvalid();
  }

  private boolean isInvalid() {
    return (schadenshoeheSelect.getValue() == null
        || probabilitySelect.getValue() == null
        || statusSelect.getValue() == null
        || riskDescriptionField.getValue().isEmpty()
        || schadenshoeheSelect.getValue() == Schadenshoehe.NICHTERFASST
        || probabilitySelect.getValue() == Eintrittswahrscheinlichkeit.NICHTERFASST
        || statusSelect.getValue() == Status.NICHT_ERFASST);
  }

  public void populateFieldsFromDTO(RisikoDTO r) {
    riskDescriptionField.setValue(r.getAnforderung());
    schadenshoeheSelect.setValue(r.getSchadenshoehe());
    statusSelect.setValue(r.getStatus());
    probabilitySelect.setValue(r.getEintrittswahrscheinlichkeit());
  }

  public void readFieldsIntoDTO(RisikoDTO r) {
    r.setStatus(statusSelect.getValue());
    r.setAnforderung(riskDescriptionField.getValue());
    r.setSchadenshoehe(schadenshoeheSelect.getValue());
    r.setEintrittswahrscheinlichkeit(probabilitySelect.getValue());
  }

  public void setUrlAndArcID(String string) {
    this.url = string;
    this.arcId = string;
  }
}