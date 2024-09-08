package org.arc42.analyse.control;

import java.util.List;
import org.arc42.analyse.control.service.Arc42AnalyseService;
import org.arc42.analyse.model.dto.DesignDecisionDTOForSA;
import org.arc42.analyse.model.dto.GitHubSecurityAdvisoryDTO;

public class SecurityAdvisoryAnalyse {
  private final Integer arcId;
  private final List<GitHubSecurityAdvisoryDTO> securityAdvisories;
  private final Arc42AnalyseService service;

  public SecurityAdvisoryAnalyse(Integer arcId) {
    this.arcId = arcId;
    this.service = Arc42AnalyseService.getInstance();
    this.securityAdvisories = service.findAllGitHubSecurityAdvisory();
  }

  public String startAnalyse() {
    StringBuilder output = new StringBuilder();
    int count = this.service.countDesignDecision(this.arcId);
    if (count >= 1) {
      output.append("<ol>\n");
      List<DesignDecisionDTOForSA> decisions = this.service.findDesignDecisionForArcId(this.arcId);
      for (DesignDecisionDTOForSA decision : decisions) {
        for (GitHubSecurityAdvisoryDTO securityAdvisory : securityAdvisories) {
          if (securityAdvisory
              .getTitle()
              .trim()
              .toLowerCase()
              .contains(decision.getTitle().trim().toLowerCase())) {
            System.out.println("Hinweis vielleicht Sicherheitslücke");
            String li =
                "<li>"
                    + decision.getTitle()
                    + " :\n"
                    + "<div >\n"
                    + "  <p style=\"background-color: #ffff00;\">\n"
                    + "    <em>hat möglicherweise Sicherheitslücken. Sie sollten die untenstehenden"
                    + " Sicherheitslücke-Beschreibung gut lesen und bewerten, ob Ihre Software"
                    + " gefährdet ist.</em></p>\n"
                    + "<p><em>betroffene Softwareartefakt(e) : "
                    + this.service.getAffectedArchitekturElement(decision.getId())
                    + "</em></p>\n"
                    + "</div>\n"
                    + "<p><em><strong>Sicherheitslücke-Beschreibung:</strong></em></p>\n"
                    + "<p>veröffentlicht am : "
                    + securityAdvisory.getPublished()
                    + "</p>\n"
                    + "<div style=\"color: #ff0000;\">"
                    + securityAdvisory.getContent()
                    + "</div>\n"
                    + "</li>";
            output.append(li);
          }
        }
      }
      output.append("</ol>");
    } else {
      output.append(
          "<p><strong><span style=\"color: #3BD700;\">&nbsp; &nbsp;- Es wurden keine"
              + " Sicherheitslücken gefunden!</span></strong></p> </ol>");
    }
    return output.toString();
  }
}
