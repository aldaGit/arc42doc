package org.arc42.dokumentation.view.components.documentation.arc42;

import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.arc42.dokumentation.model.dao.arc42documentation.TextEingabeDAO;
import org.arc42.dokumentation.model.dto.documentation.TextEingabeDTO;
import org.arc42.dokumentation.model.dto.documentation.TextEingabeDTO.TEXTTYPE;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.customComponents.VerticalSpacerGenerator;
import org.arc42.dokumentation.view.components.documentation.HelpField;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.components.documentation.riskComponents.SWOTPieLayout;
import org.arc42.dokumentation.view.components.documentation.riskComponents.staticComponents.ViewUtils;

@Route(value = "risiken/SWOT", layout = MainLayout.class)
public class Swot extends DocumentationView {

  VerticalLayout verticalLayout;
  HorizontalLayout horizontalLayout;
  H3 headline = new H3("SWOT-Analyse      ");

  TextField weaknesses = new TextField("Weaknesses");
  TextField oppportunities = new TextField("Opportunities");
  TextField threats = new TextField("Threats");
  String arcId;
  ApexChartsBuilder pie;

  Button weaknessPlus = new Button(new Icon(VaadinIcon.PLUS_CIRCLE));

  Grid<TextEingabeDTO> weaknessGrid = new Grid<>();
  private MenuBar menuBar = new MenuBar();

  int zaehlerW = 0;
  int zaehlerS = 0;
  int zaehlerO = 0;
  int zaehlerT = 0;

  Dialog dialog;
  int zaehler = 1;
  private List<TextEingabeDTO> currentEingaben;
  private SWOTPieLayout pieLayout;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    arcId = event.getRouteParameters().get("arcId").get();
    super.beforeEnter(event);
  }

  @Override
  public void init() {
    super.init();
    getSwot()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getRisiken().setOpened(true);

    // pie section
    dialog = new Dialog();
    headline.getStyle().set("font-size", "25px");
    pieLayout = new SWOTPieLayout(counterS(), counterW(), counterO(), counterT());
    pieLayout.setVisible(false);
    pieLayout.getStyle().set("width", "520px");
    pieLayout.getStyle().set("height", "380px");
    HorizontalLayout pieLayoutPart = new HorizontalLayout(pieLayout);
    pieLayoutPart.setWidthFull();
    pieLayoutPart.setVerticalComponentAlignment(Alignment.CENTER, pieLayout);
    // end pie section
    verticalLayout = new VerticalLayout();
    verticalLayout.setSizeFull();

    menuBar = ViewUtils.addMenuBar(arcId, 3);
    HorizontalLayout topMenu =
        new HorizontalLayout(
            new BreadCrumbComponent(new String[] {"Risiken", "SWOT-Analyse"}), menuBar);
    topMenu.setWidthFull();
    verticalLayout.add(topMenu);

    horizontalLayout = new HorizontalLayout();
    verticalLayout.setWidthFull();
    Button help = ViewUtils.addHelper(dialog);

    VerticalLayout dialogHelp =
        HelpField.createDialogLayout(dialog, getDescriptionText(), "zur SWOT-Analyse");
    headline.add(help);
    VerticalLayout top = new VerticalLayout();
    top.setPadding(false);
    top.add(headline);
    top.setHorizontalComponentAlignment(Alignment.CENTER, headline);

    horizontalLayout.add(dialog);
    dialog.add(dialogHelp);
    verticalLayout.add(horizontalLayout);
    verticalLayout.setHorizontalComponentAlignment(Alignment.CENTER);

    Button backToVerview = new Button("Zurück zur Übersicht");
    backToVerview.addClickListener(e -> UI.getCurrent().navigate("/arc42/" + arcId + "/risiken"));

    Button kreisdiagramm = new Button("Kreisdiagramm generieren");

    kreisdiagramm.addClickListener(
        e -> {
          pieLayout.setVisible(true);
          this.updateList();
        });

    add(verticalLayout);
    backToVerview.getElement().getStyle().set("margin-left", "auto");
    VerticalSpacerGenerator vsg = new VerticalSpacerGenerator("6vh");
    HorizontalLayout lowerOptions = new HorizontalLayout(kreisdiagramm, backToVerview);
    lowerOptions.setWidthFull();
    lowerOptions.setVerticalComponentAlignment(Alignment.END, backToVerview);
    // build swotcontainer ( horizontal layout with TexteingabeManger (s) (w) (o) (t) )
    HorizontalLayout swotcontainer = new HorizontalLayout();
    swotcontainer.setWidthFull();
    swotcontainer.setPadding(false);
    swotcontainer.setSpacing(false);
    swotcontainer.setMargin(false);
    swotcontainer.add(new TextEingabeManager(url, arcId, TEXTTYPE.STRENGTH));
    swotcontainer.add(new TextEingabeManager(url, arcId, TEXTTYPE.WEAKNESS));
    swotcontainer.add(new TextEingabeManager(url, arcId, TEXTTYPE.OPPORTUNITY));
    swotcontainer.add(new TextEingabeManager(url, arcId, TEXTTYPE.THREAT));

    verticalLayout.add(top, swotcontainer, lowerOptions, vsg.buildVerticalSpacer(), pieLayoutPart);
  }

  public void updateList() {
    List<TextEingabeDTO> eingaben = TextEingabeDAO.getInstance().findAll(url);
    if (eingaben == null) {
      return;
    }
    this.currentEingaben = eingaben;
    List<TextEingabeDTO> strengthEingaben = new ArrayList<>();
    List<TextEingabeDTO> weaknessEingaben = new ArrayList<>();
    List<TextEingabeDTO> opportunityEingaben = new ArrayList<>();
    List<TextEingabeDTO> threatEingaben = new ArrayList<>();
    for (TextEingabeDTO tdto : eingaben) {
      switch (tdto.getType()) {
        case STRENGTH:
          strengthEingaben.add(tdto);
          break;
        case WEAKNESS:
          weaknessEingaben.add(tdto);
          break;
        case OPPORTUNITY:
          opportunityEingaben.add(tdto);
          break;
        case THREAT:
          threatEingaben.add(tdto);
          break;
        default:
          break;
      }
    }
    this.weaknessGrid.setItems(weaknessEingaben);

    this.zaehlerS = strengthEingaben.size();
    this.zaehlerO = opportunityEingaben.size();
    this.zaehlerW = weaknessEingaben.size();
    this.zaehlerT = threatEingaben.size();
    if (pieLayout != null) {
      this.pieLayout.update(counterS(), counterW(), counterO(), counterT());
    }
    System.out.println(Arrays.toString(eingaben.toArray()));
  }

  private String getDescriptionText() {
    return "Die SWOT-Analyse ermöglicht es Informationen zu den Strengths (Stärken), Weaknesses"
        + " (Schwächen), Opportunities (Möglichkeiten) und Threats (Bedrohungen) eines"
        + " Projekts oder Software festzuhalten. Durch die Einteilung in die vier Rubriken"
        + " ist ersichtlich, wo das Projekt noch Verbesserungen benötigt und in welchen"
        + " Aspekten es bereits gut aufgestellt ist. Es gibt keine Vorgabe dazu, wie viele"
        + " Einträge pro Rubrik eingetragen werden müssen. Sie können zur Übersicht die"
        + " eingetragenen Daten als Kreisdiagramm generieren lassen, um das Verhältnis der"
        + " Rubriken zu erkennen. ";
  }

  public int counterW() {
    return zaehlerW;
  }

  public int counterS() {
    return zaehlerS;
  }

  public int counterO() {
    return zaehlerO;
  }

  public int counterT() {
    return zaehlerT;
  }
}