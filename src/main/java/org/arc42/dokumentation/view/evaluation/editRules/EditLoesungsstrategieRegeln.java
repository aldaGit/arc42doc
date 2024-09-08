package org.arc42.dokumentation.view.evaluation.editRules;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.Route;
import org.arc42.analyse.model.evaluation.dao.LoesungsstrategieRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.LoesungsstrategieRegelsatz;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@Route(value = "loesungsstrategie", layout = MainLayout.class)
public class EditLoesungsstrategieRegeln extends EditRegelnView {

  protected void init() {
    super.init();
    loesungsstrategie
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    VerticalLayout verticalLayout = new VerticalLayout();
    H3 header = new H3("Regeln für Lösungsstrategie");

    LoesungsstrategieRegelsatz lstrRegelsatz =
        regelwerkControl.getRegelwerk(url).getLoesungsstrategieRegelsatz();
    Binder<LoesungsstrategieRegelsatz> binder = new Binder<>(LoesungsstrategieRegelsatz.class);

    String textAreaWidth = "350px";

    TextArea qgRegeln = new TextArea("Regeln zur allgemeinen Lösungsstrategie");
    qgRegeln.setValue(
        "Zu jedem in 1.3 festgelegtem Qualitätsziel soll eine Lösungsstrategie dokumentiert"
            + " werden.");
    qgRegeln.setReadOnly(true);
    qgRegeln.setWidth(textAreaWidth);

    TextArea nzRegeln = new TextArea("Regeln zur Lösungsstrategie für Nachhaltigkeitsziele");
    nzRegeln.setValue(
        "Zu jedem in 1.5 festgelegtem Nachhaltigkeitsziel soll eine Lösungsstrategie dokumentiert"
            + " werden.");
    nzRegeln.setReadOnly(true);
    nzRegeln.setWidth(textAreaWidth);

    String sliderWidth = "250px";

    // Slider für sollMeetings
    SliderComponent slider =
        new SliderComponent(
            "Mindestanzahl Meetings",
            sliderWidth,
            lstrRegelsatz.getMinMeetings(),
            lstrRegelsatz.getMaxMeetings(),
            lstrRegelsatz.getSollMeetings());
    binder.bind(
        slider.getSlider(),
        LoesungsstrategieRegelsatz::getSollMeetings,
        LoesungsstrategieRegelsatz::setSollMeetings);

    binder.readBean(lstrRegelsatz);

    SaveButtonComponent saveButton =
        new SaveButtonComponent(lstrRegelsatz, binder, LoesungsstrategieRegelsatzDAO.getInstance());

    VerticalLayout rules = new VerticalLayout(qgRegeln, nzRegeln, slider);
    HorizontalLayout rulesLayout = new HorizontalLayout(rules);
    verticalLayout.add(header, rulesLayout, saveButton);
    verticalLayout.setAlignItems(Alignment.CENTER);
    add(verticalLayout);
  }
}
