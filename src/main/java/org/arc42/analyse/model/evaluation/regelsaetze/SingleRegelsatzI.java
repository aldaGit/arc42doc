package org.arc42.analyse.model.evaluation.regelsaetze;

import org.arc42.analyse.model.evaluation.RegelsatzI;

public interface SingleRegelsatzI extends RegelsatzI {

  double getGewichtung();

  void setGewichtung(double gewichtung);

  String getName();

  String getId();
}
