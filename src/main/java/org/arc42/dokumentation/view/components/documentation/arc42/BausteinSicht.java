package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.Route;
import org.arc42.dokumentation.model.dao.arc42documentation.ImageDAOBaustein;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.UploadComponent;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@Route(value = "bausteinsicht/diagramm", layout = MainLayout.class)
public class BausteinSicht extends DocumentationView {

  @Override
  public void init() {
    super.init();
    getBausteinsicht()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getBausteinsicht().setOpened(true);
    getBausteinsichtDiagramm()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    ImageDAOBaustein dao = ImageDAOBaustein.getInstance();
    VerticalLayout verticalLayout = new VerticalLayout();
    H3 header = new H3("Bausteinsicht Diagramm");

    UploadComponent uploadComponent = new UploadComponent(dao, url);

    verticalLayout.setSizeFull();
    verticalLayout.add(
        new BreadCrumbComponent(new String[] {"Bausteinsicht", "Diagramm"}),
        header,
        uploadComponent);
    add(verticalLayout);
  }
}