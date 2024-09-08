package org.arc42.dokumentation.view.evaluation.editRules;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.Route;
import org.arc42.analyse.model.evaluation.dao.KonzepteRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.KonzepteRegelsatz;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.BadgeGlossary;
import org.arc42.dokumentation.view.util.SelectBadgeComponent;

@Route(value = "konzepte", layout = MainLayout.class)
public class EditKonzepteRegeln extends EditRegelnView {

  protected void init() {
    super.init();
    konzepte.getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    VerticalLayout verticalLayout = new VerticalLayout();
    H3 header = new H3("Regeln für Konzepte");

    KonzepteRegelsatz kRegelsatz = regelwerkControl.getRegelwerk(url).getKonzepteRegelsatz();

    TextArea regel = new TextArea("Regel zur Dokumentation von Konzepten");
    regel.setValue(
        "Es können Kategorien ausgewählt werden, zu denen ein Konzept dokumentiert werden soll.");
    regel.setReadOnly(true);
    regel.setWidth("400px");

    SelectBadgeComponent selectBadgeComponent =
        new SelectBadgeComponent(
            kRegelsatz.getNeededConceptCategories(), BadgeGlossary.CONCEPTBADGES);

    Button saveButton =
        new Button(
            "Speichern",
            event -> {
              try {
                kRegelsatz.setNeededConceptCategories(selectBadgeComponent.getSelectedBatches());
                KonzepteRegelsatzDAO.getInstance().update(kRegelsatz);
                Notification.show("Regeln gespeichert");
              } catch (Exception e) {
                Notification.show("Regeln konnten nicht gespeichert werden");
              }
            });

    verticalLayout.add(header, regel, selectBadgeComponent, saveButton);
    verticalLayout.setAlignItems(Alignment.CENTER);
    add(verticalLayout);
  }
}