package org.arc42.dokumentation.view.evaluation.editRules;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.RoutePrefix;
import org.arc42.analyse.control.RegelwerkControl;
import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.dokumentation.view.components.documentation.main.Arc42DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationAbstractView;

@CssImport("./themes/softwaredocumentation/views/my-styles.css")
@RoutePrefix(value = "arc42/:arcId?/regeln")
public class EditRegelnView extends DocumentationAbstractView {

  protected RegelwerkControl regelwerkControl;

  protected Anchor gewichtung;
  protected Anchor einfuehrung;
  protected Anchor randbedingungen;
  protected Anchor kontextabgrenzung;
  protected Anchor loesungsstrategie;
  protected Anchor bausteinsicht;
  protected Anchor laufzeitsicht;
  protected Anchor verteilungssicht;
  protected Anchor konzepte;
  protected Anchor entwurfsentscheidungen;
  protected Anchor qualitaetsszenarien;
  protected Anchor risiken;
  protected Anchor glossar;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    super.beforeEnter(event);
    if (url == null) {
      if (event.getRouteParameters().get("arcId") == null) {
        event.rerouteTo(Arc42DocumentationView.class);
      } else {
        url = event.getRouteParameters().get("arcId").get();
        this.init();
      }
    }
  }

  @Override
  protected void init() {
    regelwerkControl = new RegelwerkControl();
    VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setSizeUndefined();
    gewichtung = createStyledAnchor("regeln/gewichtung", "Gewichtung der Kategorien");
    gewichtung.getStyle().set("font-weight", "bold");
    einfuehrung = createStyledAnchor("regeln/einfuehrung", TabGlossar.EINFUEHRUNG);
    randbedingungen = createStyledAnchor("regeln/randbedingungen", TabGlossar.RANDBEDINGUNGEN);
    kontextabgrenzung =
        createStyledAnchor("regeln/kontextabgrenzung", TabGlossar.KONTEXTABGRENZUNG);
    loesungsstrategie =
        createStyledAnchor("regeln/loesungsstrategie", TabGlossar.LOESUNGSSTRATEGIE);
    bausteinsicht = createStyledAnchor("regeln/bausteinsicht", TabGlossar.BAUSTEINSICHT);
    laufzeitsicht = createStyledAnchor("regeln/laufzeitsicht", TabGlossar.LAUFZEITSICHT);
    verteilungssicht = createStyledAnchor("regeln/verteilungssicht", TabGlossar.VERTEILUNGSSICHT);
    konzepte = createStyledAnchor("regeln/konzepte", TabGlossar.KONZEPTE);
    entwurfsentscheidungen =
        createStyledAnchor("regeln/entwurfsentscheidungen", TabGlossar.ENTWURFSENTSCHEIDUNGEN);
    qualitaetsszenarien =
        createStyledAnchor("regeln/qualitaetsszenarien", TabGlossar.QUALITAETSSZENARIEN);
    risiken = createStyledAnchor("regeln/risiken", TabGlossar.RISIKEN);
    glossar = createStyledAnchor("regeln/glossar", TabGlossar.GLOSSAR);
    Button ergebnisseButton = new Button("Ergebnisse");
    ergebnisseButton.addClickListener(
        event -> {
          UI.getCurrent().navigate("arc42/" + url + "/bewertung/ergebnisse");
        });
    Button dokuButton = new Button("Zur Doku");
    dokuButton.addClickListener(
        event -> {
          UI.getCurrent().navigate("arc42/" + url + "/titel");
        });
    HorizontalLayout buttons = new HorizontalLayout(ergebnisseButton, dokuButton);
    verticalLayout.add(
        gewichtung,
        einfuehrung,
        randbedingungen,
        kontextabgrenzung,
        loesungsstrategie,
        bausteinsicht,
        laufzeitsicht,
        verteilungssicht,
        konzepte,
        entwurfsentscheidungen,
        qualitaetsszenarien,
        risiken,
        glossar,
        buttons);
    verticalLayout.setSpacing(true);
    verticalLayout.setAlignItems(Alignment.STRETCH);
    verticalLayout.setWidth("350px");
    add(verticalLayout);
  }

  @Override
  public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {}
}
