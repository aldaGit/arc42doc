package org.arc42.dokumentation.model.dto.documentation;

public class TextEingabeDTO {
  private String content;
  private TEXTTYPE type;
  private String id = "";

  public enum TEXTTYPE {
    STRENGTH,
    WEAKNESS,
    OPPORTUNITY,
    THREAT
  }

  public TextEingabeDTO(String s, TEXTTYPE swot) {
    this.content = s;
    this.type = swot;
  }

  public String getId() {
    return this.id;
  }

  public TEXTTYPE getType() {
    return this.type;
  }

  public String getContent() {
    return this.content;
  }

  public void setId(String i) {
    this.id = i;
  }

  public void setType(TEXTTYPE t) {
    this.type = t;
  }

  public void setContent(String c) {
    this.content = c;
  }
}
