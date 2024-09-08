package org.arc42.dokumentation.model.dto.documentation;

public class RequirementDTO {

  private String id;
  private String aufgabe;

  public RequirementDTO(String aufgabe) {
    this.aufgabe = aufgabe;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAufgabe() {
    return aufgabe;
  }

  public void setAufgabe(String aufgabe) {
    this.aufgabe = aufgabe;
  }
}
