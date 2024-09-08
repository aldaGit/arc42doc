package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.arc42.dokumentation.model.dao.arc42documentation.TextEingabeDAO;
import org.arc42.dokumentation.model.dto.documentation.TextEingabeDTO;
import org.arc42.dokumentation.model.dto.documentation.TextEingabeDTO.TEXTTYPE;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.util.data.NotificationType;

public class TextEingabeManager extends VerticalLayout {

  Button plusButton = new Button(new Icon(VaadinIcon.PLUS_CIRCLE));
  TextField contentField;

  Grid<TextEingabeDTO> textEingabeGrid = new Grid<>();

  private TextEingabeDTO editingEingabe = null;
  private Button save = new Button("Ändern");
  private Button abort = new Button("Abbrechen");

  private String url;
  private String arcId;
  private TEXTTYPE swot;
  private FormLayout formLayoutW;
  private String headerText;

  public TextEingabeManager(String url, String arcId, TEXTTYPE swot) {
    formLayoutW = new FormLayout();
    abort.getStyle().set("width", "80px");
    abort.getStyle().set("margin-left", "5px");

    abort.getElement().getStyle().set("padding-left", "5px");
    abort.getElement().getStyle().set("margin-right", "5px");
    save.getStyle().set("margin-left", "5px");
    save.getStyle().set("margin-right", "5px");

    save.getStyle().set("width", "80px");
    this.url = url;
    this.arcId = arcId;
    this.swot = swot;
    switch (swot) {
      case STRENGTH:
        this.contentField = new TextField("Strength");
        this.headerText = "Strengths";
        break;
      case WEAKNESS:
        this.contentField = new TextField("Weakness");
        this.headerText = "Weaknesses";
        break;
      case OPPORTUNITY:
        this.contentField = new TextField("Opportunity");
        this.headerText = "Opportunities";
        break;
      case THREAT:
        this.contentField = new TextField("Threat");
        this.headerText = "Threats";
        break;
      default:
        this.contentField = new TextField("content");
        this.headerText = "contents";
        break;
    }
    init();
  }

  private void enableCreateMode(ClickEvent<Button> buttonClickEvent) {
    save.setVisible(false);
    abort.setVisible(false);
    plusButton.setVisible(true);
    textEingabeGrid.select(null);
    formLayoutW.setColspan(contentField, 3);
    contentField.setValue("");
    contentField.setReadOnly(false);
    contentField.focus();
  }

  private void saveTextEingabe(ClickEvent<Button> buttonClickEvent) {
    if (editingEingabe != null) {

      editingEingabe.setContent(contentField.getValue());
      TextEingabeDAO.getInstance().update(editingEingabe);
      textEingabeGrid.getDataProvider().refreshAll();
      editingEingabe = null;
    }
    this.enableCreateMode(buttonClickEvent);
  }

  private void editTextEingabe(TextEingabeDTO textEingabeDTO) {
    editingEingabe = textEingabeDTO;
    contentField.setValue(textEingabeDTO.getContent());
    plusButton.setVisible(false);
    formLayoutW.setColspan(contentField, 4);

    save.setVisible(true);
    abort.setVisible(true);
  }

  public void init() {

    abort.addClickListener(this::enableCreateMode);
    abort.addThemeVariants(ButtonVariant.LUMO_ERROR);
    save.addClickListener(this::saveTextEingabe);
    save.getStyle().set("color", "#2F5FF5");
    abort.setVisible(false);
    save.setVisible(false);

    textEingabeGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

    textEingabeGrid.setMinWidth("180px");
    textEingabeGrid.setMaxHeight("1500px");
    textEingabeGrid.setMinHeight("400px");
    textEingabeGrid.setItems(new ArrayList<>());
    textEingabeGrid.addColumn(TextEingabeDTO::getContent).setHeader(headerText);

    HorizontalLayout swotMegaGrid = new HorizontalLayout(textEingabeGrid);
    swotMegaGrid.setWidthFull();

    Binder<TextEingabeDTO> binder = new Binder<>(TextEingabeDTO.class);
    textEingabeGrid.getEditor().setBinder(binder);

    textEingabeGrid
        .addComponentColumn(
            person -> {
              Button deleteButtonW = new Button("Löschen");
              deleteButtonW.getStyle().set("color", "#6495ED");

              deleteButtonW.getStyle().set("border", "1px solid #6495ED");
              deleteButtonW.getStyle().set("font-size", "var(--lumo-font-size-s)");
              deleteButtonW.addClickListener(
                  e -> {
                    TextEingabeDAO.getInstance().delete(person);
                    refreshItems(textEingabeGrid, this.swot);
                    contentField.setValue("");
                  });
              return deleteButtonW;
            })
        .setWidth("150px")
        .setFlexGrow(0);
    textEingabeGrid.addSelectionListener(
        e -> {
          Optional<TextEingabeDTO> u = e.getFirstSelectedItem();
          if (u.isPresent()) {
            editTextEingabe(u.get());
          }
        });

    formLayoutW.setResponsiveSteps(
        new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP),
        new FormLayout.ResponsiveStep("4em", 4, FormLayout.ResponsiveStep.LabelsPosition.TOP));

    contentField.setClearButtonVisible(true);

    formLayoutW.add(contentField, 3);
    formLayoutW.add(plusButton, 1);
    // make save small
    save.addThemeVariants(ButtonVariant.LUMO_SMALL);
    abort.addThemeVariants(ButtonVariant.LUMO_SMALL);
    formLayoutW.add(save, 2);
    formLayoutW.add(abort, 2);

    // Lösung finden für unendliche weitergabe!

    plusButton.addClickListener(
        e -> {
          if (contentField.getValue() != null && !contentField.getValue().isBlank()) {
            TextEingabeDTO eingabeDTO = new TextEingabeDTO(contentField.getValue(), this.swot);
            TextEingabeDAO.getInstance().save(eingabeDTO, arcId);
            updateList();
            TextField tx = new TextField();
            tx.setClearButtonVisible(true);
            contentField.setValue("");
          } else {
            new NotificationWindow(
                "Bitte geben Sie zuerst etwas ein.",
                NotificationType.SHORT,
                NotificationType.ERROR);
          }
        });

    this.add(swotMegaGrid, formLayoutW);

    updateList();
  }

  public void updateList() {
    List<TextEingabeDTO> allSwot = TextEingabeDAO.getInstance().findAll(url);
    if (allSwot == null) {
      return;
    }

    List<TextEingabeDTO> swotRelevant = new ArrayList<>();

    for (TextEingabeDTO tdto : allSwot) {
      if (tdto.getType() == this.swot) {
        swotRelevant.add(tdto);
      }
    }
    this.textEingabeGrid.setItems(swotRelevant);
  }

  public void refreshItems(Grid<TextEingabeDTO> grid, TEXTTYPE wantedType) {
    List<TextEingabeDTO> allByArc = TextEingabeDAO.getInstance().findAllByArcId(arcId);
    if (allByArc == null) {
      return;
    }
    ArrayList<TextEingabeDTO> wantedTypeByArc = new ArrayList<>();
    for (TextEingabeDTO r : allByArc) {
      if (r.getType().equals(wantedType)) {
        wantedTypeByArc.add(r);
      }
    }
    // filter out only with valid erfasser
    grid.setItems(wantedTypeByArc);
    grid.getDataProvider().refreshAll();
  }
}