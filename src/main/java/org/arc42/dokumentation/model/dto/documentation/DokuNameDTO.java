package org.arc42.dokumentation.model.dto.documentation;

public class DokuNameDTO {

  private String name;
  private String id;

  public DokuNameDTO(String name) {
    this.name = name;
  }

  public DokuNameDTO(String name, String id) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }
}
