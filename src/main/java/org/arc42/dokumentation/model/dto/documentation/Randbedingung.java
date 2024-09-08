package org.arc42.dokumentation.model.dto.documentation;

public abstract class Randbedingung {

  private String id;
  private String randbedingung;
  private String hintergrund;

  public Randbedingung(String randbedingung, String hintergrund) {
    this.randbedingung = randbedingung;
    this.hintergrund = hintergrund;
  }

  public Randbedingung(String id, String randbedingung, String hintergrund) {
    this.id = id;
    this.randbedingung = randbedingung;
    this.hintergrund = hintergrund;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRandbedingung() {
    return randbedingung;
  }

  public void setRandbedingung(String randbedingung) {
    this.randbedingung = randbedingung;
  }

  public String getHintergrund() {
    return hintergrund;
  }

  public void setHintergrund(String hintergrund) {
    this.hintergrund = hintergrund;
  }
}
