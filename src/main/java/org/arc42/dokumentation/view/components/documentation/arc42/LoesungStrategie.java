package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import java.util.List;
import org.arc42.dokumentation.model.dao.arc42documentation.LoesungsStrategieDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.QualityGoalDAO;
import org.arc42.dokumentation.model.dto.documentation.LoesungsStrategieDTO;
import org.arc42.dokumentation.model.dto.documentation.QualityGoalDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@Route(value = "loesungsstrategie/allgemein", layout = MainLayout.class)
public class LoesungStrategie extends DocumentationView {

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
    getLoesungsstrategie()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getLoesungsstrategie().setOpened(true);
    getLoesungsstrategieAllgemein()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    LoesungsStrategieDAO dao = LoesungsStrategieDAO.getInstance();
    VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setSizeFull();

    H3 header = new H3("Allgemeine Lösungsstrategie");
    List<QualityGoalDTO> qualityGoals = QualityGoalDAO.getInstance().findAll(url);
    verticalLayout.add(new BreadCrumbComponent(new String[] {"Lösungsstrategie", "Allgemein"}));
    if (qualityGoals.isEmpty()) {
      verticalLayout.add(header, new Paragraph("Füge zunächst Qualitätsziele hinzu."));
    } else {
      Accordion accordion = new Accordion();
      accordion.setSizeFull();
      accordion.close();
      for (QualityGoalDTO qualityGoalDTO : qualityGoals) {
        TextArea qualityGoalTextArea = new TextArea();
        qualityGoalTextArea.setLabel("Lösungsstrategie");
        qualityGoalTextArea.setSizeFull();
        LoesungsStrategieDTO loesungsStrategieDTO = dao.findByQualityGoalId(qualityGoalDTO);
        qualityGoalTextArea.setValue(loesungsStrategieDTO.getStrategy());
        qualityGoalTextArea.setValueChangeMode(ValueChangeMode.EAGER);
        qualityGoalTextArea.addValueChangeListener(
            e -> {
              loesungsStrategieDTO.setStrategy(qualityGoalTextArea.getValue());
              dao.update(loesungsStrategieDTO);
            });
        accordion.add(
            qualityGoalDTO.getId() + ": " + qualityGoalDTO.getQualitaetsziel(),
            qualityGoalTextArea);
      }
      verticalLayout.add(header, accordion);
    }
    add(verticalLayout);
  }
}
