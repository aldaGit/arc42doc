package org.arc42.dokumentation.model.dto.documentation;

public class HardwareDTO {

  private String id;
  private String name;
  private double powerConsumption;
  private boolean isRenewable;

  public HardwareDTO(String name, double powerConsumption, boolean isRenewable) {
    this.name = name;
    this.powerConsumption = powerConsumption;
    this.isRenewable = isRenewable;
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

  public double getPowerConsumption() {
    return powerConsumption;
  }

  public void setPowerConsumption(double powerConsumption) {
    this.powerConsumption = powerConsumption;
  }

  public boolean isRenewable() {
    return isRenewable;
  }

  public void setRenewable(boolean renewable) {
    this.isRenewable = renewable;
  }
}
