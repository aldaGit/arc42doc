package org.arc42.dokumentation.control.util;

import Model.DDAlternative;
import Model.DDRationale;
import Model.ExistenceDecision;
import Model.UMLComponent;
import Parser.Parser;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.arc42.dokumentation.control.logic.GoogleBigQueryController;
import org.arc42.dokumentation.control.service.exception.GoogleBigQueryException;
import org.arc42.dokumentation.control.service.exception.InvalidLink;
import org.arc42.dokumentation.model.dto.documentation.SoAnswersDTO;
import org.arc42.dokumentation.view.util.methods.StaticUtils;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.HtmlRenderer;

public class DesignDecisionParser {

  public static List<UMLComponent> parseFile(String file) {
    File testFile = new File(file);
    Parser parser = new Parser();
    List<UMLComponent> l = parser.parseFile(testFile);
    return l.stream()
        .filter(x -> (x instanceof ExistenceDecision))
        .map(
            decision -> {
              ExistenceDecision decision1 = (ExistenceDecision) decision;
              try {
                DDRationale rationale = ((ExistenceDecision) decision).getRationale();
                if (rationale != null
                    && rationale.getReferenz() != null
                    && !rationale.getReferenz().isEmpty()) {
                  GoogleBigQueryController controller = GoogleBigQueryController.getInstance();
                  String reference = rationale.getReferenz();
                  if (reference.trim().toLowerCase().contains("stackoverflow".toLowerCase())) {
                    List<SoAnswersDTO> answersWithUserNameForRefLink =
                        controller.getAnswersWithUserNameForRefLink(reference);
                    decision1 = parseStackoverflowContent(answersWithUserNameForRefLink, decision1);
                  } else if (reference.trim().toLowerCase().contains("github")) {
                    if (reference.trim().toLowerCase().contains("raw.githubusercontent")) {
                      String mdContent = StaticUtils.getContentFromUrl(reference);
                      decision1 = parseGitHubContent(mdContent, decision1);
                    } else {
                      String[] repoNameAndFileFromGithubLink =
                          StaticUtils.getRepoNameAndFileFromGithubLink(reference);
                      String mdContent =
                          controller.getDecisionByFileAndRepoName(
                              repoNameAndFileFromGithubLink[1], repoNameAndFileFromGithubLink[0]);
                      decision1 = parseGitHubContent(mdContent, decision1);
                    }
                  }
                }
              } catch (IOException
                  | InterruptedException
                  | GoogleBigQueryException
                  | InvalidLink e) {
                e.printStackTrace();
              }

              return decision1;
            })
        .collect(Collectors.toList());
  }

  private static ExistenceDecision parseStackoverflowContent(
      List<SoAnswersDTO> answersForRefLink, ExistenceDecision decision) {
    Set<DDAlternative> alternatives = new HashSet<>(10);
    String choice = decision.getBeschreibung();
    answersForRefLink.forEach(
        soAnswersDTO -> {
          System.out.println(soAnswersDTO.getUserId() + "---------" + choice + "-------UNAME");
          DDAlternative alternative =
              new DDAlternative(
                  soAnswersDTO.getAnswerId(),
                  soAnswersDTO.getAnswerBody(),
                  soAnswersDTO.getAnswerBody());
          if (soAnswersDTO.getUserId().equalsIgnoreCase(choice)) {
            decision.setDecision(alternative);
            decision.setBeschreibung(soAnswersDTO.getAnswerBody());
          } else {
            alternatives.add(alternative);
          }
        });
    decision.setAlternatives(alternatives);
    return decision;
  }

  private static ExistenceDecision parseGitHubContent(
      String mdContent, ExistenceDecision decision) {
    String choice = decision.getBeschreibung();
    String[] contents = mdContent.split("(\\n\\s*)##(?!#)");
    Set<DDAlternative> ddAlternativeSet = new HashSet<>(10);
    DDAlternative[] ddAlternatives = new DDAlternative[10];
    for (String content : contents) {
      if (content.toLowerCase().contains("Considered Options".toLowerCase())
          || content.toLowerCase().contains("Considered Alternatives".toLowerCase())) {
        System.out.println(content + "----------------");
        String[] alternatives = content.split("\\*");
        for (int i = 0; i < alternatives.length; i++) {
          if (i == 0) continue;

          String alternative = alternatives[i];
          System.out.println(alternative + "----------------alt");

          org.commonmark.parser.Parser parser = org.commonmark.parser.Parser.builder().build();
          Node document = parser.parse(alternative);
          HtmlRenderer renderer = HtmlRenderer.builder().build();
          String render = renderer.render(document);

          DDAlternative ddAlternative =
              new DDAlternative(UUID.randomUUID().toString(), "", render.trim());
          ddAlternatives[i - 1] = ddAlternative;
        }
      }
      if (content.toLowerCase().contains("Pros and Cons".toLowerCase())) {
        String[] vornach_teilen = content.split("(\\n\\s*)###");
        for (int j = 0; j < vornach_teilen.length; j++) {
          if (j == 0) continue;
          String vornach = vornach_teilen[j];
          String[] split = vornach.split("(\\n\\s*)");
          DDAlternative ddAlternative = ddAlternatives[j - 1];
          Set<String> vorteile = new HashSet<>();
          Set<String> nachteile = new HashSet<>();
          boolean wahl = false;
          for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (s.toLowerCase().contains("Pros and Cons".toLowerCase())) continue;

            if (i == 0) {
              ddAlternative.setTitle(s);
              System.out.println(s + " ++++++++++++");
              if (s.trim().equalsIgnoreCase(choice)) {
                wahl = true;
              }
            }
            if (s.toLowerCase().replaceFirst("\\*", "").trim().startsWith("Good".toLowerCase())
                || s.toLowerCase().replaceFirst("\\*", "").trim().startsWith("`+`".toLowerCase())) {

              org.commonmark.parser.Parser parser = org.commonmark.parser.Parser.builder().build();
              Node document = parser.parse(s);
              HtmlRenderer renderer = HtmlRenderer.builder().build();
              String render = renderer.render(document);
              vorteile.add(render);
            }

            if (s.toLowerCase().replaceFirst("\\*", "").trim().startsWith("Bad".toLowerCase())
                || s.toLowerCase().replaceFirst("\\*", "").trim().startsWith("`-`".toLowerCase())) {
              org.commonmark.parser.Parser parser = org.commonmark.parser.Parser.builder().build();
              Node document = parser.parse(s);
              HtmlRenderer renderer = HtmlRenderer.builder().build();
              String render = renderer.render(document);
              nachteile.add(render);
            }
          }
          ddAlternative.setVorteile(vorteile);
          ddAlternative.setNachteile(nachteile);
          if (wahl) {
            decision.setDecision(ddAlternative);
            decision.setBeschreibung(ddAlternative.getTitle());
          } else {
            ddAlternativeSet.add(ddAlternative);
          }
        }
      }
    }
    decision.setAlternatives(ddAlternativeSet);
    return decision;
  }
}
