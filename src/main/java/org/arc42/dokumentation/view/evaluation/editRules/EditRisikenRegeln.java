package org.arc42.dokumentation.view.evaluation.editRules;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.Route;
import org.arc42.analyse.model.evaluation.dao.RisikenRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.RisikenRegelsatz;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@Route(value = "risiken", layout = MainLayout.class)
public class EditRisikenRegeln extends EditRegelnView {

  protected void init() {
    super.init();
    risiken.getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    VerticalLayout verticalLayout = new VerticalLayout();
    H3 header = new H3("Regeln für Risiken");

    RisikenRegelsatz rRegelsatz = regelwerkControl.getRegelwerk(url).getRisikenRegelsatz();
    Binder<RisikenRegelsatz> binder = new Binder<>(RisikenRegelsatz.class);

    String sliderWidth = "250px";

    SliderComponent sliderCL =
        new SliderComponent(
            "Mindestanzahl Risiken in Checkliste",
            sliderWidth,
            rRegelsatz.getMinRisiken(),
            rRegelsatz.getMaxRisiken(),
            rRegelsatz.getSollRisiken());
    binder.bind(
        sliderCL.getSlider(), RisikenRegelsatz::getSollRisiken, RisikenRegelsatz::setSollRisiken);

    SliderComponent sliderSWOT =
        new SliderComponent(
            "Mindestanzahl Einträge in der SWOT-Analyse",
            sliderWidth,
            rRegelsatz.getMinEntries(),
            rRegelsatz.getMaxEntries(),
            rRegelsatz.getSollEntries());
    binder.bind(
        sliderSWOT.getSlider(), RisikenRegelsatz::getSollEntries, RisikenRegelsatz::setSollEntries);

    binder.readBean(rRegelsatz);

    SaveButtonComponent saveButton =
        new SaveButtonComponent(rRegelsatz, binder, RisikenRegelsatzDAO.getInstance());

    VerticalLayout rules = new VerticalLayout(sliderCL, sliderSWOT);
    HorizontalLayout rulesLayout = new HorizontalLayout(rules);
    verticalLayout.add(header, rulesLayout, saveButton);
    verticalLayout.setAlignItems(Alignment.CENTER);
    add(verticalLayout);
  }
}
