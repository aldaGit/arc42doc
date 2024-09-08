package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextAreaVariant;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import java.util.List;
import org.arc42.analyse.control.service.RisikenService;
import org.arc42.dokumentation.model.dao.arc42documentation.RisikoDAO;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.customComponents.OpenableDetailsViewComponent;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.components.documentation.riskComponents.SWOTPieLayout;
import org.arc42.dokumentation.view.util.data.RiskMethods;

@Route(value = "risiken", layout = MainLayout.class)
public class Risiken extends DocumentationView {

  VerticalLayout mainVLayout;
  String arcId;
  private MenuBar menuBar = new MenuBar();
  private Text selected = new Text("");
  private RiskMethods riskMethods = new RiskMethods();
  RisikoDAO dao = new RisikoDAO();
  RisikoDTO risikoDTO = new RisikoDTO();
  H3 titel = new H3("11. Risiken");

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    arcId = event.getRouteParameters().get("arcId").get();
    super.beforeEnter(event);
  }

  @Override
  public void init() {
    super.init();
    getRisiken()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getRisiken().setOpened(true);

    titel.getStyle().set("font-size", "25px");

    mainVLayout = new VerticalLayout();
    mainVLayout.setSizeFull();

    HorizontalLayout horizontalTop = new HorizontalLayout();

    MenuBar rm = menuBar();
    horizontalTop.setWidthFull();
    horizontalTop.add(new BreadCrumbComponent(new String[] {"Overview"}), rm);
    horizontalTop.setAlignItems(Alignment.CENTER);

    TextArea textarea = new TextArea();
    textarea.setWidthFull();
    textarea.addThemeVariants(TextAreaVariant.LUMO_ALIGN_CENTER);
    textarea.getStyle().set("--vaadin-input-field-border-width", "1px");
    textarea.getStyle().set("--vaadin-input-field-border-color", "#244EF9");
    textarea.setValue(
        "Dieser Tab dient zur Dokumentation und Analyse von den Risiken Ihrer Software-Architektur."
            + " Anhand von vier Methoden können Sie Ihr Risikomanagement hier dokumentieren."
            + " Bevor Sie die Methoden bearbeiten, müssen Sie verpflichtend zuerst die Methode"
            + " \"Checkliste\" bearbeiten. ");
    textarea.setReadOnly(true);
    /*---------------------------------------------------- */

    Grid<RisikoDTO> checklistenTab = new Grid<>(RisikoDTO.class, false);

    checklistenTab.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    checklistenTab.addColumn(RisikoDTO::getId).setHeader("Risiko-ID");
    checklistenTab.addColumn(RisikoDTO::getAnforderung).setHeader("Risikobeschreibung");
    checklistenTab.addColumn(RisikoDTO::getSchadenshoehe).setHeader("Schadenshöhe");
    checklistenTab
        .addColumn(RisikoDTO::getEintrittswahrscheinlichkeit)
        .setHeader("Eintrittswahrscheinlichkeit");
    checklistenTab.addColumn(RisikoDTO::getStatus).setHeader("Status");
    checklistenTab.getStyle().set("height", "350px");
    checklistenTab.getStyle().set("width", "480px");

    Grid<RisikoDTO> bogenGrid = new Grid<>(RisikoDTO.class, false);
    bogenGrid.addColumn(RisikoDTO::getId).setHeader("Risiko-ID");
    bogenGrid.addColumn(RisikoDTO::getAnforderung).setHeader("Risikobeschreibung");
    bogenGrid.addColumn(RisikoDTO::getErfasser).setHeader("Erfasser");
    bogenGrid.getStyle().set("height", "350px");
    bogenGrid.getStyle().set("width", "480px");
    // bogenGrid.setVisible(false);
    bogenGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

    List<RisikoDTO> eingetrageneDaten = dao.findAllByArcId(url);
    eingetrageneDaten.removeIf(element -> !Bogen.isValidErfasser(element.getErfasser()));

    checklistenTab.setItems(eingetrageneDaten);
    bogenGrid.setItems(eingetrageneDaten);

    CheckboxGroup<String> checkboxGroup = checkBoxMenu();
    HorizontalLayout checkBoxesLayout = new HorizontalLayout(checkboxGroup);

    VerticalLayout topBoxCheck = new VerticalLayout(checkBoxesLayout);
    topBoxCheck.setAlignItems(Alignment.CENTER);
    topBoxCheck.add(checkboxGroup);
    topBoxCheck.setPadding(false);
    topBoxCheck.setHorizontalComponentAlignment(Alignment.CENTER, checkboxGroup);

    topBoxCheck.getStyle().set("border", "1px solid black");
    topBoxCheck.getStyle().set("border-radius", "20px");
    topBoxCheck.getStyle().set("background-color", "#F0F8FF");

    HorizontalLayout swatRiskHorizontal = new HorizontalLayout();
    swatRiskHorizontal.setWidthFull();

    Integer[] stats = RisikenService.getSwotStats(arcId);
    SWOTPieLayout pieLayout = new SWOTPieLayout(stats);
    pieLayout.getStyle().set("width", "478px");
    pieLayout.getStyle().set("height", "267px");
    // pieLayout.setVisible(false);

    HorizontalLayout lowerRightHeader = new HorizontalLayout();
    H4 risikoMapH4 = new H4("Risiko-Map");

    Button bearbeitenMap = new Button("Bearbeiten");

    lowerRightHeader.add(risikoMapH4, bearbeitenMap);
    lowerRightHeader.setVerticalComponentAlignment(Alignment.END, bearbeitenMap);

    OpenableDetailsViewComponent lowerRight =
        new OpenableDetailsViewComponent(
            "Risiko-Map", arcId + "/risiken/RisikoMap", new RisikoMapView().getRiskMap());

    // lowerRight.setVerticalComponentAlignment(Alignment.END, riskMapLayout);
    HorizontalLayout checklisteBogenHorizontal = new HorizontalLayout();
    checklisteBogenHorizontal.setWidthFull();
    OpenableDetailsViewComponent upperLeft =
        new OpenableDetailsViewComponent(
            "Checkliste", arcId + "/risiken/checkliste", checklistenTab);
    OpenableDetailsViewComponent upperRight =
        new OpenableDetailsViewComponent("Fragebogen", arcId + "/risiken/bogen", bogenGrid);
    checklisteBogenHorizontal.add(upperLeft, upperRight);
    OpenableDetailsViewComponent lowerLeft =
        new OpenableDetailsViewComponent("SWOT-Analyse", arcId + "/risiken/SWOT", pieLayout);

    swatRiskHorizontal.add(lowerLeft, lowerRight);

    // riskMethods.add("Checkliste", checkListeIte);
    riskMethods.add("Fragebogen", upperRight);
    riskMethods.add("SWOT-Analyse", lowerLeft);
    riskMethods.add("Risiko-Map", lowerRight);
    mainVLayout.add(
        horizontalTop, titel, topBoxCheck, textarea, checklisteBogenHorizontal, swatRiskHorizontal);
    mainVLayout.setHorizontalComponentAlignment(Alignment.CENTER, titel);

    add(mainVLayout);
  }

  ComponentEventListener<ClickEvent<MenuItem>> setTextMenuListener =
      e -> selected.setText(e.getSource().getText());

  public MenuBar menuBar() {

    ComponentEventListener<ClickEvent<MenuItem>> menuListener =
        e -> selected.setText(e.getSource().getText());
    MenuItem checkListeIte = menuBar.addItem("Checkliste", menuListener);
    checkListeIte.addClickListener(
        e -> {
          UI.getCurrent().navigate("/arc42/" + arcId + "/risiken/checkliste");
        });
    MenuItem bogenItem = menuBar.addItem("Fragebogen", menuListener);
    bogenItem.addClickListener(e -> UI.getCurrent().navigate("/arc42/" + arcId + "/risiken/bogen"));
    MenuItem sWOItem = menuBar.addItem("SWOT-Analyse", menuListener);
    sWOItem.addClickListener(e -> UI.getCurrent().navigate("/arc42/" + arcId + "/risiken/SWOT"));
    MenuItem risikoMapItem = menuBar.addItem("Risiko-Map", menuListener);
    risikoMapItem.addClickListener(
        e -> UI.getCurrent().navigate("/arc42/" + arcId + "/risiken/RisikoMap"));

    menuBar.addThemeVariants(MenuBarVariant.LUMO_CONTRAST);

    return menuBar;
  }

  private CheckboxGroup<String> checkBoxMenu() {

    CheckboxGroup<String> checkboxGroup = new CheckboxGroup<>();
    checkboxGroup.setLabel("Wählen Sie die gewünschten Methoden aus.");
    checkboxGroup.setItems("Fragebogen", "SWOT-Analyse", "Risiko-Map");

    checkboxGroup.addSelectionListener(
        s -> {
          s.getAllSelectedItems().forEach(l -> riskMethods.select(l));
          s.getRemovedSelection().forEach(l -> riskMethods.deselect(l));
        });
    return checkboxGroup;
  }
}