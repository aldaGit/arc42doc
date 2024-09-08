package org.arc42.dokumentation.model.dto.documentation;

public class KonventionDTO {

  private String id;
  private String konvention;
  private String erlaeuterung;

  public KonventionDTO(String konvention, String erlaeuterung) {
    this.konvention = konvention;
    this.erlaeuterung = erlaeuterung;
  }

  public KonventionDTO(String id, String konvention, String erlaeuterung) {
    this.id = id;
    this.konvention = konvention;
    this.erlaeuterung = erlaeuterung;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getKonvention() {
    return konvention;
  }

  public void setKonvention(String konvention) {
    this.konvention = konvention;
  }

  public String getErlaeuterung() {
    return erlaeuterung;
  }

  public void setErlaeuterung(String erlaeuterung) {
    this.erlaeuterung = erlaeuterung;
  }
}
