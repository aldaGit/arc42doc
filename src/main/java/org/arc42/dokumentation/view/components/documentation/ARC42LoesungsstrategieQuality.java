package org.arc42.dokumentation.view.components.documentation;

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
import org.arc42.dokumentation.model.dao.arc42documentation.NachhaltigkeitszieleDAO;
import org.arc42.dokumentation.model.dto.documentation.LoesungsStrategieDTO;
import org.arc42.dokumentation.model.dto.documentation.NachhaltigkeitszieleDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@Route(value = "loesungsstrategie/quality", layout = MainLayout.class)
public class ARC42LoesungsstrategieQuality extends DocumentationView {
  LoesungsStrategieDAO dao;
  List<NachhaltigkeitszieleDTO> nachhaltigkeitsziele;

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
    getLoesungsstrategieQuality()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    dao = LoesungsStrategieDAO.getInstance();
    VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setSizeFull();

    /*        H3 qualityGoalsHeader = new H3("Qualitätsziele");
    List<QualityGoalDTO> qualityGoals = QualityGoalDAO.getInstance().findAll(url);
    Accordion accordion1 = new Accordion();
    accordion1.setSizeFull();
    accordion1.close();
    for(QualityGoalDTO q: qualityGoals) {
        addQualityDTOToAccordion(accordion1, q);
    }*/

    H3 nachhaltigkeitszieleHeader = new H3("Nachhaltigkeitsziele");
    nachhaltigkeitsziele = NachhaltigkeitszieleDAO.getInstance().findAll(url);
    verticalLayout.add(
        new BreadCrumbComponent(new String[] {"Lösungsstrategie", "Nachhaltigkeitsziele"}));
    if (nachhaltigkeitsziele.isEmpty()) {
      verticalLayout.add(
          nachhaltigkeitszieleHeader, new Paragraph("Füge zunächst Nachhaltigkeitsziele hinzu."));
    } else {
      Accordion accordion = new Accordion();
      accordion.setSizeFull();
      accordion.close();
      for (NachhaltigkeitszieleDTO n : nachhaltigkeitsziele) {
        TextArea qualityGoalTextArea = new TextArea();
        qualityGoalTextArea.setLabel("Lösungsstrategie");
        qualityGoalTextArea.setSizeFull();
        LoesungsStrategieDTO loesungsStrategieDTO = dao.findByNachhaltigkeitsziel(n);
        qualityGoalTextArea.setValue(loesungsStrategieDTO.getStrategy());
        qualityGoalTextArea.setValueChangeMode(ValueChangeMode.EAGER);
        qualityGoalTextArea.addValueChangeListener(
            e -> {
              loesungsStrategieDTO.setStrategy(qualityGoalTextArea.getValue());
              dao.update(loesungsStrategieDTO);
            });
        accordion.add(n.getId() + ": " + n.getQualitaetsziel(), qualityGoalTextArea);
      }
      verticalLayout.add(nachhaltigkeitszieleHeader, accordion);
    }
    add(verticalLayout);
  }
}
