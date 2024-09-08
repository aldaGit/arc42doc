package org.arc42.analyse.model.evaluation.regelsaetze;

import java.util.List;
import org.arc42.analyse.model.evaluation.EvaluationResult;
import org.arc42.analyse.model.evaluation.EvaluationResultI;
import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.dokumentation.model.dao.arc42documentation.ImageDAOBaustein;
import org.arc42.dokumentation.model.dto.documentation.ImageDTO;

public class BausteinsichtRegelsatz extends SingleRegelsatzAbstract {

  public BausteinsichtRegelsatz(String arcId) {
    super(arcId);
  }

  public BausteinsichtRegelsatz(String arcId, String id, double gewichtung) {
    super(arcId, id, gewichtung);
  }

  @Override
  public EvaluationResultI evaluate() {
    return new EvaluationResult(checkDiagram(), getName());
  }

  @Override
  public String getName() {
    return TabGlossar.BAUSTEINSICHT;
  }

  private double checkDiagram() {
    List<ImageDTO> diagram = ImageDAOBaustein.getInstance().findAll(super.getArcId());
    double result = 0;
    if (diagram != null) {
      if (diagram.size() > 0) {
        result += 0.5;
        if (diagram.get(0).getDescription() != null
            && !diagram.get(0).getDescription().equals("null")) {
          result += 0.5;
        }
      }
    }
    return result;
  }
}
