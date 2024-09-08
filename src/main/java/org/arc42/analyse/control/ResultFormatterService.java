package org.arc42.analyse.control;

import org.arc42.analyse.control.service.Arc42AnalyseService;

public class ResultFormatterService {

  private final Integer arcId;
  private final Arc42AnalyseService service;
  private String output;

  public ResultFormatterService(Integer arcId) {
    this.arcId = arcId;
    service = Arc42AnalyseService.getInstance();
  }

  private void formatString(String output) {
    this.output = output;
  }

  public String saveAnalyseResult(String output) {
    formatString(output);
    service.deleteAnalyseResult(this.arcId);
    service.saveAnalseResult(this.arcId, output);
    return this.output;
  }
}
