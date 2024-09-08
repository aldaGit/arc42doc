package org.arc42.analyse.control.service;

import java.util.Map;

public class CheckVollstaendigkeitService {

  public void execute(Map<String, String> delegate, int arcId) {

    String vollstaendigkeitCheck =
        "<h4><span style=\"color: #0000ff;\"><em><strong>Ergebnis des"
            + " Vollständigkeit-Checks</strong></em></span></h4>\n";
    VollstaendigkeitAnalyseArc42Architektur analyseArc42Architektur =
        new VollstaendigkeitAnalyseArc42Architektur(arcId);
    analyseArc42Architektur.startVollstaendigkeitAnalyseForArcId();

    System.out.println(" ******** Anzahl " + analyseArc42Architektur.getAnzahlDocumented());
    if (!analyseArc42Architektur.getPartialDokumented()
        && !analyseArc42Architektur.getNotDokumented()) {
      String doku =
          "<p><span style=\"color: #00ff00; font-size: small;\"><em>Alle Elemente sind ausreichend"
              + " dokumentiert.</em></span></p>";
      vollstaendigkeitCheck = vollstaendigkeitCheck + doku;
    } else {
      if (analyseArc42Architektur.getNotDokumented()
          && analyseArc42Architektur.getAnzahlDocumented() > 1) {
        String not =
            "<p><span style=\"color: #ff0000; font-size: small;\"><em>Folgende Elemente wurden"
                + " nicht dokumentiert und müssen dokumentiert werden</em></span></p>\n"
                + analyseArc42Architektur.getNotDokumentedString();
        vollstaendigkeitCheck = vollstaendigkeitCheck + not;
      }
      if (analyseArc42Architektur.getNotDokumented()
          && analyseArc42Architektur.getAnzahlDocumented() <= 1) {
        String not =
            "<p><span style=\"color: #ff0000; font-size: small;\"><em>Es wurde noch nichts"
                + " dokumentiert: Sie müssen noch die folgenden Elemente"
                + " dokumentieren</em></span></p>\n"
                + analyseArc42Architektur.getNotDokumentedString();
        vollstaendigkeitCheck = vollstaendigkeitCheck + not;
      }
      if (analyseArc42Architektur.getPartialDokumented()
          && analyseArc42Architektur.getAnzahlDocumented() > 1) {
        String partial =
            "<p><span style=\"color: #0000ff; font-size: small;\"><em>Sie sollte vielleicht mehr"
                + " über die folgenden Elemente schreiben</em></span></p>\n"
                + analyseArc42Architektur.getPartialDokumentedString();
        vollstaendigkeitCheck = vollstaendigkeitCheck + partial;
      }
    }
    delegate.put("ARC_STEP_1", vollstaendigkeitCheck + "\n <p>&nbsp;</p> \n ");

    System.out.println("**********  Voll " + " " + vollstaendigkeitCheck);
  }
}
