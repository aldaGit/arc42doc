package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import org.arc42.dokumentation.model.dao.arc42documentation.RisikoDAO;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO.Schadenshoehe;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.documentation.HelpField;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.components.documentation.riskComponents.RisikoMapLayout;
import org.arc42.dokumentation.view.components.documentation.riskComponents.staticComponents.ViewUtils;
import org.arc42.dokumentation.view.util.data.RiskMethods;

@Route(value = "risiken/RisikoMap", layout = MainLayout.class)
public class RisikoMapView extends DocumentationView {
  RisikoMapLayout riskMapLayout;
  VerticalLayout pageVerticalLayout;
  HorizontalLayout horizontalLayout;
  private Text selected = new Text("");
  private MenuBar menuBar = new MenuBar();
  private RiskMethods riskMethods = new RiskMethods();

  Dialog dialog;
  Button help;
  String arcId;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    arcId = event.getRouteParameters().get("arcId").get();
    super.beforeEnter(event);
  }

  @Override
  public void init() {
    super.init();
    getRisikoMap()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getRisiken().setOpened(true);

    H3 headline = new H3("Risiko-Map  ");
    headline.getStyle().set("font-size", "25px");
    Icon helpIcon = new Icon(VaadinIcon.INFO_CIRCLE);

    dialog = new Dialog();
    VerticalLayout dialogHelp =
        HelpField.createDialogLayout(
            dialog,
            "Wählen Sie hier die Risiko-IDs aus, welche Sie im Graphen darstellen wollen. In dem"
                + " Diagramm erkennen Sie die Einordnung des Risikos zu den Aspekten der"
                + " Schadenshöhe und Eintrittswahrscheinlichkeit. ",
            "zur Risiko-Map");
    dialog.add(dialogHelp);

    help = new Button(helpIcon);
    help.setHeight("30px");
    help.setWidth("30px");
    help.addClickListener(e -> dialog.open());
    headline.add(help);

    pageVerticalLayout = new VerticalLayout();
    menuBar = ViewUtils.addMenuBar(arcId, 4);
    HorizontalLayout topMenu =
        new HorizontalLayout(
            new BreadCrumbComponent(new String[] {"Risiken", "Risiko-Map"}), menuBar);
    topMenu.setWidthFull();
    pageVerticalLayout.add(topMenu);

    CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
    checkboxGroup.setLabel("Wählen sie die Risiken aus, welche Sie im Graphen darstellen wollen.");
    checkboxGroup.setItems(RisikoDAO.getInstance().getAllIds(url));
    Button display = new Button("IDs darstellen");
    HorizontalLayout selectRisks = new HorizontalLayout();
    selectRisks.setWidthFull();
    selectRisks.add(checkboxGroup);
    selectRisks.setVerticalComponentAlignment(Alignment.CENTER, checkboxGroup);
    checkboxGroup.getStyle().set("border", "1px solid black");
    checkboxGroup.getStyle().set("border-radius", "20px");
    checkboxGroup.getStyle().set("background-color", "#F0F8FF");
    checkboxGroup.getStyle().set("padding", "var(--lumo-space-m)");
    HorizontalLayout buttonToDisplay = new HorizontalLayout();
    buttonToDisplay.add(display);
    buttonToDisplay.setVerticalComponentAlignment(Alignment.CENTER, buttonToDisplay);

    riskMapLayout = new RisikoMapLayout(true);
    riskMapLayout.setWidth("800px");
    riskMapLayout.setPadding(true);
    riskMapLayout.getStyle().set("padding-left", "60px");
    // riskMapLayout.setPadding(true);
    display.addClickListener(
        event -> {
          System.out.println("clicked:");
          riskMapLayout.clearSeries();

          for (String s : checkboxGroup.getSelectedItems()) {
            System.out.println("s: " + s);
            if (riskMapLayout != null) {
              String eintrittsw =
                  RisikoDAO.getInstance().findById(s).getEintrittswahrscheinlichkeit().getLabel();
              System.out.println("Eintrittswahrscheinlichkeit= " + eintrittsw);
              Schadenshoehe schadenshoehe = RisikoDAO.getInstance().findById(s).getSchadenshoehe();
              System.out.println("Schadenshoehe= " + schadenshoehe);
              this.riskMapLayout.update(
                  s,
                  riskMapLayout.convertEintrittswToInt(eintrittsw),
                  riskMapLayout.convertSchadenshoeheToInt(schadenshoehe));

              System.out.println(
                  "riskmapvalues "
                      + riskMapLayout.convertEintrittswToInt(eintrittsw)
                      + " sh "
                      + riskMapLayout.convertSchadenshoeheToInt(schadenshoehe));
            }
            riskMapLayout.setVisible(true);
          }

          riskMapLayout.renderIfDataChanged();
        });

    pageVerticalLayout.setSizeFull();
    pageVerticalLayout.add(headline, dialog, checkboxGroup, buttonToDisplay);
    pageVerticalLayout.add(riskMapLayout);
    pageVerticalLayout.setHorizontalComponentAlignment(Alignment.END, help);
    pageVerticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, headline);
    pageVerticalLayout.setHorizontalComponentAlignment(Alignment.CENTER, riskMapLayout);
    pageVerticalLayout.setWidthFull();

    /* if (riskMapLayout != null) {
      this.riskMapLayout.update(1, 2);
      riskMapLayout.setVisible(true);
      pageVerticalLayout.add(riskMapLayout);
    }*/
    this.add(pageVerticalLayout);
  }

  public RisikoMapLayout getRiskMap() {
    if (this.riskMapLayout == null) {
      return new RisikoMapLayout(true);
    }
    return this.riskMapLayout;
  }
}