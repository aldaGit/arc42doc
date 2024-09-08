package org.arc42.dokumentation.model.dto.documentation;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.arc42.dokumentation.view.components.customComponents.BadgeComponent;

public abstract class QualityDTO {

  private String id;
  private String qualitaetsziel;
  private String motivation;
  private List<String> qualityCriteria;

  QualityDTO(String qualitaetsziel, String motivation, List<String> qualityCriteria) {
    this.qualitaetsziel = qualitaetsziel;
    this.motivation = motivation;
    this.qualityCriteria = qualityCriteria;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getQualitaetsziel() {
    return qualitaetsziel;
  }

  public void setQualitaetsziel(String qualitaetsziel) {
    this.qualitaetsziel = qualitaetsziel;
  }

  public String getMotivation() {
    return motivation;
  }

  public void setMotivation(String motivation) {
    this.motivation = motivation;
  }

  public List<String> getQualityCriteria() {
    return qualityCriteria;
  }

  public HorizontalLayout generateBadge() {
    HorizontalLayout horizontalLayout = new HorizontalLayout();
    for (Object critera : this.qualityCriteria) {
      horizontalLayout.add(new BadgeComponent().getBadge((String) critera));
    }
    return horizontalLayout;
  }

  public void setQualityCriteria(List qualityCriteria) {
    this.qualityCriteria = qualityCriteria;
  }

  public static List<String> getPriority() {
    return new ArrayList<>(Arrays.asList("Niedrig", "Mittel", "Hoch"));
  }

  public boolean equals(QualityDTO obj) {
    return id.equals(obj.getId());
  }
}
