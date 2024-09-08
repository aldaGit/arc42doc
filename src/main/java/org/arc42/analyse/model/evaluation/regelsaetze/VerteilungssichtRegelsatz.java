package org.arc42.analyse.model.evaluation.regelsaetze;

import org.arc42.analyse.model.evaluation.EvaluationResult;
import org.arc42.analyse.model.evaluation.EvaluationResultI;
import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.dokumentation.model.dao.arc42documentation.HardwareDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.ImageDAOVerteilung;
import org.arc42.dokumentation.model.dto.documentation.ImageDTO;

public class VerteilungssichtRegelsatz extends SingleRegelsatzAbstract {

  // Hardwarekomponente
  private final int minHardware = 0;
  private int sollHardware = 1;
  private final int maxHardware = 10;

  public VerteilungssichtRegelsatz(String arcId) {
    super(arcId);
  }

  public VerteilungssichtRegelsatz(String arcId, String id, double gewichtung, int sollHardware) {
    super(arcId, id, gewichtung);
    this.sollHardware = sollHardware;
  }

  @Override
  public EvaluationResultI evaluate() {
    return new EvaluationResult(
        Math.round((checkDiagram() + checkHardwareKomponenten()) * 100.0) / 100.0, getName());
  }

  @Override
  public String getName() {
    return TabGlossar.VERTEILUNGSSICHT;
  }

  public int getMinHardware() {
    return minHardware;
  }

  public int getSollHardware() {
    return sollHardware;
  }

  public boolean setSollHardware(int sollHardware) {
    if (sollHardware >= minHardware && sollHardware <= maxHardware) {
      this.sollHardware = sollHardware;
      return true;
    }
    return false;
  }

  public int getMaxHardware() {
    return maxHardware;
  }

  private double checkDiagram() {
    ImageDTO diagram = ImageDAOVerteilung.getInstance().findById(super.getArcId());
    double result = 0;
    if (diagram != null) {
      result += 0.333;
      if (diagram.getDescription() != null && !diagram.getDescription().equals("null")) {
        result += 0.333;
      }
    }
    return result;
  }

  private double checkHardwareKomponenten() {
    return HardwareDAO.getInstance().findAll(super.getArcId()).size() >= sollHardware ? 0.333 : 0.0;
  }
}
