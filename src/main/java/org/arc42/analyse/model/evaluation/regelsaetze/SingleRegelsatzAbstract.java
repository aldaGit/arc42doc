package org.arc42.analyse.model.evaluation.regelsaetze;

import org.arc42.analyse.model.evaluation.EvaluationResultI;

public abstract class SingleRegelsatzAbstract implements SingleRegelsatzI {

  private final String arcId;
  private String id;
  private double gewichtung = 8.33;

  public SingleRegelsatzAbstract(String arcId) {
    this.arcId = arcId;
  }

  public SingleRegelsatzAbstract(String arcId, String id, double gewichtung) {
    this.arcId = arcId;
    this.id = id;
    this.gewichtung = gewichtung;
  }

  @Override
  public abstract EvaluationResultI evaluate();

  @Override
  public String getArcId() {
    return arcId;
  }

  @Override
  public double getGewichtung() {
    return gewichtung;
  }

  @Override
  public void setGewichtung(double gewichtung) {
    if (gewichtung >= 0 && gewichtung <= 100) {
      this.gewichtung = gewichtung;
    }
  }

  @Override
  public abstract String getName();

  @Override
  public String getId() {
    return id;
  }
}
