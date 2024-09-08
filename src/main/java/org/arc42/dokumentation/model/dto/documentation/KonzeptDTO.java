package org.arc42.dokumentation.model.dto.documentation;

import java.util.List;

public class KonzeptDTO {
  private String id;
  private String name;
  private String text;
  private List<String> conceptCategories;

  public KonzeptDTO(String name, String text, List<String> conceptCategories) {
    this.name = name;
    this.text = text;
    this.conceptCategories = conceptCategories;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getText() {
    return text;
  }

  public void setText(String content) {
    this.text = content;
  }

  public List<String> getConceptCategories() {
    return conceptCategories;
  }

  public void setConceptCategories(List<String> conceptCategories) {
    this.conceptCategories = conceptCategories;
  }
}
