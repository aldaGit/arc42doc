package org.arc42.dokumentation.model.dto.documentation;

public class InterfaceDTO {
  private String id;
  private String name;
  private String documentation;
  private int callsPerMonth;
  private double emissions;
  private double emissionsPerMonth;

  public InterfaceDTO(String name, String documentation, int callsPerMonth, double emissions) {
    this.name = name;
    this.documentation = documentation;
    this.callsPerMonth = callsPerMonth;
    this.emissions = emissions;
    this.emissionsPerMonth = callsPerMonth * emissions;
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

  public String getDocumentation() {
    return documentation;
  }

  public void setDocumentation(String documentation) {
    this.documentation = documentation;
  }

  public int getCallsPerMonth() {
    return callsPerMonth;
  }

  public void setCallsPerMonth(int callsPerMonth) {
    this.callsPerMonth = callsPerMonth;
  }

  public double getEmissions() {
    return emissions;
  }

  public void setEmissions(double emissions) {
    this.emissions = emissions;
  }

  public double getEmissionsPerMonth() {
    return emissionsPerMonth;
  }

  public void setEmissionsPerMonth(double emissionsPerMonth) {
    this.emissionsPerMonth = emissionsPerMonth;
  }
}
