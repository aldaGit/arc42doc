package org.arc42.analyse.control;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Paragraph;
import org.arc42.analyse.control.service.Arc42AnalyseService;

public class SustainabilityRatingService {
  private final Arc42AnalyseService service;
  private static SustainabilityRatingService instance;

  public enum Rating {
    A(1, "A"),
    B(2, "B"),
    C(3, "C");
    public final int rating;
    public final String label;

    Rating(int rating, String label) {
      this.rating = rating;
      this.label = label;
    }
  }

  private SustainabilityRatingService() {
    super();
    service = Arc42AnalyseService.getInstance();
  }

  public static SustainabilityRatingService getInstance() {
    if (instance != null) {
      return instance;
    }
    instance = new SustainabilityRatingService();
    return instance;
  }

  public Rating getMeetingRating(int url) {
    int countPraesenz = service.countTotalMeetingsByType("Präsenz", url);
    int countHybrid = service.countTotalMeetingsByType("Hybrid", url);
    int countRemote = service.countTotalMeetingsByType("Remote", url);
    int totalCount = countHybrid + countPraesenz + countRemote;
    // C = mehr als 20 % in Präsenz
    // B = mehr als 5 % in Präsenz
    // A = sonst
    return 5 * countPraesenz > totalCount
        ? Rating.C
        : 20 * countPraesenz > totalCount ? Rating.B : Rating.A;
  }

  public Rating getEmissionsRating(int url) {
    // g CO₂ pro kWh, Durchschnitt 2018-2022 von
    // https://de.statista.com/statistik/daten/studie/38897/umfrage/co2-emissionsfaktor-fuer-den-strommix-in-deutschland-seit-1990/
    double emissionsPerkWh = 419;
    double emissions =
        service.getTotalPowerConsumptionOfHardware(url) * emissionsPerkWh * 365 / 1000
            + service.getTotalEmissionsOfInterfaces(url) * 12 / 1000;
    // t CO₂ pro Kopf pro Jahr in Deutschland,
    // https://de.statista.com/statistik/daten/studie/1272890/umfrage/pro-kopf-treibhausgasemissionen-in-deutschland/
    double emissionsPerYearPerCapita = 8300;
    return emissions < 0.1 * emissionsPerYearPerCapita
        ? Rating.A
        : emissions < 0.5 * emissionsPerYearPerCapita ? Rating.B : Rating.C;
  }

  public Rating getRenewableRating(int url) {
    double renewablePortion = service.getRenewablePortion(url);
    return renewablePortion > 80 ? Rating.A : renewablePortion > 60 ? Rating.B : Rating.C;
  }

  public Rating getFinishedRating(int url) {
    int countNachhaltigkeitsziele = service.countNachhaltigkeitsziele(url);
    int countOekologisch = service.countOekologischRandbedingung(url);
    int countLoesungsStrategie = service.countLoesungsStrategieToNachhaltigkeitsziele(url);
    if (countNachhaltigkeitsziele == 5 && countOekologisch == 5 && countLoesungsStrategie == 5) {
      return Rating.A;
    } else if (countNachhaltigkeitsziele > 2
        && countOekologisch > 2
        && countLoesungsStrategie > 1) {
      return Rating.B;
    } else {
      return Rating.C;
    }
  }

  public Rating getTotalRating(int url) {
    int ratingCount =
        getEmissionsRating(url).rating
            + getFinishedRating(url).rating
            + getMeetingRating(url).rating
            + getRenewableRating(url).rating;
    return ratingCount < 6 ? Rating.A : ratingCount < 10 ? Rating.B : Rating.C;
  }

  public Component getMeetingRatingDescription() {
    return new Paragraph(
        "A: höchstens 5% der Meetings finden in Präsenz statt; "
            + "B: zwischen 5% und 20% der Meetings finden in Präsenz statt; "
            + "C: sonst");
  }

  public Component getEmissionsRatingDescription() {
    return new Paragraph(
        "Unter 10% des Grenzwertes ergibt ein Rating von A, unter 50% des Grenzwertes"
            + " ergibt ein Rating von B, ansonsten C.");
  }

  public Component getFinishedRatingDescription() {
    return new Paragraph(
        "Sind mindestens fünf Nachhaltigkeitsziele und fünf ökologische Rahmenbedingungen vorhanden"
            + " und gibt es zu jedem Nachhaltigkeitsziel eine Lösungsstrategie ist das Rating A."
            + " Sind mindestens jeweils 3 dokumentiert und zu 2 Nachhaltigkeitszielen"
            + " Lösungsstrategien vorhanden ist das Rating B. Ansonsten ist das Rating C.");
  }

  public Component getRenewableRatingDescription() {
    return new Paragraph(
        "Wird über 80% des Stroms für die Hardware-Komponenten aus erneuerbaren Energien "
            + "bezogen, ergibt sich ein Rating von A, ab 60% von B und unter 60% von C.");
  }
}
