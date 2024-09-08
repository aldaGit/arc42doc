package org.arc42.dokumentation.view.components.documentation.riskComponents;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.arc42.dokumentation.model.dao.arc42documentation.RisikoDAO;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO.Eintrittswahrscheinlichkeit;

public class FrageBogenGrid extends VerticalLayout {
  Grid<RisikoDTO> risikenGrid = new Grid<>(RisikoDTO.class, false);
  Select<String> risikoidSelectBox;
  private String arcId;

  public FrageBogenGrid(String arcId, Select<String> risikoidSelectBox) {
    this.risikoidSelectBox = risikoidSelectBox;
    this.arcId = arcId;
    HorizontalLayout risikenGridLayout = new HorizontalLayout();
    risikenGridLayout.setWidthFull();

    List<RisikoDTO> risiken = RisikoDAO.getInstance().findAllByArcId(this.arcId);
    if (risiken == null) {
      risiken = new ArrayList<>();
    }
    List<RisikoDTO> risikenMitErfasser = new ArrayList<>();
    for (RisikoDTO r : risiken) {
      if (isValidErfasser(r.getErfasser())) {
        System.out.println(r.getErfasser() + " " + r.getId());
        risikenMitErfasser.add(r);
      }
    }
    risikenGrid.setAllRowsVisible(true);
    risikenGrid.addColumn(RisikoDTO::getId).setHeader("ID").setFrozen(true);
    risikenGrid.addColumn(RisikoDTO::getAnforderung).setHeader("Beschreibung").setFrozen(true);
    risikenGrid.addColumn(RisikoDTO::getErfasser).setHeader("Erfasser:in").setFrozen(true);
    risikenGrid.setItems(risikenMitErfasser);
    risikenGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    risikenGrid.getEditor().setBuffered(true);
    risikenGrid.setWidthFull();
    risikenGridLayout.add(risikenGrid);
    risikenGridLayout.setPadding(true);
    risikenGrid
        .addComponentColumn(
            risiko -> {
              Button delete = new Button("Löschen");
              delete.getStyle().set("color", "#2F5FF5");
              delete.getStyle().set("border", "1px solid #2F5FF5");
              delete.addClickListener(
                  e -> {
                    Dialog dialog = new Dialog();
                    VerticalLayout dialogFenster =
                        createDialogLayout(
                            dialog,
                            "Möchten Sie diesen Fragebogen des Risikos mit der ID "
                                + risiko.getId()
                                + " endgültig löschen?",
                            risiko);
                    dialog.add(dialogFenster);
                    dialog.open();
                  });
              return delete;
            })
        .setWidth("150px")
        .setFlexGrow(0);

    risikenGrid.addSelectionListener(
        e -> {
          Optional<RisikoDTO> u = e.getFirstSelectedItem();
          if (u.isPresent()) {
            risikoidSelectBox.setValue(u.get().getId());
          }
        });

    this.add(risikenGridLayout);
  }

  private boolean isValidErfasser(String erfasser) {
    return erfasser != null
        && !erfasser.equals("")
        && !erfasser.equals("null")
        && !erfasser.equals("\"\""); // ""
  }

  public void refreshItems() {
    List<RisikoDTO> risikoDTOs = RisikoDAO.getInstance().findAllByArcId(arcId);
    if (risikoDTOs == null) {
      risikoDTOs = new ArrayList<>();
    }
    ArrayList<RisikoDTO> risikenMitErfasser = new ArrayList<>();
    for (RisikoDTO r : risikoDTOs) {
      if (isValidErfasser(r.getErfasser())) {
        System.out.println(r.getErfasser() + " " + r.getId());
        risikenMitErfasser.add(r);
      }
    }
    // filter out only with valid erfasser
    this.risikenGrid.setItems(risikenMitErfasser);
    this.risikenGrid.getDataProvider().refreshAll();
  }

  public VerticalLayout createDialogLayout(Dialog d, String text, RisikoDTO risiko) {
    Span span = new Span(text);
    Button ja = new Button("Ja");
    ja.addClickListener(
        e -> {
          removeBogenFromDTO(risiko);
          RisikoDAO.getInstance().update(risiko);
          refreshItems();
          this.risikoidSelectBox.setValue(null);
          d.close();
        });
    Button abbrechen = new Button("Abbrechen", e -> d.close());
    abbrechen.addThemeVariants(ButtonVariant.LUMO_ERROR);

    HorizontalLayout buttonLayout = new HorizontalLayout(ja, abbrechen);
    buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

    VerticalLayout dialogLayout = new VerticalLayout(span, buttonLayout);

    dialogLayout.setPadding(false);
    dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
    dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");

    return dialogLayout;
  }

  private void removeBogenFromDTO(RisikoDTO risikoDTO) {
    System.out.println("removing bogen from " + risikoDTO);
    risikoDTO.setErfasser("");
    risikoDTO.setAnsprechpartner("");
    risikoDTO.setPrioSkala(Eintrittswahrscheinlichkeit.KEINE);
    risikoDTO.setGeschaedigte("");
    risikoDTO.setZuletztAktu("");
    risikoDTO.setOrgaEinheit("");
    risikoDTO.setWirkung("");
    risikoDTO.setNotiz("");
    risikoDTO.setEintrittsDatum("");
  }
}