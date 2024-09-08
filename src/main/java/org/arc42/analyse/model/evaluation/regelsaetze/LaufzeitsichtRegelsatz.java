package org.arc42.analyse.model.evaluation.regelsaetze;

import org.arc42.analyse.model.evaluation.EvaluationResult;
import org.arc42.analyse.model.evaluation.EvaluationResultI;
import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.dokumentation.model.dao.arc42documentation.ImageDAOLaufzeit;
import org.arc42.dokumentation.model.dto.documentation.ImageDTO;

public class LaufzeitsichtRegelsatz extends SingleRegelsatzAbstract {

  public LaufzeitsichtRegelsatz(String arcId) {
    super(arcId);
  }

  public LaufzeitsichtRegelsatz(String arcId, String id, double gewichtung) {
    super(arcId, id, gewichtung);
  }

  @Override
  public EvaluationResultI evaluate() {
    return new EvaluationResult(checkDiagram(), getName());
  }

  @Override
  public String getName() {
    return TabGlossar.LAUFZEITSICHT;
  }

  private double checkDiagram() {
    ImageDTO diagram = ImageDAOLaufzeit.getInstance().findById(super.getArcId());
    double result = 0;
    if (diagram != null) {
      result += 0.5;
      if (diagram.getDescription() != null && !diagram.getDescription().equals("null")) {
        result += 0.5;
      }
    }
    return result;
  }
}
