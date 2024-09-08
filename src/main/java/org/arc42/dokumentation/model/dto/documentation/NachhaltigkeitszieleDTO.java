package org.arc42.dokumentation.model.dto.documentation;

import java.util.Arrays;

public class NachhaltigkeitszieleDTO extends QualityDTO {

  private String prio;

  private String saving;

  private String goal;

  public NachhaltigkeitszieleDTO(String goal, String motivation, String prio, String saving) {
    super(goal, motivation, Arrays.asList("Nachhaltigkeit", "green"));
    this.prio = prio;
    this.saving = saving;
    this.goal = goal;
  }

  public String getPrio() {
    return prio;
  }

  public void setPrio(String prio) {
    this.prio = prio;
  }

  public String getSaving() {
    return saving;
  }

  public void setSaving(String saving) {
    this.saving = saving;
  }

  public String getGoal() {
    return goal;
  }

  public void setGoal(String goal) {
    this.goal = goal;
  }
}
