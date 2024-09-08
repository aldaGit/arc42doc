package org.arc42.dokumentation.view.evaluation;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.Route;
import java.util.List;
import org.arc42.analyse.control.RegelwerkControl;
import org.arc42.analyse.model.evaluation.EvaluationResultI;
import org.arc42.analyse.model.evaluation.Regelwerk;
import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.dokumentation.view.components.documentation.HelpField;
import org.arc42.dokumentation.view.components.documentation.main.Arc42DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationAbstractView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@CssImport("./themes/softwaredocumentation/views/my-styles.css")
@Route(value = "arc42/:arcId?/bewertung/ergebnisse", layout = MainLayout.class)
public class EvaluationResults extends DocumentationAbstractView {

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
    RegelwerkControl regelwerkControl = new RegelwerkControl();
    VerticalLayout verticalLayout = new VerticalLayout();
    HorizontalLayout horizontalLayoutHeader = new HorizontalLayout();
    horizontalLayoutHeader.setWidthFull();
    H3 header = new H3("Ergebnisse der Bewertung");
    Button regelButton = new Button("Regeln bearbeiten");
    regelButton.addClickListener(
        event -> {
          UI.getCurrent().navigate("arc42/" + url + "/regeln/gewichtung");
        });
    Button dokuButton = new Button("Zur Dokumentation");
    dokuButton.addClickListener(
        event -> {
          UI.getCurrent().navigate("arc42/" + url + "/titel");
        });
    horizontalLayoutHeader.add(header, regelButton, dokuButton);
    horizontalLayoutHeader.setVerticalComponentAlignment(Alignment.CENTER, header);
    horizontalLayoutHeader.setVerticalComponentAlignment(Alignment.END, dokuButton);

    List<EvaluationResultI> tabResults = regelwerkControl.getEvaluationResults(url);
    Grid<EvaluationResultI> results = new Grid<>(EvaluationResultI.class, false);
    results.addColumn(EvaluationResultI::getTab).setHeader("Tab");
    results.addColumn(EvaluationResultI::getResultAsString).setHeader("Ergebnis");
    results
        .addColumn(
            result -> {
              if (result.getTab().equals(TabGlossar.GESAMT)) {
                return "";
              }
              Regelwerk regelwerk = regelwerkControl.getRegelwerk(url);
              return regelwerk.getParticularRegelsatz(result.getTab()).getGewichtung() + " %";
            })
        .setHeader("Gewichtung");
    results
        .addComponentColumn(result -> createStatusIcon(result.getResultAsString()))
        .setHeader("Status");
    results.setItems(tabResults);
    results.setClassNameGenerator(
        result -> {
          if (result.equals(tabResults.get(tabResults.size() - 1))) {
            if (result.getResultAsString().equals("100 %")) {
              return "highlighted-row-green";
            } else {
              return "highlighted-row-orange";
            }
          }
          return null;
        });
    results.setAllRowsVisible(true);

    Dialog dialog = new Dialog();
    VerticalLayout dialogHelp =
        HelpField.createDialogLayout(
            dialog,
            """
            Die Bewertung einer Kategorie erfolgt stets prozentual.\s
            Das heißt, es wird dem Nutzer angegeben wie viel Prozent
            der Anforderungen, die im Regelwerk eingestellt sind,
            durch die erbrachte Dokumentation erreicht wurden.
            Die Gesamtbewertung setzt sich aus den erreichten
            Werten in den Kategorien unter Berücksichtigung
            der jeweiligen Gewichtung zusammen.""",
            "zur Ergebnisberechnung");
    dialog.add(dialogHelp);

    HorizontalLayout horizontalLayoutInfo = new HorizontalLayout();
    Icon infoIcon = new Icon(VaadinIcon.INFO_CIRCLE);
    Button infoButton = new Button(infoIcon);
    infoButton.addClickListener(e -> dialog.open());
    Span infotext = new Span("Informationen zur Berechnung der Ergebnisse");
    horizontalLayoutInfo.add(infoButton, infotext);

    Button sustainabilityButton = new Button("Zur Nachhaltigkeitsbewertung");
    sustainabilityButton.addClickListener(
        event -> {
          UI.getCurrent().navigate("arc42/" + url + "/bewertung/nachhaltigkeit");
        });

    verticalLayout.add(horizontalLayoutHeader, results, horizontalLayoutInfo, sustainabilityButton);
    add(verticalLayout);
  }

  private Icon createStatusIcon(String result) {
    Icon icon;
    if (result.equals("100 %")) {
      icon = VaadinIcon.CHECK.create();
      icon.getElement().getThemeList().add("badge success");
      icon.getStyle().setColor("green");
    } else {
      icon = VaadinIcon.CLOSE_SMALL.create();
      icon.getElement().getThemeList().add("badge error");
      icon.getStyle().setColor("red");
    }
    icon.getStyle().set("padding", "var(--lumo-space-xs");
    return icon;
  }

  @Override
  public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {}
}
