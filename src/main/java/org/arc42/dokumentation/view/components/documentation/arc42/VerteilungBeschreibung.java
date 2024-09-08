package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dao.arc42documentation.ImageDAOVerteilung;
import org.arc42.dokumentation.model.dto.documentation.ImageDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.TextAreaComponent;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@Route(value = "verteilungssicht/beschreibung", layout = MainLayout.class)
public class VerteilungBeschreibung extends DocumentationView {

  ARC42DAOAbstract<ImageDTO, String> dao;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    event.getRouteParameters().get("arcId").get();
    super.beforeEnter(event);
  }

  @Override
  public void init() {
    super.init();
    getVerteilungssicht()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getVerteilungssicht().setOpened(true);
    getVerteilungssichtBeschreibung()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    dao = ImageDAOVerteilung.getInstance();
    ImageDTO dto = dao.findById(url);

    VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setSizeFull();
    verticalLayout.add(new BreadCrumbComponent(new String[] {"Laufzeitsicht", "Beschreibung"}));

    if (dto != null) {
      TextAreaComponent textAreaComponent =
          new TextAreaComponent(
              "Verteilungsicht-Beschreibung",
              dto.getDescription().equals("null") ? "" : dto.getDescription(),
              dao);
      textAreaComponent
          .getTextArea()
          .addValueChangeListener(
              (HasValue.ValueChangeListener<
                      AbstractField.ComponentValueChangeEvent<TextArea, String>>)
                  event -> hasChanges(true));
      verticalLayout.add(textAreaComponent);
    } else {
      verticalLayout.add(
          new H3(
              "Sie müssen vorher ein Bild und eine UFX-Datei hochladen, bevor sie hier die"
                  + " Verteilungssicht beschreiben!"));
    }

    add(verticalLayout);
  }
}