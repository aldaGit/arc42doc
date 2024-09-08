package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dao.arc42documentation.RisikoDAO;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO.Eintrittswahrscheinlichkeit;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.HelpField;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.components.documentation.riskComponents.FrageBogenGrid;
import org.arc42.dokumentation.view.components.documentation.riskComponents.staticComponents.BogenHelper;
import org.arc42.dokumentation.view.components.documentation.riskComponents.staticComponents.ViewUtils;
import org.arc42.dokumentation.view.util.data.NotificationType;

@Route(value = "risiken/bogen", layout = MainLayout.class)
public class Bogen extends DocumentationView {
  VerticalLayout pageVerticalLayout;
  HorizontalLayout horizontalLayout;
  VerticalLayout titleLayout;
  Grid<RisikoDTO> risikenGrid = new Grid<>(RisikoDTO.class, false);
  BogenHelper bogenHelper = new BogenHelper();

  Dialog dialog = new Dialog();
  MenuBar menuBar = new MenuBar();
  TextField risikoBeschreibung = new TextField("Risikobeschreibung");

  TextField erfasser = new TextField("Erfasser:in (Vorname, Nachname)");

  Button speichern;
  Button help;
  TextField geschaedigte = new TextField("Geschädigte:r");
  TextField orga = new TextField("Zuständige Organisationseinheit");
  TextField wirkung = new TextField("Maßnahmen");
  DatePicker eintritt = new DatePicker("Voraussichtliches Eintrittsdatum");
  TextArea notizField = new TextArea("Notiz");
  TextField ansprechparnter = new TextField("Ansprechpartner:in");
  DatePicker aktualisiert = new DatePicker("Zuletzt aktualisiert");
  private Select<Eintrittswahrscheinlichkeit> prio;
  String arcId;
  Select<String> risikoid = new Select<>();
  private List<String> risikoIdListe = new ArrayList<>();
  private List<String> beschreibungenListe = new ArrayList<>();
  FrageBogenGrid frageBogenGrid;
  TextField searchField;
  GridListDataView<RisikoDTO> allGridData = null;

  String resihihihaha = " noch nichts";

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    arcId = event.getRouteParameters().get("arcId").get();
    super.beforeEnter(event);
  }

  @Override
  public void init() {
    super.init();
    getBogen()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getRisiken().setOpened(true);
    if (arcId == null || arcId.isEmpty()) {
      arcId = "100000";
      url = "100000";
    }

    // make erfasser RequiredConfigurator indicate with an asterisk
    speichern = new Button("Speichern");
    erfasser.setRequiredIndicatorVisible(true);
    erfasser.setRequired(true);
    erfasser.addValueChangeListener(
        e -> {
          // any value removes asterisk, otherwise if empty have label with asterisk at end
          if (e.getValue().isEmpty() || e.getValue() == null || e.getValue().equals("")) {
            erfasser.setRequiredIndicatorVisible(true);

          } else {
            erfasser.setRequiredIndicatorVisible(false);
          }
        });

    this.frageBogenGrid = new FrageBogenGrid(arcId, risikoid);
    frageBogenGrid.setPadding(false);
    menuBar = ViewUtils.addMenuBar(arcId, 2);
    risikoBeschreibung.getStyle().set("padding-top", "50px");
    Button backToVerview = ViewUtils.backToOverview(arcId);
    speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    Button neuHinzufuegen = new Button("Neuen Bogen hinzufügen");
    erfasser.setRequired(true);
    risikoid.setRequiredIndicatorVisible(true);
    risikoBeschreibung.setRequired(true);

    pageVerticalLayout = new VerticalLayout();
    pageVerticalLayout.setWidthFull();
    horizontalLayout = new HorizontalLayout();
    HorizontalLayout buttonLayout = new HorizontalLayout();
    HorizontalLayout topMenu =
        new HorizontalLayout(new BreadCrumbComponent(new String[] {"Risiken", "Bogen"}), menuBar);
    topMenu.setWidthFull();
    dialog = new Dialog();

    help = ViewUtils.addHelper(dialog);
    VerticalLayout dialogHelp =
        HelpField.createDialogLayout(dialog, bogenHelper.getDescriptionText(), "zum Fragebogen");
    dialog.add(dialogHelp);
    HorizontalLayout gridLayout = new HorizontalLayout();
    gridLayout.setWidthFull();

    risikoid.setLabel("Risiko-ID");
    risikoid.setRequiredIndicatorVisible(true);
    List<String> dbResult = bogenHelper.getAllIdsFromDB(url);
    risikoIdListe.addAll(dbResult);
    refreshRisikoSelect();

    risikoBeschreibung.setLabel("Risikobeschreibung");
    risikoBeschreibung.setReadOnly(true);
    risikoBeschreibung.setWidth("450px");
    List<String> resultDB = bogenHelper.getAllBeschreibungen(url);
    beschreibungenListe.addAll(resultDB);

    risikoid.addValueChangeListener(
        event -> {
          if (event.getValue() == null) {
            clearAllFields();
            return;
          }
          if (RisikoDAO.getInstance().getAllIds(url).isEmpty()) {
            new NotificationWindow(
                "Noch keine IDs da. Erst Checkliste ausfüllen!",
                NotificationType.SHORT,
                NotificationType.NEUTRAL);
          }
          if (risikoid.getValue() != null && !risikoid.getValue().isEmpty()) {
            RisikoDTO existing = RisikoDAO.getInstance().findById(event.getValue());
            if (existing == null) {
              System.out.println("Risiko mit der ID " + event.getValue() + " nicht gefunden!");
              return;
            }
            populateFieldsFromDTO(existing);
          }
        });

    horizontalLayout.add(dialog, risikoid, risikoBeschreibung);
    pageVerticalLayout.add(topMenu, ViewUtils.addHeadline("Fragebogen", help), horizontalLayout);

    prio = new Select<>();
    prio.setLabel("Prioriätsskala");
    prio.setEmptySelectionAllowed(true);

    prio.setItems(Eintrittswahrscheinlichkeit.values());
    prio.setHelperText("");

    FormLayout formLayout =
        new FormLayout(
            erfasser,
            ansprechparnter,
            aktualisiert,
            prio,
            geschaedigte,
            orga,
            wirkung,
            eintritt,
            notizField);
    formLayout.setResponsiveSteps(new ResponsiveStep("0", 3));

    speichern.addClickListener(
        event -> {
          RisikoDTO dbRisiko = RisikoDAO.getInstance().findById(risikoid.getValue());
          if (dbRisiko != null && isValid()) {
            setAttributes(dbRisiko);
            RisikoDAO.getInstance().update(dbRisiko);
            this.frageBogenGrid.refreshItems();
            refreshRisikoSelect();
            NotificationWindow n =
                new NotificationWindow(
                    "Bogen erfolgreich gespeichert!",
                    NotificationType.SHORT,
                    NotificationType.SUCCESS);
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            clearAllFields();
          } else {
            new NotificationWindow(
                "Es müssen die Felder \"Risiko-ID\" und \"Erfasser:in\" ausgefüllt werden!",
                NotificationType.MEDIUM,
                NotificationType.ERROR);
          }
        });
    neuHinzufuegen.addClickListener(
        e -> {
          clearAllFields();
          // UI.getCurrent().navigate("/arc42/" + arcId + "/risiken/bogen");

        });
    List<RisikoDTO> risikenForGrid = RisikoDAO.getInstance().findAllByArcId(arcId);
    if (risikenForGrid == null) {
      risikenForGrid = new ArrayList<>();
    }
    allGridData = risikenGrid.setItems(risikenForGrid);
    searchField = BogenHelper.addSearchField(allGridData);
    searchField.getStyle().set("padding-top", "25px");
    allGridData.addFilter(
        risiko -> {
          String searchTerm = searchField.getValue().trim();
          if (searchTerm.isEmpty()) return true;
          return ViewUtils.matchesTerm(risiko.getId(), searchTerm);
        });
    searchField.addValueChangeListener(e -> risikenGrid.getDataProvider().refreshAll());
    buttonLayout.add(speichern, neuHinzufuegen, backToVerview);
    buttonLayout.setWidthFull();
    buttonLayout.setVerticalComponentAlignment(Alignment.CENTER, speichern);
    pageVerticalLayout.setHorizontalComponentAlignment(Alignment.END, backToVerview);
    pageVerticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, horizontalLayout);
    horizontalLayout.setVerticalComponentAlignment(Alignment.END, risikoid);
    gridLayout.add(this.frageBogenGrid);
    gridLayout.setPadding(false);
    TextArea hinweis = BogenHelper.addHinsweis();
    pageVerticalLayout.add(formLayout, hinweis, buttonLayout, searchField, gridLayout);
    this.add(pageVerticalLayout);
  }

  private void refreshRisikoSelect() {
    List<String> allIds = RisikoDAO.getInstance().getAllIds(url);
    // filter out all risks with valid erfasser
    allIds.removeIf(id -> isValidErfasser(RisikoDAO.getInstance().findById(id).getErfasser()));
    // für perfomance verlagern in dao
    risikoid.setItems(allIds);
  }

  public void clearAllFields() {
    risikoid.clear();
    risikoBeschreibung.clear();
    ansprechparnter.clear();
    aktualisiert.clear();
    prio.clear();
    geschaedigte.clear();
    orga.clear();
    wirkung.clear();
    eintritt.clear();
    notizField.clear();
    erfasser.clear();
    erfasser.setInvalid(false);
    risikoBeschreibung.setInvalid(false);
  }

  public void populateFieldsFromDTO(RisikoDTO existing) {
    System.out.println("populieren der Felder mit DTO: \n" + existing);
    risikoid.setValue(existing.getId());
    risikoBeschreibung.setValue(existing.getAnforderung());
    notizField.setValue(existing.getNotiz());
    ansprechparnter.setValue(existing.getAnsprechpartner());
    if (isValidErfasser(existing.getErfasser())) {
      prio.setValue(existing.getPrioSkala());
    } else {
      prio.clear();
    }
    prio.setValue(existing.getPrioSkala());
    notizField.setValue(existing.getNotiz());
    orga.setValue(existing.getOrgaEinheit());
    wirkung.setValue(existing.getWirkung());
    geschaedigte.setValue(existing.getGeschaedigte());
    erfasser.setValue(existing.getErfasser());

    try {
      eintritt.setValue(LocalDate.parse(existing.getEintrittsDatum()));
    } catch (Exception e) {
      eintritt.setValue(null);
      System.out.println(e.getMessage() + "Eintritt konntennicht geparst werden ");
    }
    try {
      aktualisiert.setValue(LocalDate.parse(existing.getZuletztAktu()));
    } catch (Exception e) {
      aktualisiert.setValue(null);
      System.out.println(e.getMessage() + "Aktualisiert konntennicht geparst werden ");
    }
  }

  public static boolean isValidErfasser(String erfasser) {
    return erfasser != null
        && !erfasser.equals("")
        && !erfasser.equals("null")
        && !erfasser.equals("\"\""); // ""
  }

  public boolean isValid() {
    if (erfasser == null || erfasser.getValue().isEmpty()) {
      return false;
    }
    if (risikoBeschreibung == null || risikoBeschreibung.getValue().isEmpty()) {
      return false;
    }
    if (arcId == null || arcId.isEmpty()) {
      return false;
    }
    return true;
  }

  public void setAttributes(RisikoDTO dbRisiko) {
    LocalDate l = eintritt.getValue();
    LocalDate aktu = aktualisiert.getValue();
    if (l == null) {

      dbRisiko.setEintrittsDatum("");

    } else {
      dbRisiko.setEintrittsDatum(l.toString());
    }
    if (aktu == null) {
      aktu = LocalDate.now();
    }
    dbRisiko.setErfasser(erfasser.getValue());
    dbRisiko.setAnsprechpartner(ansprechparnter.getValue());
    dbRisiko.setPrioSkala(prio.getValue());
    dbRisiko.setGeschaedigte(geschaedigte.getValue());
    dbRisiko.setZuletztAktu(aktu.toString());
    dbRisiko.setOrgaEinheit(orga.getValue());
    dbRisiko.setWirkung(wirkung.getValue());
    dbRisiko.setNotiz(notizField.getValue());
  }
}