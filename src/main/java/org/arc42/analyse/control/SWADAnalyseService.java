package org.arc42.analyse.control;

import java.util.Date;
import org.arc42.analyse.control.service.Arc42AnalyseService;
import org.arc42.analyse.model.util.DateFormater;

public class SWADAnalyseService {

  private final Integer arcId;
  private final Arc42AnalyseService service;
  private String output;

  public SWADAnalyseService(Integer arcId) {
    this.arcId = arcId;
    this.output =
        "<h2><span style=\"text-decoration: underline;\"><em><strong>Ergebnis der Analyse der"
            + " Softwarearchitektur: ######### </strong></em></span></h2>\n"
            + "<p><em>Datum der Analyse: ----------</em></p>\n"
            + "<p>&nbsp;</p>";
    service = Arc42AnalyseService.getInstance();
  }

  public void initAnalyse() {
    String title = this.service.getTitleByArcId(arcId);
    String analyseDate = DateFormater.format(new Date());
    this.output = this.output.replaceFirst("#########", title);
    this.output = this.output.replaceFirst("----------", analyseDate);
    System.out.println(" ------------ " + this.output);
  }

  public String getOutput() {
    return output;
  }
}
