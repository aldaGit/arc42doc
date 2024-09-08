package org.arc42.dokumentation.model.dto.documentation;

public class LoesungsStrategieDTO {

  private String id;
  private String strategy;
  private String nId;
  private String qualityGoalId;

  public LoesungsStrategieDTO(String strategy) {
    this.strategy = strategy;
  }

  public LoesungsStrategieDTO(String strategy, String qualityGoalId) {
    this.strategy = strategy;
    this.qualityGoalId = qualityGoalId;
  }

  public LoesungsStrategieDTO(String strategy, String nId, String qualityGoalId) {
    this.strategy = strategy;
    this.nId = nId;
    this.qualityGoalId = qualityGoalId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getStrategy() {
    return strategy;
  }

  public void setStrategy(String strategy) {
    this.strategy = strategy;
  }

  public String getnId() {
    return nId;
  }

  public void setnId(String nId) {
    this.nId = nId;
  }

  public String getQualityGoalId() {
    return qualityGoalId;
  }

  public void setQualityGoalId(String qualityGoalId) {
    this.qualityGoalId = qualityGoalId;
  }
}
