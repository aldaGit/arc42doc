package org.arc42.dokumentation.view.components.documentation;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dao.arc42documentation.LoesungsStrategieDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.QualityGoalDAO;
import org.arc42.dokumentation.model.dto.documentation.LoesungsStrategieDTO;
import org.arc42.dokumentation.model.dto.documentation.QualityDTO;
import org.arc42.dokumentation.model.dto.documentation.QualityGoalDTO;
import org.arc42.dokumentation.model.dto.documentation.QualityScenarioDTO;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.customComponents.BadgeComponent;
import org.arc42.dokumentation.view.util.data.NotificationType;

public class QualityCriteriaComponent extends HorizontalLayout {

  private final ARC42DAOAbstract dao;
  private final VerticalLayout selectedBatch;
  private final VerticalLayout notSelectedLayout;
  private final ButtonComponent buttonComponent;
  private final Grid qualityG;
  private QualityDTO qualityDTO;
  private VerticalLayout notSelectedBatch;
  private TextFieldComponent textFieldComponent;

  public QualityCriteriaComponent(Grid qualityG, ARC42DAOAbstract dao) {
    this.qualityG = qualityG;
    this.dao = dao;

    VerticalLayout selectedLayout = new VerticalLayout();
    Label labelAusg = new Label("Ausgewählt");
    selectedLayout.add(labelAusg);

    notSelectedLayout = new VerticalLayout();
    Label labelNichtAusg = new Label("Nicht ausgewählt");
    notSelectedLayout.add(labelNichtAusg);

    notSelectedBatch = new BadgeComponent().getAllBadges();
    selectedBatch = new VerticalLayout();
    addListener();

    selectedLayout.add(selectedBatch);
    notSelectedLayout.add(notSelectedBatch);
    VerticalLayout verticalLayout = new VerticalLayout();
    buttonComponent = new ButtonComponent();
    if (dao instanceof QualityGoalDAO) {
      textFieldComponent = new TextFieldComponent("Qualitätsziel", "Motivation");
      verticalLayout.add(textFieldComponent, buttonComponent);
    }

    VerticalLayout main = new VerticalLayout();
    HorizontalLayout interactionLayout =
        new HorizontalLayout(verticalLayout, selectedLayout, notSelectedLayout);
    main.add(interactionLayout);
    setSizeFull();
    add(main);
  }

  public VerticalLayout getSelectedBatch() {
    return selectedBatch;
  }

  public void updateList() {
    List<QualityGoalDTO> qualityGoalDTOS = dao.findAll(null);
    qualityG.setItems(qualityGoalDTOS);
  }

  private void createListener(BadgeComponent badgeComponent) {
    badgeComponent.addClickListener(
        (ComponentEventListener<ClickEvent<Span>>)
            event -> {
              if (badgeComponent.getParent().get() == notSelectedBatch
                  && badgeComponent.getParent().get().getChildren().count() > 6) {
                notSelectedBatch.remove(badgeComponent);
                selectedBatch.add(badgeComponent);
              } else if (badgeComponent.getParent().get() == selectedBatch) {
                selectedBatch.remove(badgeComponent);
                notSelectedBatch.add(badgeComponent);
              } else {
                new NotificationWindow(
                    "Sie können nur maximal drei Kategorien pro Ziel auswählen!",
                    NotificationType.SHORT,
                    NotificationType.ERROR);
              }
            });
  }

  public List<String> getCategories(VerticalLayout component) {
    List<String> list = new ArrayList<>();
    for (int c = 0; c < component.getChildren().count(); c++) {
      if (component.getComponentAt(c) instanceof BadgeComponent) {
        list.add(((BadgeComponent) component.getComponentAt(c)).getText());
      }
    }
    return list;
  }

  private void setCategories(QualityDTO qualityGoalDTO) {
    selectedBatch.removeAll();
    notSelectedBatch.removeAll();
    List<BadgeComponent> list = BadgeComponent.getAllBadgesAsList();

    for (Object criteria : qualityGoalDTO.getQualityCriteria()) {
      for (BadgeComponent c : list) {
        if (c.getText().equals(criteria)) {
          selectedBatch.add(c);
          list.remove(c);
          break;
        }
      }
    }
    for (BadgeComponent c : list) {
      notSelectedBatch.add(c);
    }
    addListener();
  }

  public void clearBatch() {
    selectedBatch.removeAll();
    notSelectedBatch = new BadgeComponent().getAllBadges();
    addListener();
    notSelectedLayout.replace(notSelectedLayout.getComponentAt(1), notSelectedBatch);
  }

  private void addListener() {
    for (int i = 0; i < notSelectedBatch.getComponentCount(); i++) {
      if (notSelectedBatch.getComponentAt(i) instanceof BadgeComponent) {
        createListener((BadgeComponent) notSelectedBatch.getComponentAt(i));
      }
    }
    for (int i = 0; i < selectedBatch.getComponentCount(); i++) {
      if (selectedBatch.getComponentAt(i) instanceof BadgeComponent) {
        createListener((BadgeComponent) selectedBatch.getComponentAt(i));
      }
    }
  }

  public void selectedCell(QualityDTO qualityDTO, boolean isPresent) {
    if (isPresent && qualityDTO != null) {
      if (qualityDTO instanceof QualityGoalDTO) {
        textFieldComponent.getTextField1().setValue(qualityDTO.getQualitaetsziel());
        textFieldComponent.getTextField2().setValue(qualityDTO.getMotivation());
      }
      setCategories(qualityDTO);
      buttonComponent.setVisible(true);
    } else {
      if (qualityDTO instanceof QualityGoalDTO) {
        textFieldComponent.clear();
      }
      buttonComponent.setVisible(false);
      clearBatch();
    }
  }

  public void addButtonListener(QualityDTO qualityDTO) {
    buttonComponent
        .getEdit()
        .addClickListener(
            (ComponentEventListener<ClickEvent<Button>>)
                event -> {
                  if (getCategories(selectedBatch).size() != 0) {
                    qualityDTO.setQualitaetsziel(textFieldComponent.getTextField1().getValue());
                    qualityDTO.setMotivation(textFieldComponent.getTextField2().getValue());
                    qualityDTO.setQualityCriteria(getCategories(selectedBatch));
                    this.dao.update(qualityDTO);
                    textFieldComponent.clear();
                    updateList();
                    new NotificationWindow(
                        "Qualitätsziel erfolgreich geändert!",
                        NotificationType.SHORT,
                        NotificationType.SUCCESS);
                  } else {
                    new NotificationWindow(
                        "Sie müssen mindestens einen Kategorie auswählen!",
                        NotificationType.MEDIUM,
                        NotificationType.ERROR);
                  }
                });

    buttonComponent
        .getDelete()
        .addClickListener(
            (ComponentEventListener<ClickEvent<Button>>)
                event -> {
                  this.dao.delete(qualityDTO);
                  textFieldComponent.clear();
                  updateList();
                  new NotificationWindow(
                      "Qualitätsziel erfolgreich gelöscht!",
                      NotificationType.SHORT,
                      NotificationType.NEUTRAL);
                });
  }

  public void addCreateListener() {
    buttonComponent
        .getCreate()
        .addClickListener(
            (ComponentEventListener<ClickEvent<Button>>)
                event -> {
                  if (!textFieldComponent.isEmpty()) {
                    List list = getCategories(selectedBatch);
                    if (list.size() != 0) {
                      if (dao instanceof QualityGoalDAO) {
                        qualityDTO =
                            new QualityGoalDTO(
                                textFieldComponent.getTextField1().getValue(),
                                textFieldComponent.getTextField2().getValue(),
                                getCategories(selectedBatch));
                        QualityGoalDTO dto = (QualityGoalDTO) dao.createRelationship(qualityDTO);
                        LoesungsStrategieDAO.getInstance().save(new LoesungsStrategieDTO(""), dto);
                      } else {
                        qualityDTO =
                            new QualityScenarioDTO(
                                textFieldComponent.getTextField1().getValue(),
                                textFieldComponent.getTextField2().getValue(),
                                getCategories(selectedBatch));
                        dao.createRelationship(qualityDTO);
                      }
                      textFieldComponent.clear();
                      updateList();
                      clearBatch();
                    } else {
                      new NotificationWindow(
                          "Sie müssen mindestens einen Kategorie auswählen!",
                          NotificationType.MEDIUM,
                          NotificationType.ERROR);
                    }
                  } else {
                    new NotificationWindow(
                        "Sie müssen beide Textfelder füllen!",
                        NotificationType.MEDIUM,
                        NotificationType.ERROR);
                  }
                });
  }
}
