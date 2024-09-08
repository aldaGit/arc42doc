package org.arc42.analyse.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.arc42.analyse.control.service.Arc42AnalyseService;
import org.arc42.analyse.control.service.GoogleBigQueryConnect;
import org.arc42.analyse.model.dto.Arc42AlternativeDT0;
import org.arc42.analyse.model.dto.Arc42DocumentationDTO;
import org.arc42.analyse.model.util.GithubDecisionParser;
import org.arc42.dokumentation.control.service.exception.InvalidLink;
import org.arc42.dokumentation.view.util.methods.StaticUtils;

public class DesignDecisionAnalyse {

  private final Integer arcId;
  private final Arc42AnalyseService service;
  List<Arc42DocumentationDTO> dtos;
  private GoogleBigQueryConnect bigQueryConnect;

  public DesignDecisionAnalyse(Integer arcId) {
    this.arcId = arcId;
    this.service = Arc42AnalyseService.getInstance();
    try {
      this.bigQueryConnect = GoogleBigQueryConnect.getInstance();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void startAnalyse() {
    int count = this.service.countDesignDecision(this.arcId);
    if (count >= 1) {
      this.dtos = new ArrayList<>(5);
      List<String> rationaleIds = this.service.getRationaleIdForArcId(this.arcId);
      for (String rationaleId : rationaleIds) {
        Arc42DocumentationDTO dto = new Arc42DocumentationDTO(String.valueOf(this.arcId));
        dto.setRationaleId(rationaleId);
        dto.setRationaleReferenz(this.service.getRationaleReferenz(this.arcId, rationaleId));
        dto.setRationaleBeschreibung(
            this.service.getRationaleBeschreibung(this.arcId, rationaleId));
        dto.setDecisionId(this.service.getDecisionIdForRationaleId(this.arcId, rationaleId));
        dto.setDecisionTitle(this.service.getDesignDecisionTitle(dto.getDecisionId()));
        dto.setAffectedArtifact(this.service.getAffectedArchitekturElement(dto.getDecisionId()));
        Arc42AlternativeDT0 referencedAlternative =
            new Arc42AlternativeDT0(
                this.service.getReferencedAlternativeId(dto.getDecisionId()),
                this.service.getReferencedAlternativeTitle(dto.getDecisionId()));
        dto.setReferencedAlternative(referencedAlternative);
        if (dto.getRationaleReferenz().toLowerCase().contains("stackoverflow")) {
          try {
            Arc42AlternativeDT0 acceptedAlternative = new Arc42AlternativeDT0("Keine Id");
            String questionId = StaticUtils.getQuestionIdInRefLink(dto.getRationaleReferenz());
            acceptedAlternative.setId(
                this.bigQueryConnect.getAcceptedAnswerForQuestionId(questionId));
            dto.setAcceptedAlternative(acceptedAlternative);
          } catch (InterruptedException | InvalidLink e) {
            e.printStackTrace();
          }
        } else if (dto.getRationaleReferenz().toLowerCase().contains("github")) {
          String alternative =
              GithubDecisionParser.getAcceptedAlternative(dto.getRationaleReferenz());
          Arc42AlternativeDT0 acceptedAlternative = new Arc42AlternativeDT0(null, alternative);
          dto.setAcceptedAlternative(acceptedAlternative);
        }
        dtos.add(dto);
      }

    } else {
      System.out.println("Es gibt keine Entwurfsentscheidung zu analysieren");
    }
  }

  public List<Arc42DocumentationDTO> getDtos() {
    return dtos;
  }
}
