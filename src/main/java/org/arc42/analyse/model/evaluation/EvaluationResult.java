package org.arc42.analyse.model.evaluation;

public class EvaluationResult implements EvaluationResultI {

  private final double result;
  private final String tab;

  public EvaluationResult(double result, String tab) {
    this.result = result;
    this.tab = tab;
  }

  @Override
  public double getResult() {
    return result;
  }

  @Override
  public String getTab() {
    return tab;
  }

  @Override
  public String getResultAsString() {
    if (getTab().equals(TabGlossar.GESAMT)) {
      return String.format("%d%s", Math.round(getResult()), " %");
    }
    return String.format("%d%s", Math.round(getResult() * 100), " %");
  }
}
