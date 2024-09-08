package org.arc42.dokumentation.view.util;

import java.util.ArrayList;
import java.util.List;

public class BadgeGlossary {

  public static final List<String> QUALITYBADGES =
      new ArrayList<>(
          List.of(
              "Funktionalität",
              "Effizienz",
              "Kompatibilität",
              "Benutzbarkeit",
              "Zuverlässigkeit",
              "Sicherheit",
              "Wartbarkeit",
              "Portabilität",
              "Nachhaltigkeit"));

  public static final List<String> CONCEPTBADGES =
      new ArrayList<>(
          List.of(
              "General",
              "Domain",
              "UX",
              "Security",
              "Architecture",
              "UnderTheHood",
              "Development",
              "Operational"));

  public static final List<String> COLORS =
      new ArrayList<>(
          List.of(
              "coral",
              "mediumseagreen",
              "darkkhaki",
              "darkslategray",
              "cadetblue",
              "slategray",
              "mediumslateblue",
              "mediumvioletred",
              "green"));
}
