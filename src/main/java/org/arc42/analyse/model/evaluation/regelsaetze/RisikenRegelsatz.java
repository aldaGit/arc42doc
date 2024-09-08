package org.arc42.analyse.model.evaluation.regelsaetze;

import java.util.List;
import org.arc42.analyse.model.evaluation.EvaluationResult;
import org.arc42.analyse.model.evaluation.EvaluationResultI;
import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.dokumentation.model.dao.arc42documentation.RisikoDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.TextEingabeDAO;
import org.arc42.dokumentation.model.dto.documentation.TextEingabeDTO;

public class RisikenRegelsatz extends SingleRegelsatzAbstract {

  // Checkliste
  private final int minRisiken = 1;
  private int sollRisiken = 1;
  private final int maxRisiken = 10;

  // SWOT-Analyse
  private final int minEntries = 1;
  private int sollEntries = 1;
  private final int maxEntries = 10;

  public RisikenRegelsatz(String arcId) {
    super(arcId);
  }

  public RisikenRegelsatz(
      String arcId, String id, double gewichtung, int sollRisiken, int sollEntries) {
    super(arcId, id, gewichtung);
    this.sollRisiken = sollRisiken;
    this.sollEntries = sollEntries;
  }

  @Override
  public EvaluationResultI evaluate() {
    return new EvaluationResult(checkNumberRisks() + checkSWOT(), getName());
  }

  @Override
  public String getName() {
    return TabGlossar.RISIKEN;
  }

  public int getMinRisiken() {
    return minRisiken;
  }

  public int getSollRisiken() {
    return sollRisiken;
  }

  public boolean setSollRisiken(int sollRisiken) {
    if (sollRisiken >= minRisiken && sollRisiken <= maxRisiken) {
      this.sollRisiken = sollRisiken;
      return true;
    }
    return false;
  }

  public int getMaxRisiken() {
    return maxRisiken;
  }

  public int getMinEntries() {
    return minEntries;
  }

  public int getSollEntries() {
    return sollEntries;
  }

  public boolean setSollEntries(int sollEntries) {
    if (sollEntries >= minEntries && sollEntries <= maxEntries) {
      this.sollEntries = sollEntries;
      return true;
    }
    return false;
  }

  public int getMaxEntries() {
    return maxEntries;
  }

  private double checkNumberRisks() {
    return RisikoDAO.getInstance().findAllByArcId(super.getArcId()).size() >= sollRisiken
        ? 0.5
        : 0.0;
  }

  private double checkSWOT() {
    List<TextEingabeDTO> swotDTOs = TextEingabeDAO.getInstance().findAllByArcId(super.getArcId());
    int strengths = 0;
    int weaknesses = 0;
    int opportunities = 0;
    int threats = 0;
    for (TextEingabeDTO dto : swotDTOs) {
      switch (dto.getType()) {
        case STRENGTH -> ++strengths;
        case WEAKNESS -> ++weaknesses;
        case OPPORTUNITY -> ++opportunities;
        case THREAT -> ++threats;
        default -> {}
      }
    }
    return strengths >= sollEntries
            && weaknesses >= sollEntries
            && opportunities >= sollEntries
            && threats >= sollEntries
        ? 0.5
        : 0.0;
  }
}
