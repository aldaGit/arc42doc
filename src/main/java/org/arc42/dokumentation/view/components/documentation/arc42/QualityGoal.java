package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import java.util.List;
import java.util.Optional;
import org.arc42.dokumentation.model.dao.arc42documentation.QualityGoalDAO;
import org.arc42.dokumentation.model.dto.documentation.QualityGoalDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.documentation.QualityCriteriaComponent;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@Route(value = "qualitaetsziele", layout = MainLayout.class)
public class QualityGoal extends DocumentationView {

  private QualityGoalDTO qualityGoalDTO;
  private QualityCriteriaComponent qualityCriteria;
  private QualityGoalDAO dao;

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
    getQualitaetsziele()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    this.dao = QualityGoalDAO.getInstance();
    List<QualityGoalDTO> qualityGoalDTOS = dao.findAll(url);
    Grid<QualityGoalDTO> qualityGoalG = new Grid(QualityGoalDTO.class, false);
    qualityGoalG.setAllRowsVisible(true);

    VerticalLayout main = new VerticalLayout();
    qualityGoalG.addColumn(QualityGoalDTO::getId).setHeader("ID");
    qualityGoalG.addColumn(QualityGoalDTO::getQualitaetsziel).setHeader("Qualitätsziel");
    qualityGoalG.addColumn(QualityGoalDTO::getMotivation).setHeader("Motivation");
    qualityGoalG
        .addColumn(new ComponentRenderer<>(QualityGoalDTO::generateBadge))
        .setHeader("Kategorie");
    qualityGoalG.setItems(qualityGoalDTOS);

    qualityCriteria = new QualityCriteriaComponent(qualityGoalG, dao);
    qualityCriteria.addCreateListener();
    qualityGoalG.addSelectionListener(
        (SelectionListener<Grid<QualityGoalDTO>, QualityGoalDTO>)
            selectionEvent -> {
              Optional<QualityGoalDTO> optionalDoku = selectionEvent.getFirstSelectedItem();
              if (optionalDoku.isPresent()) {
                qualityGoalDTO = optionalDoku.get();
                qualityCriteria.addButtonListener(qualityGoalDTO);
              }
              qualityCriteria.selectedCell(qualityGoalDTO, optionalDoku.isPresent());
            });

    main.add(
        new BreadCrumbComponent(new String[] {"Einführung und Ziele", "Qualitätsziele"}),
        qualityGoalG,
        qualityCriteria);
    add(main);
  }
}