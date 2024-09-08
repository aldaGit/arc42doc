package org.arc42.dokumentation.view.components.documentation.arc42;

import com.github.appreciated.apexcharts.config.TitleSubtitle;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;
import org.arc42.analyse.control.service.RisikenService;
import org.arc42.dokumentation.model.dao.arc42documentation.RisikoDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.SustainabilityDashboardService;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO.Schadenshoehe;
import org.arc42.dokumentation.view.components.customComponents.VerticalSpacerGenerator;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.components.documentation.riskComponents.PrioritaetenChart;
import org.arc42.dokumentation.view.components.documentation.riskComponents.RisikoMapLayout;
import org.arc42.dokumentation.view.components.documentation.riskComponents.SWOTPieLayout;
import org.arc42.dokumentation.view.components.documentation.riskComponents.StatusBarChart;

@Route(value = "zusammenfassung", layout = MainLayout.class)
public class AnalyseResult extends DocumentationView {

  private String arcId;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    this.arcId = event.getRouteParameters().get("arcId").get();
    super.beforeEnter(event);
  }

  @Override
  public void init() {
    super.init();
    getSummaryPage()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getSummaryPage().setOpened(true);

    // titles
    H5 einfuehrungH5 = new H5("1. Einführung und Ziele");
    H5 randbedingungH5 = new H5("2. Randsbedingungen");
    H5 kontextabgrenzungH5 = new H5("3. Kontextabgrenzung");
    H5 loesungsstrategieH5 = new H5("4. Lösungsstrategie");
    H5 bausteinsichtH5 = new H5("5. Bausteinsicht");
    H5 laufzeitsichtH5 = new H5("6. Laufzeitsicht");
    H5 verteilungssichtH5 = new H5("7. Verteilungssicht");
    H5 konzepteH5 = new H5("8. Konzepte");
    H5 entwurfsentscheidung = new H5("9. Entwurfsentscheidungen");
    H5 qualitatszeileH5 = new H5("10. Qualitätsszenarien");
    H5 risikenH5 = new H5("11. Risiken");
    H5 glossarH5 = new H5("12. Glossar");
    VerticalSpacerGenerator vsg = new VerticalSpacerGenerator("5em");

    SWOTPieLayout pieLayout = setUpPieLayout();
    RisikoMapLayout riskMapLayout = setUpRiskMapLayout();
    PrioritaetenChart prioritaetenChart = setUpPrioChartLayout();
    StatusBarChart statusBarChart = setUpStatusBarChart();
    Grid<RisikoDTO> anspreGrid = setUpGrid();
    H6 mapTitle = new H6("Risiko-Map");
    // disable padding and margin on maptitle
    mapTitle.getStyle().set("margin", "0px");
    mapTitle.getStyle().set("padding", "0px");
    H6 prioTitelH6 = new H6("Anteile der Prioritäten der Fragebögen");
    H6 statusH6 = new H6("Status der Risiken");
    H6 swotTitle = new H6("SWOT-Analyse");
    H6 ansprechGridH6 = new H6("Ansprechpartner");

    VerticalLayout firstRowLeft = new VerticalLayout(swotTitle, pieLayout);
    VerticalLayout firstRowRight = new VerticalLayout(prioTitelH6, prioritaetenChart);
    HorizontalLayout firstRow = new HorizontalLayout(firstRowLeft, firstRowRight);

    VerticalLayout firstRowTitlesAddendum = new VerticalLayout(mapTitle, riskMapLayout);
    // firstRowTitlesAddendum.setWidthFull();

    VerticalLayout secondRowLeft = new VerticalLayout(statusH6, statusBarChart);
    VerticalLayout secondRowRight = new VerticalLayout(ansprechGridH6, anspreGrid);
    HorizontalLayout secondRow = new HorizontalLayout(secondRowLeft, secondRowRight);
    secondRow.setVerticalComponentAlignment(Alignment.START, secondRowLeft, secondRowRight);

    // all full width

    firstRowLeft.setWidthFull();
    firstRowLeft.setAlignItems(Alignment.CENTER);
    firstRowRight.setWidthFull();
    firstRowRight.setAlignItems(Alignment.CENTER);
    firstRow.setWidth("80%");
    firstRow.setAlignItems(Alignment.CENTER);
    firstRowTitlesAddendum.setWidth("80%");
    firstRowTitlesAddendum.setSpacing(false);
    firstRowTitlesAddendum.setPadding(false);
    firstRowTitlesAddendum.setMargin(false);
    firstRowTitlesAddendum.setHorizontalComponentAlignment(
        Alignment.CENTER, mapTitle, riskMapLayout);

    secondRow.setAlignItems(Alignment.CENTER);
    secondRowLeft.setWidthFull();
    secondRowLeft.setAlignItems(Alignment.CENTER);
    // secondRowLeft.setSpacing(false);
    secondRowLeft.setPadding(false);
    secondRowLeft.setMargin(false);
    secondRow.setPadding(false);
    secondRowRight.setWidthFull();
    secondRowRight.setAlignItems(Alignment.CENTER);
    // secondRowRight.setSpacing(false);
    secondRowRight.setPadding(false);
    secondRowRight.setMargin(false);
    secondRow.setWidth("80%");

    // firstRowTitles.setWidthFull();
    // firstRowTitles.setVerticalComponentAlignment(Alignment.CENTER, swotTitle);
    // firstRowTitles.setVerticalComponentAlignment(Alignment.CENTER, mapTitle);
    // secondRow.setAlignItems(Alignment.STRETCH);
    // secondRowTitles.setWidthFull();
    // secondRowTitles.setVerticalComponentAlignment(Alignment.START, statusH6);
    // secondRowTitles.setVerticalComponentAlignment(Alignment.END, ansprechGridH6);
    // swotTitle.setWidth("380px");
    // mapTitle.setWidth("380px");
    // prioTitelH6.setWidth("380px");
    VerticalLayout pagVerticalLayout =
        new VerticalLayout(
            einfuehrungH5,
            randbedingungH5,
            kontextabgrenzungH5,
            loesungsstrategieH5,
            bausteinsichtH5,
            laufzeitsichtH5,
            verteilungssichtH5,
            kontextabgrenzungH5,
            konzepteH5,
            entwurfsentscheidung,
            qualitatszeileH5,
            risikenH5,
            firstRow,
            vsg.buildVerticalSpacer(),
            firstRowTitlesAddendum,
            vsg.buildVerticalSpacer(),
            secondRow,
            vsg.buildVerticalSpacer(),
            glossarH5);
    pagVerticalLayout.setAlignItems(Alignment.CENTER);
    // macht alles komisch nach oben
    // pagVerticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
    pagVerticalLayout.setSpacing(false);
    pagVerticalLayout.setPadding(false);
    // pagVerticalLayout.setMargin(false);
    pagVerticalLayout.setWidthFull();
    add(pagVerticalLayout);

    // FIONA:
    SustainabilityDashboardService sustainabilityDashboardService =
        SustainabilityDashboardService.getInstance(url);
    VerticalLayout sustainabilityDashboard =
        sustainabilityDashboardService.createSustainabilityDashboard();
    add(sustainabilityDashboard);
  }

  public SWOTPieLayout setUpPieLayout() {
    SWOTPieLayout pieLayout = new SWOTPieLayout(RisikenService.getSwotStats(arcId));
    // pieLayout.getStyle().set("width", "300px");
    // pieLayout.getStyle().set("height", "250px");
    return pieLayout;
  }

  public RisikoMapLayout setUpRiskMapLayout() {
    RisikoMapLayout riskMapLayout = new RisikoMapLayout(false);

    riskMapLayout.clearSeries();

    for (String s : RisikoDAO.getInstance().getAllIds(url)) {

      if (riskMapLayout != null) {
        String eintrittsw =
            RisikoDAO.getInstance().findById(s).getEintrittswahrscheinlichkeit().getLabel();

        Schadenshoehe schadenshoehe = RisikoDAO.getInstance().findById(s).getSchadenshoehe();

        riskMapLayout.update(
            s,
            RisikoMapLayout.convertEintrittswToInt(eintrittsw),
            RisikoMapLayout.convertSchadenshoeheToInt(schadenshoehe));
      }
      riskMapLayout.setVisible(true);
    }

    riskMapLayout.renderIfDataChanged();

    TitleSubtitle title = new TitleSubtitle();
    title.setText("Risiko-Map");
    riskMapLayout.getApexChartsBuilder().build();
    return riskMapLayout;
  }

  public PrioritaetenChart setUpPrioChartLayout() {

    Integer[] prioritaetenNumbers = getPrioCounts();
    PrioritaetenChart prioritaetenChart = new PrioritaetenChart(prioritaetenNumbers);

    TitleSubtitle title = new TitleSubtitle();
    title.setText("Risiko-Map");
    return prioritaetenChart;
  }

  public StatusBarChart setUpStatusBarChart() {

    Integer[] statusNumbers = getStatusCount();
    StatusBarChart statusChart = new StatusBarChart(statusNumbers);
    statusChart.setPadding(false);
    statusChart.setMargin(false);
    return statusChart;
  }

  public Grid<RisikoDTO> setUpGrid() {

    Grid<RisikoDTO> grid = new Grid<>();

    grid.addColumn(RisikoDTO::getAnsprechpartner).setHeader("Ansprechpartner").setFrozen(true);
    grid.addColumn(RisikoDTO::getId).setHeader("Risiko-ID").setFrozen(true);
    grid.addColumn(RisikoDTO::getAnforderung).setHeader("Risikobeschreibung").setFrozen(true);
    grid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
    grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

    grid.setAllRowsVisible(true);

    List<RisikoDTO> risiken = RisikoDAO.getInstance().findAllByArcId(arcId);
    List<RisikoDTO> risikenMitAnsprechpartner = new ArrayList<>();
    for (RisikoDTO risikoDTO : risiken) {
      if (isValidAnsprechpartner(risikoDTO.getAnsprechpartner())
          && isValidErfasser(risikoDTO.getErfasser())) {
        risikenMitAnsprechpartner.add(risikoDTO);
      }
    }
    grid.setItems(risikenMitAnsprechpartner);

    return grid;
  }

  private boolean isValidAnsprechpartner(String ansprechpartner) {
    return ansprechpartner != null
        && !ansprechpartner.equals("")
        && !ansprechpartner.equals("null")
        && !ansprechpartner.equals("\"\""); // ""
  }

  private boolean isValidErfasser(String erfasser) {
    return erfasser != null
        && !erfasser.equals("")
        && !erfasser.equals("null")
        && !erfasser.equals("\"\""); // ""
  }

  private Integer[] getPrioCounts() {
    List<RisikoDTO> prios = RisikoDAO.getInstance().findAll(url);
    int countKeine = 0;
    int countNiedrig = 0;
    int countMittel = 0;
    int countHoch = 0;
    int countSehrHoch = 0;
    int countNichtErfasst = 0;
    for (RisikoDTO dto : prios) {
      switch (dto.getPrioSkala()) {
        case KEINE:
          countKeine++;
          break;
        case NIEDRIG:
          countNiedrig++;
          break;
        case MITTEL:
          countMittel++;
          break;
        case HOCH:
          countHoch++;
          break;
        case SEHRHOCH:
          countSehrHoch++;
          break;
        case NICHTERFASST:
          countNichtErfasst++;
          break;
        default:
          break;
      }
    }
    return new Integer[] {
      countKeine, countNiedrig, countMittel, countHoch, countSehrHoch, countNichtErfasst
    };
  }

  private Integer[] getStatusCount() {
    List<RisikoDTO> prios = RisikoDAO.getInstance().findAll(this.arcId);
    int countInBearbeitung = 0;
    int countUnbearbeitet = 0;
    int countErledigt = 0;
    for (RisikoDTO dto : prios) {
      switch (dto.getStatus()) {
        case INBEARBEITUNG:
          countInBearbeitung++;
          break;
        case UNBEARBEITET:
          countUnbearbeitet++;
          break;
        case ERLEDIGT:
          countErledigt++;
          break;
        default:
          break;
      }
    }
    System.out.println(
        "Found statuscounts: " + countInBearbeitung + countUnbearbeitet + countErledigt);
    return new Integer[] {countErledigt, countInBearbeitung, countUnbearbeitet};
  }

  public int getMaximumOfPrioCounts() {
    Integer[] prioCounts = getPrioCounts();
    int max = 0;
    for (Integer integer : prioCounts) {
      if (integer > max) {
        max = integer;
      }
    }
    return max;
  }
}
