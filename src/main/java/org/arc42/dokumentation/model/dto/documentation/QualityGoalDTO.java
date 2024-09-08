package org.arc42.dokumentation.model.dto.documentation;

import java.util.List;

public class QualityGoalDTO extends QualityDTO {

  public QualityGoalDTO(String qualitaetsziel, String motivation, List<String> qualityCriteria) {
    super(qualitaetsziel, motivation, qualityCriteria);
  }

  @Override
  public String toString() {
    return "QZ" + getId();
  }
}
