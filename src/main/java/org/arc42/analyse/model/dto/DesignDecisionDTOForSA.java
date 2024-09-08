package org.arc42.analyse.model.dto;

public class DesignDecisionDTOForSA {

  private String id;
  private String title;

  public DesignDecisionDTOForSA(String id, String title) {
    this.id = id;
    this.title = title;
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
