package org.arc42.analyse.control.service;

import java.util.List;
import java.util.Map;
import org.arc42.analyse.control.DesignDecisionAnalyse;
import org.arc42.analyse.model.dto.Arc42DocumentationDTO;

public class CheckDesignDecisionService {

  public void execute(Map<String, String> delegate, int arcId) {
    DesignDecisionAnalyse analyse = new DesignDecisionAnalyse(arcId);
    analyse.startAnalyse();
    List<Arc42DocumentationDTO> dtos = analyse.getDtos();

    String ddcheck =
        "<h4><span style=\"color: #0000ff;\"><em><strong>Ergebnis des"
            + " Entwurfsentscheidung-Checks</strong></em></span></h4>\n";

    if (dtos != null) {
      StringBuilder output = new StringBuilder("<ol>\n");
      int i = 0;
      for (Arc42DocumentationDTO dto : dtos) {
        i = i + 1;
        String licontent = "<li>Designentscheidung:\n";

        licontent +=
            "<p><span style=\"text-decoration: underline;\"><em><strong>Choice:</strong>"
                + " </em></span></p>\n"
                + "<p>"
                + dto.getDecisionTitle()
                + "</p>\n";

        String artifact = dto.getAffectedArtifact();
        System.out.println(
            dto
                + " 000000000000 "
                + dto.checkRationale()
                + " ++++++++++++++ "
                + dto.acceptedAlternativChange());
        licontent +=
            "<span style=\"text-decoration: underline;\"><em><strong>Affected"
                + " Architectural-Element:</strong></em></span>\n"
                + "<p>"
                + artifact
                + "</p>\n";

        if (dto.acceptedAlternativChange()) {
          licontent +=
              "<p><em><span style=\"text-decoration: underline;\"><strong>Result of the"
                  + " Check:</strong></span></em></p>\n"
                  + "<p style=\"color: #ff0000;\"> Die Entwurfsentscheidung unterscheidet sich der"
                  + " in der Referenz ausgewählten Entwurfsentscheidung</p>\n";

          licontent +=
              "<p><span style=\"text-decoration: underline;\"><em><strong>Quelle der"
                  + " Entscheidung:</strong></em></span></p>\n"
                  + "<p style=\"color: #ff0000;\">"
                  + dto.getRationaleReferenz()
                  + "</p>\n";
        } else {
          licontent +=
              "<p><em><span style=\"text-decoration: underline;\"><strong>Result of the"
                  + " Check:</strong></span></em></p>\n"
                  + "  <p style=\"color: #00ff00;\"> Die Entwurfsentscheidung ist soweit"
                  + " OKAY</p>\n";

          licontent +=
              "<p><span style=\"text-decoration: underline;\"><em><strong>Quelle der"
                  + " Entscheidung:</strong></em></span></p>\n"
                  + "<p style=\"color: #00ff00;\">"
                  + dto.getRationaleReferenz()
                  + "</p>\n";
        }
        if (!dto.checkRationale()) {
          licontent +=
              "<p><span style=\"text-decoration: underline;\"><em><strong>Result of the"
                  + " Rational-Check:</strong></em></span></p>\n"
                  + "  <p style=\"color: #ff0000;\">Diese Entwurfsentscheidung wurde nicht"
                  + " begründet oder nicht ausführlich begründet (Rationale ist nicht OKAY).</p>\n";
        } else {
          licontent +=
              "<p><span style=\"text-decoration: underline;\"><em><strong>Result of the"
                  + " Rational-Check:</strong></em></span></p>\n"
                  + "  <p style=\"color: #00ff00;\">Diese Entwurfsentscheidung wurde ausführlich"
                  + " begründet (Rationale ist OKAY).</p>\n";
        }
        licontent += "</li>\n";
        output.append(licontent);
      }
      output.append("</ol>");
      ddcheck += output;
    } else {
      ddcheck +=
          "<p><strong><span style=\"color: #ff0000;\">&nbsp; &nbsp;- Es wurde keine Bausteinsicht"
              + " (Klassendiagramm) oder keine Entwurfsentscheidungen in der Bausteinsicht"
              + " dokumentiert</span></strong></p> \n";
    }
    delegate.put("ARC_STEP_3", ddcheck + "\n <p>&nbsp;</p> \n ");

    System.out.println("############### CheckDesignDecisionDelegate  ################## " + arcId);
  }
}
