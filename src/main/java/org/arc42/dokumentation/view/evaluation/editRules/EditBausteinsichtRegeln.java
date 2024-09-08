package org.arc42.dokumentation.view.evaluation.editRules;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.Route;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@Route(value = "bausteinsicht", layout = MainLayout.class)
public class EditBausteinsichtRegeln extends EditRegelnView {

  protected void init() {
    super.init();
    bausteinsicht.getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    VerticalLayout verticalLayout = new VerticalLayout();
    H3 header = new H3("Regeln für die Bausteinsicht");

    String textAreaWidth = "400px";

    TextArea diagramRegel = new TextArea("Regeln zum Hochladen eines Diagramms");
    diagramRegel.setValue(
        "Für eine Bausteinsicht soll ein Diagramm als Bild und als UXF-Datei hochgeladen werden.");
    diagramRegel.setReadOnly(true);
    diagramRegel.setWidth(textAreaWidth);

    TextArea beschreibungRegel = new TextArea("Regel zur Beschreibung");
    beschreibungRegel.setValue(
        "Zu einer Bausteinsicht soll stets eine Beschreibung dokumentiert werden.");
    beschreibungRegel.setReadOnly(true);
    beschreibungRegel.setWidth(textAreaWidth);

    verticalLayout.add(header, diagramRegel, beschreibungRegel);
    verticalLayout.setAlignItems(Alignment.CENTER);
    add(verticalLayout);
  }
}
