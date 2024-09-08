package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dao.arc42documentation.QualityGoalDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.QualityScenarioDAO;
import org.arc42.dokumentation.model.dto.documentation.QualityDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.GraphBuilder;

@Route(value = "quality/baum", layout = MainLayout.class)
public class QualityTree extends DocumentationView {

  @Override
  public void init() {
    super.init();
    getQualitaetsszenarien()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getQualitaetsszenarien().setOpened(true);
    getQualitaetsbaum()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    ARC42DAOAbstract dao = QualityGoalDAO.getInstance();
    // TODO: FIX issues with this implementation, dao has undefined behaviour
    List<QualityDTO> qualityGoalDTOS = dao.findAll(url);
    dao = QualityScenarioDAO.getInstance();
    qualityGoalDTOS.addAll(dao.findAll(url));

    Grid<QualityDTO> qualityGoalG = new Grid(QualityDTO.class, false);
    qualityGoalG.addColumn(QualityDTO::toString).setHeader("ID");
    qualityGoalG
        .addColumn(new ComponentRenderer<>(QualityDTO::generateBadge))
        .setHeader("Kategorie");
    qualityGoalG.setItems(qualityGoalDTOS);
    qualityGoalG.setAllRowsVisible(true);
    qualityGoalG.setWidthFull();
    qualityGoalG.setSelectionMode(Grid.SelectionMode.NONE);

    Element image = new Element("object");
    image.setAttribute("type", "image/svg+xml");
    image.getStyle().set("display", "block");

    StreamResource resource =
        new StreamResource("image.svg", () -> getImageInputStream(qualityGoalDTOS));
    image.setAttribute("data", resource);

    VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setSizeFull();
    verticalLayout.add(new BreadCrumbComponent(new String[] {"Qualitätszenarien", "Baum"}));
    verticalLayout.getElement().appendChild(image);
    verticalLayout.add(qualityGoalG);
    add(verticalLayout);
  }

  private InputStream getImageInputStream(List<QualityDTO> qualityGoalDTOS) {
    GraphBuilder graphBuilder = new GraphBuilder();
    String svg;
    svg =
        "<?xml version='1.0' encoding='UTF-8' standalone='no'?>"
            + graphBuilder.generateGraph(qualityGoalDTOS);

    return new ByteArrayInputStream(svg.getBytes(StandardCharsets.UTF_8));
  }
}
