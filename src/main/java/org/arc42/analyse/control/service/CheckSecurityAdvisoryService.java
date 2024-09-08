package org.arc42.analyse.control.service;

import java.util.Map;
import org.arc42.analyse.control.SecurityAdvisoryAnalyse;

public class CheckSecurityAdvisoryService {

  public void execute(Map<String, String> delegate, int arcId) {

    SecurityAdvisoryAnalyse analyse = new SecurityAdvisoryAnalyse(arcId);
    String header =
        "<h4><span style=\"color: #0000ff;\"><em><strong>Ergebnis des"
            + " Sicherheitslücken-Checks</strong></em></span></h4>\n";
    String result = analyse.startAnalyse();
    if (result.trim().equalsIgnoreCase("")) {
      result =
          header
              + "<p><span style=\"background-color: #00ff00;\"><strong>&nbsp; &nbsp; Es wurde keine"
              + " Bibliothek oder keine Designentscheidung gefunden, die potentielle"
              + " Sicherheitslücken enthalten können.</strong></span></p>";
    } else {
      result = header + result;
    }
    delegate.put("ARC_STEP_4", result + "\n <p>&nbsp;</p> \n ");

    System.out.println("############### CheckSecurityAdvisoryDelegae  ################## " + arcId);
  }
}
