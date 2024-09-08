package org.arc42.analyse.model.evaluation;

public interface EvaluationResultI {
  /** Returns the result of an evaluation (percent value between 0 and 1) */
  double getResult();

  /** Returns the name of the tab the result is for */
  String getTab();

  /** Returns the result value in percent (e.g. "70 %") */
  String getResultAsString();
}
