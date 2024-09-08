package org.arc42.analyse.control.service;

import java.util.Map;

public class SWADAnalyseService {

  public void execute(Map<String, String> analyse, int arcId) {
    org.arc42.analyse.control.SWADAnalyseService service =
        new org.arc42.analyse.control.SWADAnalyseService(arcId);
    service.initAnalyse();
    analyse.put("ARC_INIT", service.getOutput());
    System.out.println("############### SWADAnalyse  ################## " + arcId);
  }
}
