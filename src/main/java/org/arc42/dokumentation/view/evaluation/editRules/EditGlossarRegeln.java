package org.arc42.dokumentation.view.evaluation.editRules;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.Route;
import org.arc42.analyse.model.evaluation.dao.GlossarRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.GlossarRegelsatz;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@Route(value = "glossar", layout = MainLayout.class)
public class EditGlossarRegeln extends EditRegelnView {

  protected void init() {
    super.init();
    glossar.getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    VerticalLayout verticalLayout = new VerticalLayout();
    H3 header = new H3("Regeln für das Glossar");

    GlossarRegelsatz glRegelsatz = regelwerkControl.getRegelwerk(url).getGlossarRegelsatz();
    Binder<GlossarRegelsatz> binder = new Binder<>(GlossarRegelsatz.class);

    String fieldWidth = "330px";

    // Integer Field min
    IntegerField minWortanzahl = new IntegerField("Mindestanzahl Wörter zur Beschreibung");
    minWortanzahl.setWidth(fieldWidth);
    minWortanzahl.setMin(1);
    binder.bind(
        minWortanzahl, GlossarRegelsatz::getMinWortanzahl, GlossarRegelsatz::setMinWortanzahl);

    // Integer Field max
    IntegerField maxWortanzahl = new IntegerField("Maximale Anzahl an Wörtern zur Beschreibung");
    maxWortanzahl.setWidth(fieldWidth);
    maxWortanzahl.setMin(1);
    binder.bind(
        maxWortanzahl, GlossarRegelsatz::getMaxWortanzahl, GlossarRegelsatz::setMaxWortanzahl);

    if (!glRegelsatz.isChecked()) {
      minWortanzahl.setEnabled(false);
      maxWortanzahl.setEnabled(false);
    }

    // Checkbox
    Checkbox checkbox = new Checkbox("Wortanzahl soll gecheckt werden");
    binder.bind(checkbox, GlossarRegelsatz::isChecked, GlossarRegelsatz::setChecked);

    checkbox.addValueChangeListener(
        event -> {
          minWortanzahl.setEnabled(event.getValue());
          maxWortanzahl.setEnabled(event.getValue());
        });

    binder.readBean(glRegelsatz);

    SaveButtonComponent saveButton =
        new SaveButtonComponent(glRegelsatz, binder, GlossarRegelsatzDAO.getInstance());

    verticalLayout.add(header, minWortanzahl, maxWortanzahl, checkbox, saveButton);
    verticalLayout.setAlignItems(Alignment.CENTER);
    add(verticalLayout);
  }
}
