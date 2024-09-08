package org.arc42.dokumentation.view.components.documentation.main;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.arc42.analyse.control.RegelwerkControl;
import org.arc42.analyse.model.evaluation.EvaluationResultI;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dao.arc42documentation.Arc42DokuNameDAO;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.DialogComponent;
import org.neo4j.driver.exceptions.DatabaseException;

@Route(value = "arc42View", layout = MainLayout.class)
public class Arc42DocumentationView extends ReadDocumentionAbstract {

  private final List<DokuNameDTO> dokuNameDTOList = new ArrayList<>();
  private final Arc42DokuNameDAO dao;
  ARC42DAOAbstract dao1;
  private String arcId;
  private DokuNameDTO dokuNameDTO;

  public Arc42DocumentationView() {
    // Load Documentations
    dao = new Arc42DokuNameDAO();
    setSizeFull();
    fillPossibleArc42Doku();

    // Grid creation
    Grid<DokuNameDTO> gridDocumentation = new Grid<>(DokuNameDTO.class, false);
    gridDocumentation.getStyle().setBackground("#f1f1f1");
    gridDocumentation.addColumn(DokuNameDTO::getId).setHeader("ID").setSortable(true);
    gridDocumentation.addColumn(DokuNameDTO::getName).setHeader("Title").setSortable(true);
    gridDocumentation
        .addColumn(
            dokuNameDTO -> {
              RegelwerkControl regelwerkControl = new RegelwerkControl();
              List<EvaluationResultI> evResults =
                  regelwerkControl.getEvaluationResults(dokuNameDTO.getId());
              if (evResults.isEmpty()) {
                return "";
              } else {
                System.out.println(evResults.size() - 1);
                return evResults.get(evResults.size() - 1).getResultAsString();
              }
            })
        .setHeader("Fortschritt");
    gridDocumentation.setAllRowsVisible(true);
    gridDocumentation.setMaxHeight("50%");

    // Empty check
    gridDocumentation.setItems(this.dokuNameDTOList);

    // Header
    H3 headline = new H3("Arc42 Dokumentation");
    headline.addClassNames("text-l", "m-m");
    Button create = new Button("Neue Dokumentation erstellen");
    create.getStyle().set("background", "#085c94");
    HorizontalLayout header = new HorizontalLayout(headline, create);
    header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    header.expand(headline);
    header.setWidth("100%");
    header.addClassNames("py-0", "px-m");

    // Button-Layout
    HorizontalLayout buttonLayout = new HorizontalLayout();
    Button update = new Button("Bearbeiten");
    create.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    Button delete = new Button("Löschen");
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    delete.getStyle().set("margin-inline-end", "auto");
    Button evaluation = new Button("Bewertung");
    buttonLayout.add(update, delete, evaluation);
    buttonLayout.setVisible(false);

    add(header, gridDocumentation, buttonLayout);

    create.addClickListener(
        buttonClickEvent -> {
          DialogComponent dialogComponent = new DialogComponent("createDoku");
          dialogComponent.getCreateButton().addClickShortcut(Key.ENTER);
          dialogComponent
              .getCreateButton()
              .addClickListener(
                  (ComponentEventListener<ClickEvent<Button>>)
                      event -> {
                        //                        if (arcId == null) {
                        DokuNameDTO result =
                            dao.save(new DokuNameDTO(dialogComponent.getTitel().getValue()));
                        if (result != null && !result.getName().isEmpty()) {
                          dialogComponent.getDialog().close();
                          arcId = result.getId();
                          if (new RegelwerkControl().createRegelwerk(String.valueOf(arcId))) {
                            System.out.println("Regelwerk for " + arcId + " created");
                          } else {
                            System.out.println("Error on creating Regelwerk for " + arcId);
                          }
                          UI.getCurrent().navigate("arc42/" + arcId + "/titel");
                        }
                        //                        }
                      });
        });
    update.addClickListener(
        buttonClickEvent -> UI.getCurrent().navigate("arc42/" + dokuNameDTO.getId() + "/titel"));

    delete.addClickListener(
        buttonClickEvent -> {
          DialogComponent dialogComponent = new DialogComponent("delete");
          dialogComponent
              .getDeleteButton()
              .addClickListener(
                  (ComponentEventListener<ClickEvent<Button>>)
                      buttonClickEvent1 -> {
                        if (arcId != null) {
                          Boolean result = dao.delete1(dokuNameDTO);
                          if (result != null && result) {
                            dialogComponent.getDialog().close();
                            buttonLayout.setVisible(false);
                            new NotificationWindow(
                                "Die ARC42 Dokumentation wurde erfolgreich gelöscht!",
                                4000,
                                "success");
                            fillPossibleArc42Doku();
                            gridDocumentation.getDataProvider().refreshAll();
                          } else {
                            new NotificationWindow(
                                "Die ARC42 Dokumentation konnte nicht gelöscht werden!",
                                4000,
                                "error");
                          }
                        }
                      });
        });

    gridDocumentation.addSelectionListener(
        selection -> {
          Optional<DokuNameDTO> optionalDoku = selection.getFirstSelectedItem();
          if (optionalDoku.isPresent()) {
            dokuNameDTO = optionalDoku.get();
            arcId = optionalDoku.get().getId();
            buttonLayout.setVisible(true);
          } else {
            buttonLayout.setVisible(false);
          }
        });

    evaluation.addClickListener(
        event -> {
          UI.getCurrent().navigate("arc42/" + dokuNameDTO.getId() + "/bewertung/ergebnisse");
        });
  }

  public void init() {
    new Arc42DocumentationView();
  }

  private void fillPossibleArc42Doku() {
    Arc42DokuNameDAO dao2 = new Arc42DokuNameDAO();
    dokuNameDTOList.clear();
    List<DokuNameDTO> arc42Dokus = null;
    try {
      arc42Dokus = dao2.findAll(null);
    } catch (DatabaseException dbe) {
      // Notification.show("DB-Exception", dbe.getMessage(), Notification.Type.ERROR_MESSAGE);
    } catch (Exception e) {
      // Notification.show("Exception", e.getMessage(), Notification.Type.ERROR_MESSAGE);
    }

    if (arc42Dokus == null) {
      // Notification.show("Exception", "No Arc42 Documentation available!",
      // Notification.Type.ERROR_MESSAGE);
    } else {
      for (DokuNameDTO name : arc42Dokus) {
        this.dokuNameDTOList.add(name);
      }
    }
  }
}