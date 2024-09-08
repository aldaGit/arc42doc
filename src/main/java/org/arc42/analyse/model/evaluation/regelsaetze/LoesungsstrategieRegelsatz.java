package org.arc42.analyse.model.evaluation.regelsaetze;

import java.util.List;
import org.arc42.analyse.model.evaluation.EvaluationResult;
import org.arc42.analyse.model.evaluation.EvaluationResultI;
import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.dokumentation.model.dao.arc42documentation.LoesungsStrategieDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.MeetingDAO;
import org.arc42.dokumentation.model.dto.documentation.LoesungsStrategieDTO;

public class LoesungsstrategieRegelsatz extends SingleRegelsatzAbstract {

  // Projektorganisation
  private final int minMeetings = 0;
  private int sollMeetings = 1;
  private final int maxMeetings = 10;

  public LoesungsstrategieRegelsatz(String arcId) {
    super(arcId);
  }

  public LoesungsstrategieRegelsatz(String arcId, String id, double gewichtung, int sollMeetings) {
    super(arcId, id, gewichtung);
    this.sollMeetings = sollMeetings;
  }

  @Override
  public EvaluationResultI evaluate() {
    return new EvaluationResult(
        Math.round((checkLoesungsstrategien() + checkMeetings()) * 100.0) / 100.0, getName());
  }

  @Override
  public String getName() {
    return TabGlossar.LOESUNGSSTRATEGIE;
  }

  public int getMinMeetings() {
    return minMeetings;
  }

  public int getSollMeetings() {
    return sollMeetings;
  }

  public int getMaxMeetings() {
    return maxMeetings;
  }

  public boolean setSollMeetings(int sollMeetings) {
    if (sollMeetings >= minMeetings && sollMeetings <= maxMeetings) {
      this.sollMeetings = sollMeetings;
      return true;
    }
    return false;
  }

  private double checkLoesungsstrategien() {
    List<LoesungsStrategieDTO> dtos = LoesungsStrategieDAO.getInstance().findAll(super.getArcId());
    double result = 0;
    double counterAllgemein = 0;
    double counterAllgemeinComplete = 0.0;
    double counterNachhaltigkeit = 0;
    double counterNachhaltigkeitComplete = 0.0;
    for (LoesungsStrategieDTO dto : dtos) {
      if (dto.getQualityGoalId() != null && !dto.getQualityGoalId().equals("null")) {
        ++counterAllgemein;
        if (!dto.getStrategy().equals("")) {
          ++counterAllgemeinComplete;
        }
      }
      if (dto.getnId() != null && !dto.getnId().equals("null")) {
        ++counterNachhaltigkeit;
        if (!dto.getStrategy().equals("")) {
          ++counterNachhaltigkeitComplete;
        }
      }
    }
    if (counterAllgemein != 0 && counterAllgemeinComplete == counterAllgemein) {
      result += 0.333;
    }
    if (counterNachhaltigkeit != 0 && counterNachhaltigkeitComplete == counterNachhaltigkeit) {
      result += 0.333;
    }
    return result;
  }

  private double checkMeetings() {
    return MeetingDAO.getInstance().findAll(super.getArcId()).size() >= sollMeetings ? 0.333 : 0;
  }
}
