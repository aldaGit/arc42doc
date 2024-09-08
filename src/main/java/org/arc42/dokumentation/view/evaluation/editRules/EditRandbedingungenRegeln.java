package org.arc42.dokumentation.view.evaluation.editRules;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.Route;
import org.arc42.analyse.model.evaluation.dao.RandbedingungenRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.RandbedingungenRegelsatz;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@Route(value = "randbedingungen", layout = MainLayout.class)
public class EditRandbedingungenRegeln extends EditRegelnView {

  protected void init() {
    super.init();
    randbedingungen.getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    VerticalLayout verticalLayout = new VerticalLayout();
    H3 header = new H3("Regeln für Randbedingungen");

    RandbedingungenRegelsatz rbRegelsatz =
        regelwerkControl.getRegelwerk(url).getRandbedingungenRegelsatz();
    Binder<RandbedingungenRegelsatz> binder = new Binder<>(RandbedingungenRegelsatz.class);

    String sliderWidth = "250px";

    // Slider für technisch
    SliderComponent sliderT =
        new SliderComponent(
            "Mindestanzahl technische Randbedingungen",
            sliderWidth,
            rbRegelsatz.getMinTechnisch(),
            rbRegelsatz.getMaxTechnisch(),
            rbRegelsatz.getSollTechnisch());
    binder.bind(
        sliderT.getSlider(),
        RandbedingungenRegelsatz::getSollTechnisch,
        RandbedingungenRegelsatz::setSollTechnisch);

    // Slider für organisatorisch
    SliderComponent sliderO =
        new SliderComponent(
            "Mindestanzahl organisatorische Randbedingungen",
            sliderWidth,
            rbRegelsatz.getMinOrgan(),
            rbRegelsatz.getMaxOrgan(),
            rbRegelsatz.getSollOrgan());
    binder.bind(
        sliderO.getSlider(),
        RandbedingungenRegelsatz::getSollOrgan,
        RandbedingungenRegelsatz::setSollOrgan);

    // Slider für Konventionen
    SliderComponent sliderK =
        new SliderComponent(
            "Mindestanzahl Konventionen",
            sliderWidth,
            rbRegelsatz.getMinKonventionen(),
            rbRegelsatz.getMaxKonventionen(),
            rbRegelsatz.getSollKonventionen());
    binder.bind(
        sliderK.getSlider(),
        RandbedingungenRegelsatz::getSollKonventionen,
        RandbedingungenRegelsatz::setSollKonventionen);

    binder.readBean(rbRegelsatz);

    SaveButtonComponent saveButton =
        new SaveButtonComponent(rbRegelsatz, binder, RandbedingungenRegelsatzDAO.getInstance());

    VerticalLayout sliderVL = new VerticalLayout(sliderT, sliderO, sliderK);
    HorizontalLayout sliderHL = new HorizontalLayout(sliderVL);
    verticalLayout.add(header, sliderHL, saveButton);
    verticalLayout.setAlignItems(Alignment.CENTER);
    add(verticalLayout);
  }
}
