package org.arc42.analyse.control;

import java.util.HashMap;
import org.arc42.analyse.control.service.*;
import org.arc42.analyse.control.service.SWADAnalyseService;

public class ARC42AnalyseResultService {

  public void starteAnalyse(int arcId) {

    System.out.println("+++++++ Starte An  alyse für Softwarearchitektur mit Id: " + arcId);

    HashMap<String, String> analyse = new HashMap<>();
    SWADAnalyseService swadAnalyseService = new SWADAnalyseService();
    CheckVollstaendigkeitService checkVollstaendigkeitService = new CheckVollstaendigkeitService();
    ReadSecurityAdvisoryService readSecurityAdvisoryService = new ReadSecurityAdvisoryService();
    CheckSecurityAdvisoryService checkSecurityAdvisoryService = new CheckSecurityAdvisoryService();
    CheckDesignDecisionService checkDesignDecisionService = new CheckDesignDecisionService();
    CheckQualityService checkQualityService = new CheckQualityService();
    RisikenService risikenService = new RisikenService();

    swadAnalyseService.execute(analyse, arcId);
    checkVollstaendigkeitService.execute(analyse, arcId);
    readSecurityAdvisoryService.execute(arcId);
    checkSecurityAdvisoryService.execute(analyse, arcId);
    checkDesignDecisionService.execute(analyse, arcId);
    checkQualityService.execute(analyse, arcId);
    risikenService.execute(analyse, arcId, "ARC_STEP_5");

    String init = (analyse.get("ARC_INIT") != null) ? analyse.get("ARC_INIT") : "";
    String step1 = (analyse.get("ARC_STEP_1") != null) ? analyse.get("ARC_STEP_1") : "";
    String step2 = (analyse.get("ARC_STEP_2") != null) ? analyse.get("ARC_STEP_2") : "";
    String step3 = (analyse.get("ARC_STEP_3") != null) ? analyse.get("ARC_STEP_3") : "";
    String step4 = (analyse.get("ARC_STEP_4") != null) ? analyse.get("ARC_STEP_4") : "";

    String step5 = analyse.getOrDefault("ARC_STEP_5", "");

    String analyseResult = init + step1 + step2 + step3 + step4 + step5;
    ResultFormatterService resultFormatterService = new ResultFormatterService(arcId);
    String output = resultFormatterService.saveAnalyseResult(analyseResult);
    System.out.println("############### Analyse Result  ################## " + arcId + output);
  }
}
