package org.arc42.dokumentation.model.dto.documentation;

public class GlossarEintragDTO {
  private String id;
  private String begriff;
  private String beschreibung;

  public GlossarEintragDTO(String begriff, String beschreibung) {
    this.begriff = begriff;
    this.beschreibung = beschreibung;
  }

  public String getId() {
    return id;
  }

  public String getBegriff() {
    return begriff;
  }

  public String getBeschreibung() {
    return beschreibung;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setBegriff(String begriff) {
    this.begriff = begriff;
  }

  public void setBeschreibung(String beschreibung) {
    this.beschreibung = beschreibung;
  }
}
