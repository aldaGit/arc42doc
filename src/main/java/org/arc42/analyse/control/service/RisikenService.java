package org.arc42.analyse.control.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.arc42.dokumentation.model.dao.arc42documentation.TextEingabeDAO;
import org.arc42.dokumentation.model.dto.documentation.TextEingabeDTO;
import org.arc42.dokumentation.view.components.documentation.riskComponents.SWOTPieLayout;

public class RisikenService {

  public static Integer[] getSwotStats(String url) {

    List<TextEingabeDTO> allSwot = TextEingabeDAO.getInstance().findAll(url);

    if (allSwot == null) {
      return new Integer[] {0, 0, 0, 0};
    }

    Integer strengthEingaben = 0;
    Integer weaknessEingaben = 0;
    Integer opportunityEingaben = 0;
    Integer threatEingaben = 0;
    for (TextEingabeDTO tdto : allSwot) {
      switch (tdto.getType()) {
        case STRENGTH:
          strengthEingaben++;
          break;
        case WEAKNESS:
          weaknessEingaben++;
          break;
        case OPPORTUNITY:
          opportunityEingaben++;
          break;
        case THREAT:
          threatEingaben++;
          break;
      }
    }
    // S W O T
    return new Integer[] {strengthEingaben, weaknessEingaben, opportunityEingaben, threatEingaben};
  }

  public void execute(HashMap<String, String> analyse, int arcId, String targetName) {

    SWOTPieLayout pieLayout = new SWOTPieLayout(RisikenService.getSwotStats("" + arcId));
    String risikoAnalyse = pieLayout.getHtml();
    System.out.println("RISIKOANALYSE \n\n\n\n\n\n" + risikoAnalyse);
    analyse.put(targetName, risikoAnalyse);
  }

  public List<TextEingabeDTO> getRisiken(String url) {
    List<TextEingabeDTO> all = TextEingabeDAO.getInstance().findAll(url);
    if (all == null) {
      return new ArrayList<>();
    }
    return all;
  }
}
