package org.arc42.dokumentation.model.dto.documentation;

import com.fasterxml.jackson.annotation.JsonValue;

public class EntwurfsentscheidungDTO {

  private String id;
  private String entscheidung;
  private String konsequenz;
  private String begruendung;
  private Wichtigkeit wichtigkeit;

  public EntwurfsentscheidungDTO(
      String entscheidung, String konsequenz, String begruendung, Wichtigkeit wichtigkeit) {
    this.entscheidung = entscheidung;
    this.konsequenz = konsequenz;
    this.begruendung = begruendung;
    this.wichtigkeit = wichtigkeit;
  }

  public String getId() {
    return id;
  }

  public String getEntscheidung() {
    return entscheidung;
  }

  public String getKonsequenz() {
    return konsequenz;
  }

  public String getBegruendung() {
    return begruendung;
  }

  public Wichtigkeit getWichtigkeit() {
    return wichtigkeit;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setEntscheidung(String entscheidung) {
    this.entscheidung = entscheidung;
  }

  public void setKonsequenz(String konsequenz) {
    this.konsequenz = konsequenz;
  }

  public void setBegruendung(String begruendung) {
    this.begruendung = begruendung;
  }

  public void setWichtigkeit(String wichtigkeit) {
    this.wichtigkeit = Wichtigkeit.fromString(wichtigkeit);
  }

  public enum Wichtigkeit {
    NIEDRIG("niedrig"),
    MITTEL("mittel"),
    HOCH("hoch"),
    NICHT_ERFASST("");

    private final String label;

    Wichtigkeit(String s) {
      this.label = s;
    }

    @JsonValue
    public String getLabel() {
      return label;
    }

    @Override
    public String toString() {
      return this.label;
    }

    public static EntwurfsentscheidungDTO.Wichtigkeit fromString(String into) {
      switch (into) {
        case "niedrig":
          return NIEDRIG;
        case "mittel":
          return MITTEL;
        case "hoch":
          return HOCH;
        case "":
          return NICHT_ERFASST;
        default:
          return NICHT_ERFASST;
      }
    }
  }
}
