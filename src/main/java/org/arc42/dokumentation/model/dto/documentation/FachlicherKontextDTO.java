package org.arc42.dokumentation.model.dto.documentation;

public class FachlicherKontextDTO {
  private String id;
  private String kommunikationspartner;
  private String input;
  private String output;
  private String beschreibung;
  private String risiken;
  private ImageDTO imageDTO;

  public FachlicherKontextDTO(
      String kommunikationspartner,
      String input,
      String output,
      String beschreibung,
      String risiken) {
    this.kommunikationspartner = kommunikationspartner;
    this.input = input;
    this.output = output;
    this.beschreibung = beschreibung;
    this.risiken = risiken;
  }

  public String getId() {
    return id;
  }

  public String getKommunikationspartner() {
    return kommunikationspartner;
  }

  public String getInput() {
    return input;
  }

  public String getOutput() {
    return output;
  }

  public String getBeschreibung() {
    return beschreibung;
  }

  public String getRisiken() {
    return risiken;
  }

  public ImageDTO getImageDTO() {
    return imageDTO;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setKommunikationspartner(String kommunikationspartner) {
    this.kommunikationspartner = kommunikationspartner;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public void setOutput(String output) {
    this.output = output;
  }

  public void setBeschreibung(String beschreibung) {
    this.beschreibung = beschreibung;
  }

  public void setRisiken(String risiken) {
    this.risiken = risiken;
  }
}
