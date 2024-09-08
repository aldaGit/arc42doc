package org.arc42.dokumentation.view.evaluation.editRules;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.Route;
import org.arc42.analyse.model.evaluation.dao.VerteilungssichtRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.VerteilungssichtRegelsatz;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@Route(value = "verteilungssicht", layout = MainLayout.class)
public class EditVerteilungssichtRegeln extends EditRegelnView {

  protected void init() {
    super.init();
    verteilungssicht
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    VerticalLayout verticalLayout = new VerticalLayout();
    H3 header = new H3("Regeln für die Verteilungssicht");

    VerteilungssichtRegelsatz vsRegelsatz =
        regelwerkControl.getRegelwerk(url).getVerteilungssichtRegelsatz();
    Binder<VerteilungssichtRegelsatz> binder = new Binder<>(VerteilungssichtRegelsatz.class);

    String textAreaWidth = "400px";

    TextArea diagramRegel = new TextArea("Regeln zum Hochladen eines Diagramms");
    diagramRegel.setValue(
        "Für eine Verteilungssicht soll ein Diagramm als Bild und als UXF-Datei hochgeladen"
            + " werden.");
    diagramRegel.setReadOnly(true);
    diagramRegel.setWidth(textAreaWidth);

    TextArea beschreibungRegel = new TextArea("Regel zur Beschreibung");
    beschreibungRegel.setValue(
        "Zu einer Verteilungssicht soll stets eine Beschreibung dokumentiert werden.");
    beschreibungRegel.setReadOnly(true);
    beschreibungRegel.setWidth(textAreaWidth);

    String sliderWidth = "250px";

    // Slider für Hardwarekomponenten
    SliderComponent slider =
        new SliderComponent(
            "Mindestanzahl Hardwarekomponenten",
            sliderWidth,
            vsRegelsatz.getMinHardware(),
            vsRegelsatz.getMaxHardware(),
            vsRegelsatz.getSollHardware());
    binder.bind(
        slider.getSlider(),
        VerteilungssichtRegelsatz::getSollHardware,
        VerteilungssichtRegelsatz::setSollHardware);

    binder.readBean(vsRegelsatz);

    SaveButtonComponent saveButton =
        new SaveButtonComponent(vsRegelsatz, binder, VerteilungssichtRegelsatzDAO.getInstance());

    VerticalLayout rules = new VerticalLayout(diagramRegel, beschreibungRegel, slider);
    HorizontalLayout rulesLayout = new HorizontalLayout(rules);
    verticalLayout.add(header, rulesLayout, saveButton);
    verticalLayout.setAlignItems(Alignment.CENTER);
    add(verticalLayout);
  }
}
