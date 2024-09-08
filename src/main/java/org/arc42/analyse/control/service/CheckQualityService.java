package org.arc42.analyse.control.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.stream.Collectors;
import org.arc42.analyse.control.QualityAnalyse;

public class CheckQualityService {

  public void execute(Map<String, String> delegate, int arcId) {
    QualityAnalyse analyse = new QualityAnalyse(arcId);
    Map<String, Double> weightMap = analyse.getWeightMapCategory();
    String categorie =
        analyse.getNonExistingCategories().stream()
            .map(categorie1 -> "<ul><li>" + categorie1 + "</li></ul>")
            .collect(Collectors.joining());

    String nonExistingQualityGoal =
        analyse.getNonExisitingRelation().stream()
            .map(qualityScenarioDTO -> "<ul><li>" + qualityScenarioDTO + "</li></ul>")
            .collect(Collectors.joining());

    String kategorien =
        analyse.getWeightMapCategory().entrySet().stream()
            .map(
                entry ->
                    "<tr><td style=\"width: 50%;\">"
                        + entry.getKey()
                        + "</td><td style=\"width: 50%;\">"
                        + BigDecimal.valueOf(
                                entry.getValue() * 100 / analyse.getTotalWeight(weightMap))
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue()
                        + "</td></tr>")
            .collect(Collectors.joining());

    String qcheck =
        "<h4><span style=\"color: #0000ff;\"><em><strong>Ergebnis des"
            + " Qualitäts-Checks</strong></em></span></h4><p>Folgende Qualitätskriterien"
            + " gemä&szlig; ISO 25010 Norm für Qualitätskriterien von Software, IT-Systemen und"
            + " Software-Engineering wurden <span style=\"text-decoration:"
            + " underline;\">nicht</span> in den Qualitätszielen oder Qualitätskriterien"
            + " berücksichtigt:</p><p>"
            + categorie
            + "<p>Folgende Qualitätsziele wurden nicht durch Qualitätsszenarien konkretisiert:</p>"
            + nonExistingQualityGoal
            + "<p>Hier ist eine Übersicht mit den Qualitätsszenarien die ausreichend dokumentiert"
            + " wurden, veranschaulicht in %:</p><table"
            + " border=\"1\"><tbody><tr><td><p><strong>Kategorie</strong></p></td><td><strong>Abgedeckt"
            + " zu: (%)</strong></td></tr>"
            + kategorien
            + "</tbody></table>";

    Map<String, Double> weigthMap = analyse.startAnalyse();
    String result =
        weigthMap.entrySet().stream()
            .map(
                entry ->
                    "<td style=\"width: 50%;\">"
                        + entry.getKey()
                        + "</td><td style=\"width: 50%;\">"
                        + entry.getValue()
                        + "</td>")
            .collect(Collectors.joining("</tr>"));
    System.out.println(result);

    System.out.println("############### CheckDesignDecisionDelegate  ################## " + arcId);
    delegate.put("ARC_STEP_2", qcheck);
  }
}
