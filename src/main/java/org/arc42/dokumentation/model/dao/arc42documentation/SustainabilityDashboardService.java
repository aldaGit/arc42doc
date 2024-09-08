package org.arc42.dokumentation.model.dao.arc42documentation;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.Annotations;
import com.github.appreciated.apexcharts.config.annotations.builder.AnnotationLabelBuilder;
import com.github.appreciated.apexcharts.config.annotations.builder.YAxisAnnotationsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.DataLabelsBuilder;
import com.github.appreciated.apexcharts.config.builder.PlotOptionsBuilder;
import com.github.appreciated.apexcharts.config.builder.XAxisBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.plotoptions.builder.BarBuilder;
import com.github.appreciated.apexcharts.config.plotoptions.builder.RadialBarBuilder;
import com.github.appreciated.apexcharts.config.plotoptions.radialbar.RadialBarDataLabels;
import com.github.appreciated.apexcharts.config.plotoptions.radialbar.builder.TotalBuilder;
import com.github.appreciated.apexcharts.config.series.SeriesType;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.arc42.analyse.control.SustainabilityRatingService;
import org.arc42.analyse.control.service.Arc42AnalyseService;
import org.arc42.dokumentation.model.dto.documentation.MeetingDTO;
import org.arc42.dokumentation.model.dto.documentation.NachhaltigkeitszieleDTO;

public class SustainabilityDashboardService {
  private Arc42AnalyseService service;

  private NachhaltigkeitszieleDAO greenGoalsDao;
  private static SustainabilityDashboardService instance;
  int url;
  private final double emissionsPerkWh = 419; // g CO₂ pro kWh, Durchschnitt 2018-2022 von
  // https://de.statista.com/statistik/daten/studie/38897/umfrage/co2-emissionsfaktor-fuer-den-strommix-in-deutschland-seit-1990/

  private final double emissionsPerYearPerCapita = 8300; // t CO₂ pro Kopf pro Jahr in Deutschland,

  // https://de.statista.com/statistik/daten/studie/1272890/umfrage/pro-kopf-treibhausgasemissionen-in-deutschland/

  private SustainabilityDashboardService(String url) {
    super();
    this.url = Integer.parseInt(url);
  }

  public static SustainabilityDashboardService getInstance(String url) {
    if (instance == null) {
      instance = new SustainabilityDashboardService(url);
    }
    return instance;
  }

  public VerticalLayout createSustainabilityDashboard() {
    // Set-Up
    this.service = Arc42AnalyseService.getInstance();
    this.greenGoalsDao = NachhaltigkeitszieleDAO.getInstance();
    SustainabilityRatingService ratingService = SustainabilityRatingService.getInstance();

    H2 sustainabilityBoardHeader = new H2("Sustainability-Board");
    HorizontalLayout headerRow = new HorizontalLayout(sustainabilityBoardHeader);
    headerRow.setPadding(true);

    VerticalLayout sustainabilityBoard = new VerticalLayout();
    sustainabilityBoard.setClassName("dashboard");

    ApexCharts emissionsChart = createEmissionsChart();
    H5 emissionsHeader = new H5("Jährliche CO₂-Emissionen durch Hardware und Schnittstellen in kg");
    Accordion emissionsHelper = new Accordion();
    Paragraph emissionsHelperText =
        new Paragraph(
            emissionsPerYearPerCapita / 1000
                + "t CO₂ sind die jährlichen Pro-Kopf-Emissionen in Deutschland (2020).\n"
                + "Beim Stromverbrauch der Hardware-Komponenten wird der tägliche Betrieb, an 365"
                + " Tagen im Jahr, angenommen. Außerdem wird mit den durchschnittlichen Emissionen"
                + " von "
                + emissionsPerkWh
                + "kg CO₂/kWh gerechnet.");
    emissionsHelper.add("Erklärung", emissionsHelperText);
    emissionsHelper.close();
    VerticalLayout emissionsChartLayout =
        new VerticalLayout(emissionsHeader, emissionsChart, emissionsHelper);
    emissionsChartLayout.getStyle().set("background-color", "#ffffff");

    ApexCharts meetingsPie = createMeetingsChart();
    H5 meetingsHeader = new H5("Meetings pro Jahr nach Art des Meetings");
    VerticalLayout meetingsPieLayout = new VerticalLayout(meetingsHeader, meetingsPie);
    meetingsPieLayout.getStyle().set("background-color", "#ffffff");

    ApexCharts finishedRadial = createFinishedChart();
    H5 finishedHeader = new H5("Vollständigkeit der Dokumentation");
    Accordion finishedHelper = new Accordion();
    Paragraph finishedHelperText =
        new Paragraph(
            "Zielwerte: 5 Nachhaltigkeitsziele, 5 ökologische"
                + "Randbedingungen und je Nachhaltigkeitsziel eine Lösungsstrategie");
    finishedHelper.add("Erklärung", finishedHelperText);
    finishedHelper.close();
    VerticalLayout radialBarLayout =
        new VerticalLayout(finishedHeader, finishedRadial, finishedHelper);
    radialBarLayout.getStyle().set("background-color", "#ffffff");

    ApexCharts renewableChart = createRenewableChart();
    H5 renewableHeader = new H5("Anteil Hardware-Komponenten mit erneuerbaren Energien");
    VerticalLayout renewableLayout = new VerticalLayout(renewableHeader, renewableChart);
    renewableLayout.getStyle().set("background-color", "#ffffff");

    H5 energyHeader = new H5("Anzahl von Energy Smells");
    VerticalLayout energyLayout = new VerticalLayout(energyHeader);
    try {
      HashMap<String, Integer> smellDistribution =
          Arc42AnalyseService.getInstance().getSmellDistribution();
      int smellCount = smellDistribution.values().stream().mapToInt(Integer::intValue).sum();
      ApexCharts energySmellChart = createEnergyChart(smellDistribution);
      energyLayout.add(energySmellChart);
      NachhaltigkeitszieleDTO codeGoal = greenGoalsDao.findByGoal("Keine Energy Smells");
      if (codeGoal != null) {
        Paragraph codeGoalResult =
            new Paragraph(
                "Im Code wurden "
                    + smellCount
                    + " Energy Smells entdeckt. Somit wurde das Ziel mit der ID "
                    + codeGoal.getId()
                    + " ");
        Span result = new Span(smellCount == 0 ? " erreicht." : " nicht erreicht.");
        result.getStyle().set("color", smellCount == 0 ? "green" : "red");
        result.getStyle().set("font-weight", "bold");
        codeGoalResult.getElement().appendChild((result.getElement()));
        energyLayout.add(codeGoalResult);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    energyLayout.add();

    HorizontalLayout chartsFirstRow = new HorizontalLayout(radialBarLayout, meetingsPieLayout);
    chartsFirstRow.setSizeFull();
    chartsFirstRow.setPadding(true);

    HorizontalLayout chartsSecondRow = new HorizontalLayout(emissionsChartLayout, renewableLayout);
    chartsSecondRow.setPadding(true);
    chartsSecondRow.setSizeFull();

    H3 ratingHeader = new H3("Rating");
    HorizontalLayout ratingLayout = new HorizontalLayout();
    Accordion ratings = new Accordion();
    ratings.add(
        "Vollständigkeit: " + ratingService.getFinishedRating(url).label,
        ratingService.getFinishedRatingDescription());
    ratings.add(
        "Meetings: " + ratingService.getMeetingRating(url).label,
        ratingService.getMeetingRatingDescription());
    ratings.add(
        "CO₂-Emissionen: " + ratingService.getEmissionsRating(url).label,
        ratingService.getEmissionsRatingDescription());
    ratings.add(
        "Nutzung Erneuerbarer: " + ratingService.getRenewableRating(url).label,
        ratingService.getRenewableRatingDescription());
    ratings.setSizeFull();
    ratings.close();
    SustainabilityRatingService.Rating totalRating = ratingService.getTotalRating(url);
    Label ratingLabel = new Label(totalRating.label);
    ratingLabel.getStyle().set("color", "white");
    ratingLabel.getStyle().set("font-weight", "bolder");
    ratingLabel.getStyle().set("font-size", "50px");
    ratingLabel.setHeightFull();

    VerticalLayout rating1 = new VerticalLayout(ratingLabel);
    if (totalRating.equals(SustainabilityRatingService.Rating.A)) {
      rating1.getStyle().set("background-color", "green");
    } else if (totalRating.equals(SustainabilityRatingService.Rating.B)) {
      rating1.getStyle().set("background-color", "yellow");
    } else {
      rating1.getStyle().set("background-color", "red");
    }
    rating1.setAlignItems(FlexComponent.Alignment.CENTER);
    ratingLayout.add(rating1);
    ratingLayout.add(ratings);
    VerticalLayout rating = new VerticalLayout(ratingHeader, ratingLayout);
    rating.getStyle().set("background-color", "#ffffff");
    rating.setSizeFull();
    HorizontalLayout dashboardThirdRow = new HorizontalLayout(rating, energyLayout);
    dashboardThirdRow.setPadding(true);
    dashboardThirdRow.setSizeFull();

    sustainabilityBoard.add(headerRow, chartsFirstRow, chartsSecondRow, dashboardThirdRow);
    sustainabilityBoard.getStyle().set("background-color", "#dfe3e8");
    sustainabilityBoard.setSpacing(false);
    sustainabilityBoard.setPadding(false);
    sustainabilityBoard.setMargin(false);
    return sustainabilityBoard;
  }

  private ApexCharts createRenewableChart() {
    return ApexChartsBuilder.get()
        .withChart(ChartBuilder.get().withType(Type.RADIALBAR).withForeColor("green").build())
        .withSeries(round(service.getRenewablePortion(url)))
        .withColors("green")
        .withLabels("Anteil")
        .build();
  }

  private ApexCharts createFinishedChart() {
    RadialBarDataLabels labels = new RadialBarDataLabels();
    labels.setTotal(TotalBuilder.get().withShow(true).withLabel("Gesamt").build());
    return ApexChartsBuilder.get()
        .withChart(ChartBuilder.get().withType(Type.RADIALBAR).build())
        .withSeries(
            (double) service.countNachhaltigkeitsziele(url) * 20,
            (double) service.countOekologischRandbedingung(url) * 20,
            ((double) service.countLoesungsStrategieToNachhaltigkeitsziele(url))
                / (double) service.countNachhaltigkeitsziele(url)
                * 100)
        .withLabels(
            "Nachhaltigkeitsziele",
            "Ökologische Randbedingungen",
            "Lösungsstrategien zu Nachhaltigkeitszielen")
        .withPlotOptions(
            PlotOptionsBuilder.get()
                .withRadialBar(
                    RadialBarBuilder.get().withEndAngle(270.0).withDataLabels(labels).build())
                .build())
        .build();
  }

  private ApexCharts createEmissionsChart() {
    Series<Double> hardware =
        new Series<>(
            round(service.getTotalPowerConsumptionOfHardware(url) * emissionsPerkWh * 365 / 1000));
    Series<Double> interfaces =
        new Series<>(round(service.getTotalEmissionsOfInterfaces(url) * 12 / 1000));
    hardware.setName("Hardware");
    hardware.setType(SeriesType.COLUMN);
    interfaces.setName("Schnittstellen");
    interfaces.setType(SeriesType.COLUMN);
    Series<Double> marker = new Series<>(emissionsPerYearPerCapita);
    marker.setName(
        emissionsPerYearPerCapita / 1000
            + "t CO₂ = jährliche Pro-Kopf-Emissionen in Deutschland (2020)");
    marker.setType(SeriesType.LINE);
    Annotations annotations = new Annotations();
    annotations.setYaxis(
        List.of(
            YAxisAnnotationsBuilder.get()
                .withY(emissionsPerYearPerCapita)
                .withBorderColor("#feb019")
                .withStrokeDashArray(0.0)
                .withLabel(
                    AnnotationLabelBuilder.get()
                        .withText(emissionsPerYearPerCapita / 1000 + "t CO₂")
                        .withBorderColor("#feb019")
                        .build())
                .build()));
    return ApexChartsBuilder.get()
        .withChart(ChartBuilder.get().withType(Type.BAR).withStacked(true).build())
        .withSeries(hardware, interfaces, marker)
        .withLabels("Emissionen")
        .withAnnotations(annotations)
        .build();
  }

  private ApexCharts createMeetingsChart() {
    return new ApexChartsBuilder()
        .withChart(ChartBuilder.get().withType(Type.PIE).build())
        .withLabels(
            MeetingDTO.MeetingType.REMOTE.label,
            MeetingDTO.MeetingType.HYBRID.label,
            MeetingDTO.MeetingType.PRAESENZ.label)
        .withSeries(
            Double.valueOf(
                service.countTotalMeetingsByType(MeetingDTO.MeetingType.REMOTE.label, url)),
            Double.valueOf(
                service.countTotalMeetingsByType(MeetingDTO.MeetingType.HYBRID.label, url)),
            Double.valueOf(
                service.countTotalMeetingsByType(MeetingDTO.MeetingType.PRAESENZ.label, url)))
        .build();
  }

  private ApexCharts createEnergyChart(HashMap<String, Integer> smellDistribution) {
    return ApexChartsBuilder.get()
        .withChart(ChartBuilder.get().withType(Type.BAR).build())
        .withPlotOptions(
            PlotOptionsBuilder.get().withBar(BarBuilder.get().withHorizontal(true).build()).build())
        .withDataLabels(DataLabelsBuilder.get().withEnabled(false).build())
        .withSeries(new Series<>(smellDistribution.values().toArray(new Integer[0])))
        .withXaxis(
            XAxisBuilder.get().withCategories(smellDistribution.keySet().stream().toList()).build())
        .build();
  }

  private double round(double d) {
    return Math.round(d * 10.0) / 10.0;
  }
}
