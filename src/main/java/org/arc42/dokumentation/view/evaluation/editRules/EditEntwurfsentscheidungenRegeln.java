package org.arc42.dokumentation.view.evaluation.editRules;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.Route;
import org.arc42.analyse.model.evaluation.dao.EntwurfsentscheidungenRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.EntwurfsentscheidungenRegelsatz;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@Route(value = "entwurfsentscheidungen", layout = MainLayout.class)
public class EditEntwurfsentscheidungenRegeln extends EditRegelnView {

  protected void init() {
    super.init();
    entwurfsentscheidungen
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    VerticalLayout verticalLayout = new VerticalLayout();
    H3 header = new H3("Regeln für Entwurfsentscheidungen");

    EntwurfsentscheidungenRegelsatz eeRegelsatz =
        regelwerkControl.getRegelwerk(url).getEntwurfsentscheidungenRegelsatz();

    SliderComponent sliderEE =
        new SliderComponent(
            "Mindestanzahl Entwurfsentscheidungen",
            "250px",
            eeRegelsatz.getMinEntwurfsentscheidungen(),
            eeRegelsatz.getMaxEntwurfsentscheidungen(),
            eeRegelsatz.getSollEntwurfsentscheidungen());
    Binder<EntwurfsentscheidungenRegelsatz> binder =
        new Binder<>(EntwurfsentscheidungenRegelsatz.class);
    binder.bind(
        sliderEE.getSlider(),
        EntwurfsentscheidungenRegelsatz::getSollEntwurfsentscheidungen,
        EntwurfsentscheidungenRegelsatz::setSollEntwurfsentscheidungen);

    TextArea eeRegel = new TextArea("Weitere Regel zu Entwurfsentscheidungen");
    eeRegel.setValue(
        "Zu einer Entwurfsentscheidung sollen Konsequenzen, Begründung und Wichtigkeit dokumentiert"
            + " werden.");
    eeRegel.setReadOnly(true);
    eeRegel.setWidth("400px");

    binder.readBean(eeRegelsatz);

    SaveButtonComponent saveButton =
        new SaveButtonComponent(
            eeRegelsatz, binder, EntwurfsentscheidungenRegelsatzDAO.getInstance());

    VerticalLayout rules = new VerticalLayout(sliderEE, eeRegel);
    HorizontalLayout rulesLayout = new HorizontalLayout(rules);
    verticalLayout.add(header, rulesLayout, saveButton);
    verticalLayout.setAlignItems(Alignment.CENTER);
    add(verticalLayout);
  }
}
