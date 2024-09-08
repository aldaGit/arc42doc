package org.arc42.dokumentation.model.dto.documentation;

import java.util.List;

public class QualityScenarioDTO extends QualityDTO {

  private String scenarioName;

  private String stimulus;
  private String reaction;
  private String response;
  private QualityDTO qualityGoalDTO;
  private String priority;
  private String risk;

  public QualityScenarioDTO(String qualitaetsziel, String motivation, List qualityCriteria) {
    super(qualitaetsziel, motivation, qualityCriteria);
  }

  @Override
  public String getId() {
    return super.getId();
  }

  public String getStimulus() {
    return stimulus;
  }

  public String getReaction() {
    return reaction;
  }

  public String getResponse() {
    return response;
  }

  public QualityDTO getQualityGoalDTO() {
    return qualityGoalDTO;
  }

  public String getCurrentPriority() {
    return priority;
  }

  public String getRisk() {
    return risk;
  }

  public String getScenarioName() {
    return scenarioName;
  }

  public void setScenarioName(String scenarioName) {
    this.scenarioName = scenarioName;
  }

  public void setStimulus(String stimulus) {
    this.stimulus = stimulus;
  }

  public void setReaction(String reaction) {
    this.reaction = reaction;
  }

  public void setResponse(String response) {
    this.response = response;
  }

  public void setQualityGoalDTO(QualityGoalDTO qualityGoalDTO) {
    this.qualityGoalDTO = qualityGoalDTO;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public void setRisk(String risk) {
    this.risk = risk;
  }

  public void setQualityGoalDTO(QualityDTO qualityGoalDTO) {
    this.qualityGoalDTO = qualityGoalDTO;
    if (qualityGoalDTO != null) {
      super.setMotivation(qualityGoalDTO.getMotivation());
      super.setQualitaetsziel(qualityGoalDTO.getQualitaetsziel());
      super.setQualityCriteria(qualityGoalDTO.getQualityCriteria());
    }
  }

  @Override
  public String toString() {
    return "QS" + getId();
  }

  public double getWeight(String s) {
    double weight = 1.0;
    switch (s) {
      case "Niedrig":
        return weight;
      case "Mittel":
        weight = 2.0;
        break;
      case "Hoch":
        weight = 3.0;
        break;
    }
    return weight;
  }
}
