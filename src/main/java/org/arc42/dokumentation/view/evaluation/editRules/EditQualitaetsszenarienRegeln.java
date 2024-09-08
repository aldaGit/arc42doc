package org.arc42.dokumentation.view.evaluation.editRules;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.Route;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@Route(value = "qualitaetsszenarien", layout = MainLayout.class)
public class EditQualitaetsszenarienRegeln extends EditRegelnView {

  protected void init() {
    super.init();
    qualitaetsszenarien
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    VerticalLayout verticalLayout = new VerticalLayout();
    H3 header = new H3("Regeln für Qualitätsszenarien");

    String textAreaWidth = "400px";

    TextArea goalRegel = new TextArea("Regel zur Konkretisierung eines Qualitätsziels");
    goalRegel.setValue(
        "Für jedes in 1.3 dokumentiertes Qualitätsziel soll ein Qualitätsszenario erstellt"
            + " werden.");
    goalRegel.setReadOnly(true);
    goalRegel.setWidth(textAreaWidth);

    TextArea attributRegel = new TextArea("Regel zu Qualitätsszenarien");
    attributRegel.setValue(
        "Zu einem Qualitätsszenario sollen Auslöser, Reaktion, Zielwert, Qualitätsziel, Priorität"
            + " und Risiko dokumentiert werden.");
    attributRegel.setReadOnly(true);
    attributRegel.setWidth(textAreaWidth);

    verticalLayout.add(header, goalRegel, attributRegel);
    verticalLayout.setAlignItems(Alignment.CENTER);
    add(verticalLayout);
  }
}
