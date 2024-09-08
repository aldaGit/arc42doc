package org.arc42.analyse.model.evaluation.regelsaetze;

import java.util.List;
import org.arc42.analyse.model.evaluation.EvaluationResult;
import org.arc42.analyse.model.evaluation.EvaluationResultI;
import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.dokumentation.model.dao.arc42documentation.QualityGoalDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.QualityScenarioDAO;
import org.arc42.dokumentation.model.dto.documentation.QualityGoalDTO;
import org.arc42.dokumentation.model.dto.documentation.QualityScenarioDTO;

public class QualitaetsszenarienRegelsatz extends SingleRegelsatzAbstract {

  public QualitaetsszenarienRegelsatz(String arcId) {
    super(arcId);
  }

  public QualitaetsszenarienRegelsatz(String arcId, String id, double gewichtung) {
    super(arcId, id, gewichtung);
  }

  @Override
  public EvaluationResultI evaluate() {
    return new EvaluationResult(checkScenariosForGoals() + checkScenariosComplete(), getName());
  }

  @Override
  public String getName() {
    return TabGlossar.QUALITAETSSZENARIEN;
  }

  private double checkScenariosForGoals() {
    List<QualityGoalDTO> qualityGoals =
        QualityGoalDAO.getInstance().findAllByArcId(super.getArcId());
    if (qualityGoals.size() == 0) {
      return 0.0;
    }
    List<QualityScenarioDTO> qualityScenarios =
        QualityScenarioDAO.getInstance().findAllByArcId(super.getArcId());
    boolean allGoalsDocumented = true;
    for (QualityGoalDTO goal : qualityGoals) {
      boolean present = false;
      for (QualityScenarioDTO scenario : qualityScenarios) {
        if (scenario.getQualityGoalDTO() != null
            && scenario.getQualityGoalDTO().getId().equals(goal.getId())) {
          present = true;
          break;
        }
      }
      if (!present) {
        allGoalsDocumented = false;
        break;
      }
    }
    return allGoalsDocumented ? 0.5 : 0.0;
  }

  private double checkScenariosComplete() {
    List<QualityScenarioDTO> dtos = QualityScenarioDAO.getInstance().findAll(super.getArcId());
    double counterComplete = 0.0;
    for (QualityScenarioDTO dto : dtos) {
      if (!dto.getStimulus().equals("")
          && !dto.getReaction().equals("")
          && !dto.getResponse().equals("")
          && dto.getQualityGoalDTO() != null
          && !dto.getCurrentPriority().equals("")
          && !dto.getRisk().equals("")) {
        ++counterComplete;
      }
    }
    return dtos.size() == 0 ? 0.0 : (counterComplete / dtos.size()) / 2.0;
  }
}
