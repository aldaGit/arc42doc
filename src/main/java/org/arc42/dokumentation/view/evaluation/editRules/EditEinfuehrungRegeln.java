package org.arc42.dokumentation.view.evaluation.editRules;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.Route;
import org.arc42.analyse.model.evaluation.dao.EinfuehrungRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.EinfuehrungRegelsatz;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.BadgeGlossary;
import org.arc42.dokumentation.view.util.SelectBadgeComponent;

@Route(value = "einfuehrung", layout = MainLayout.class)
public class EditEinfuehrungRegeln extends EditRegelnView {

  protected void init() {
    super.init();
    einfuehrung.getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    einfuehrung.getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    VerticalLayout verticalLayout = new VerticalLayout();
    H3 header = new H3("Regeln für Einführung und Ziele");

    EinfuehrungRegelsatz ezRegelsatz = regelwerkControl.getRegelwerk(url).getEinfuehrungRegelsatz();
    Binder<EinfuehrungRegelsatz> binder = new Binder<>(EinfuehrungRegelsatz.class);

    String sliderWidth = "250px";
    String integerFieldWidth = "300px";

    IntegerField maxTL = new IntegerField("Maximale Zeichenanzahl im Titel");
    maxTL.setWidth(integerFieldWidth);
    maxTL.setMin(1);
    binder.bind(
        maxTL, EinfuehrungRegelsatz::getMaxTitelLength, EinfuehrungRegelsatz::setMaxTitelLength);

    SliderComponent sliderMinA =
        new SliderComponent(
            "Mindestanzahl Aufgabenstellungen",
            sliderWidth,
            ezRegelsatz.getMinMinAufgaben(),
            ezRegelsatz.getMaxMinAufgaben(),
            ezRegelsatz.getMinAufgaben());
    binder.bind(
        sliderMinA.getSlider(),
        EinfuehrungRegelsatz::getMinAufgaben,
        EinfuehrungRegelsatz::setMinAufgaben);

    SliderComponent sliderMinQZ =
        new SliderComponent(
            "Mindestanzahl Qualitätsziele",
            sliderWidth,
            ezRegelsatz.getMinMinZiele(),
            ezRegelsatz.getMaxMinZiele(),
            ezRegelsatz.getMinZiele());
    binder.bind(
        sliderMinQZ.getSlider(),
        EinfuehrungRegelsatz::getMinZiele,
        EinfuehrungRegelsatz::setMinZiele);

    String textAreaWidth = "400px";

    TextArea qzRegeln = new TextArea("Regel zu Qualitätszielen");
    qzRegeln.setValue("Zu einem Qualitätsziel soll stets eine Motivation dokumentiert werden.");
    qzRegeln.setReadOnly(true);
    qzRegeln.setWidth(textAreaWidth);

    TextArea qzExplanation = new TextArea("Regel zu Kategorien von Qualitätszielen");
    qzExplanation.setValue(
        "Es können Kategorien ausgewählt werden, zu denen Qualitätsziele dokumentiert werden"
            + " sollen.");
    qzExplanation.setReadOnly(true);
    qzExplanation.setWidth(textAreaWidth);

    SelectBadgeComponent selectBadgeComponent =
        new SelectBadgeComponent(
            ezRegelsatz.getNeededQualityCriteria(), BadgeGlossary.QUALITYBADGES);

    SliderComponent sliderMinSH =
        new SliderComponent(
            "Mindestanzahl Stakeholder",
            sliderWidth,
            ezRegelsatz.getMinMinStakeholder(),
            ezRegelsatz.getMaxMinStakeholder(),
            ezRegelsatz.getMinStakeholder());
    binder.bind(
        sliderMinSH.getSlider(),
        EinfuehrungRegelsatz::getMinStakeholder,
        EinfuehrungRegelsatz::setMinStakeholder);

    SliderComponent sliderMinNZ =
        new SliderComponent(
            "Mindestanzahl Nachhaltigkeitsziele",
            sliderWidth,
            ezRegelsatz.getMinMinNachhaltigkeitsziele(),
            ezRegelsatz.getMaxMinNachhaltigkeitsziele(),
            ezRegelsatz.getMinNachhaltigkeitsziele());
    binder.bind(
        sliderMinNZ.getSlider(),
        EinfuehrungRegelsatz::getMinNachhaltigkeitsziele,
        EinfuehrungRegelsatz::setMinNachhaltigkeitsziele);

    TextArea nzRegeln = new TextArea("Regeln zu Nachhaltigkeitszielen");
    nzRegeln.setValue(
        "Zu einem Nachhaltigkeitsziel soll stets eine Motivation,"
            + " Priorität und Einsparung dokumentiert werden.");
    nzRegeln.setReadOnly(true);
    nzRegeln.setWidth(textAreaWidth);

    binder.readBean(ezRegelsatz);

    Button saveButton =
        new Button(
            "Speichern",
            event -> {
              try {
                binder.writeBean(ezRegelsatz);
                ezRegelsatz.setNeededQualityCriteria(selectBadgeComponent.getSelectedBatches());
                EinfuehrungRegelsatzDAO.getInstance().update(ezRegelsatz);
                Notification.show("Regeln gespeichert");
              } catch (ValidationException e) {
                Notification.show("Regeln konnten nicht gespeichert werden");
              }
            });

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    VerticalLayout verticalLayoutLeft = new VerticalLayout();
    VerticalLayout verticalLayoutRight = new VerticalLayout();
    verticalLayoutLeft.add(maxTL, sliderMinA, sliderMinSH, sliderMinNZ, nzRegeln);
    verticalLayoutRight.add(sliderMinQZ, qzRegeln, qzExplanation, selectBadgeComponent);
    horizontalLayout.add(verticalLayoutLeft, verticalLayoutRight);
    verticalLayout.add(header, horizontalLayout, saveButton);
    verticalLayout.setAlignItems(Alignment.CENTER);
    add(verticalLayout);
  }
}
