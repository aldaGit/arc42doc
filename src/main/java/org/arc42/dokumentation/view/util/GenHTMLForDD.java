package org.arc42.dokumentation.view.util;

import Model.*;
import Model.Class;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import org.arc42.dokumentation.model.dao.arc42documentation.DesignDecisionDAO;

public class GenHTMLForDD {

  private static GenHTMLForDD instance;
  private final DesignDecisionDAO dao;

  private GenHTMLForDD() {
    dao = DesignDecisionDAO.getInstance();
  }

  public static GenHTMLForDD getInstance() {
    if (instance == null) {
      instance = new GenHTMLForDD();
    }
    return instance;
  }

  public String getHtmlForDDs(List<String> ids) {
    StringBuilder ergebnis = new StringBuilder();
    for (String id : ids) {
      String result;
      DesignDecision byId = dao.findById(id);
      if (byId != null) {
        ExistenceDecision decision = (ExistenceDecision) byId;
        StringBuilder list = new StringBuilder();

        String li = "<li>####Alternativ</li>\n";
        for (DDAlternative ddalternative : decision.getAlternatives()) {
          list.append(
              li.replaceFirst(
                  "####Alternativ", Matcher.quoteReplacement(ddalternative.getTitle())));
        }

        list.append(
            li.replaceFirst(
                "####Alternativ", Matcher.quoteReplacement(decision.getDecision().getTitle())));

        String ddString =
            "<li><span style=\"font-family: Arial; font-size:"
                + " large;\"><em>####Title</em></span></li>\n"
                + "  <div><em> <span style=\"color: #7a2518; font-family: Noto Serif, DejaVu Serif,"
                + " serif; font-size: large;\">Problem</span></em><span style=\"font-family:"
                + " Arial;\"><em>:&nbsp;</em><span style=\"font-size:"
                + " small;\">####ProblemText</span></span></div> \n"
                + " <div><span style=\"font-family: Arial;\"><span style=\"font-size:"
                + " small;\">&nbsp;</span></span></div>\n"
                + "<div><em><span style=\"color: #7a2518; font-family: Noto Serif, DejaVu Serif,"
                + " serif; font-size: large;\">Considered Alternatives</span></em></div>\n"
                + "<div>\n"
                + "<ul>\n"
                + "###List</ul>\n"
                + "<div><em><span style=\"color: #7a2518; font-family: Noto Serif, DejaVu Serif,"
                + " serif; font-size: large;\">Decision Outcome:&nbsp;</span><span"
                + " style=\"font-family: Arial;\">####DecisionOutcome</span></em></div>\n"
                + "<div></div>\n"
                + "<div><em><span style=\"color: #7a2518; font-family: Noto Serif, DejaVu Serif,"
                + " serif; font-size: large;\">Affected Artifact:&nbsp;</span><span"
                + " style=\"font-family: Arial;\">####Artifacts</span></em></div>\n"
                + "<div></div>\n"
                + "</div>\n"
                + "<div>&nbsp;</div>\n";
        result = ddString.replaceFirst("####Title", Matcher.quoteReplacement(decision.getTitle()));
        result =
            result.replaceFirst(
                "####ProblemText",
                ((DDIssue) (decision.getIssues().toArray()[0])).getBeschreibung());
        result = result.replaceFirst("###List", Matcher.quoteReplacement(list.toString()));
        result =
            result.replaceFirst(
                "####DecisionOutcome", Matcher.quoteReplacement(decision.getDecision().getTitle()));
        result =
            result.replaceFirst(
                "####Artifacts",
                listAsString(
                    decision.getArchitekturElements().stream()
                        .map(umlComponent -> ((Class) umlComponent).getName())
                        .collect(Collectors.toSet())));
        ergebnis.append(result);
      }
    }
    String open = "<div>\n" + "<ol>\n";
    String close = "</ol>\n" + "\n" + "</div>";
    ergebnis = new StringBuilder(open + ergebnis + close);
    return ergebnis.toString();
  }

  private String listAsString(Set<String> strings) {
    StringBuilder result = new StringBuilder();
    Object[] array = strings.toArray();
    if (strings != null && !strings.isEmpty()) {
      for (int i = 0; i < strings.size(); i++) {
        if (i == 0) {
          result.append(array[i]);
        } else {
          result.append(",").append(array[i]);
        }
      }
    }
    return result.toString();
  }
}
