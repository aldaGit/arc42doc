package org.arc42.dokumentation.view.evaluation.editRules;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.Route;
import org.arc42.analyse.model.evaluation.dao.KontextRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.KontextRegelsatz;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@Route(value = "kontextabgrenzung", layout = MainLayout.class)
public class EditKontextRegeln extends EditRegelnView {

  protected void init() {
    super.init();
    kontextabgrenzung
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    VerticalLayout verticalLayout = new VerticalLayout();
    H3 header = new H3("Regeln für die Kontextabgrenzung");

    KontextRegelsatz kRegelsatz = regelwerkControl.getRegelwerk(url).getKontextRegelsatz();
    Binder<KontextRegelsatz> binder = new Binder<>(KontextRegelsatz.class);

    String sliderWidth = "250px";
    String textAreaWidth = "350px";

    // Slider für fachlich
    SliderComponent sliderF =
        new SliderComponent(
            "Mindestanzahl fachlicher Kontext",
            sliderWidth,
            kRegelsatz.getMinFachlich(),
            kRegelsatz.getMaxFachlich(),
            kRegelsatz.getSollFachlich());
    binder.bind(
        sliderF.getSlider(), KontextRegelsatz::getSollFachlich, KontextRegelsatz::setSollFachlich);

    TextArea fkRegeln = new TextArea("Regeln zum fachlichen Kontext");
    fkRegeln.setValue(
        "Zu einem Kommunikationspartner soll stets ein Input, Output, Beschreibung und Risiken"
            + " eingetragen werden.");
    fkRegeln.setReadOnly(true);
    fkRegeln.setWidth(textAreaWidth);

    // Slider für technisch
    SliderComponent sliderT =
        new SliderComponent(
            "Mindestanzahl technischer Kontext",
            sliderWidth,
            kRegelsatz.getMinTechnisch(),
            kRegelsatz.getMaxTechnisch(),
            kRegelsatz.getSollTechnisch());
    binder.bind(
        sliderT.getSlider(),
        KontextRegelsatz::getSollTechnisch,
        KontextRegelsatz::setSollTechnisch);

    TextArea tkRegeln = new TextArea("Regeln zum technischen Kontext");
    tkRegeln.setValue(
        "Zu einer Schnittstelle soll stets eine Beschreibung, Aufrufe pro Monat und CO2-Emissionen"
            + " eingetragen werden.");
    tkRegeln.setReadOnly(true);
    tkRegeln.setWidth(textAreaWidth);

    binder.readBean(kRegelsatz);

    SaveButtonComponent saveButton =
        new SaveButtonComponent(kRegelsatz, binder, KontextRegelsatzDAO.getInstance());

    VerticalLayout fachlichVL = new VerticalLayout(sliderF, fkRegeln);
    VerticalLayout technischVL = new VerticalLayout(sliderT, tkRegeln);
    HorizontalLayout regelnHL = new HorizontalLayout(fachlichVL, technischVL);
    verticalLayout.add(header, regelnHL, saveButton);
    verticalLayout.setAlignItems(Alignment.CENTER);
    add(verticalLayout);
  }
}
