package org.arc42.analyse.model.evaluation.regelsaetze;

import java.util.List;
import org.arc42.analyse.model.evaluation.EvaluationResult;
import org.arc42.analyse.model.evaluation.EvaluationResultI;
import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.dokumentation.model.dao.arc42documentation.EntwurfsentscheidungDAO;
import org.arc42.dokumentation.model.dto.documentation.EntwurfsentscheidungDTO;

public class EntwurfsentscheidungenRegelsatz extends SingleRegelsatzAbstract {

  private final int minEntwurfsentscheidungen = 1;
  private int sollEntwurfsentscheidungen = 1;
  private final int maxEntwurfsentscheidungen = 10;

  public EntwurfsentscheidungenRegelsatz(String arcId) {
    super(arcId);
  }

  public EntwurfsentscheidungenRegelsatz(
      String arcId, String id, double gewichtung, int sollEntwurfsentscheidungen) {
    super(arcId, id, gewichtung);
    this.sollEntwurfsentscheidungen = sollEntwurfsentscheidungen;
  }

  @Override
  public EvaluationResultI evaluate() {
    return new EvaluationResult(checkAnzahlEntwurfsentscheidungen() + checkEEComplete(), getName());
  }

  @Override
  public String getName() {
    return TabGlossar.ENTWURFSENTSCHEIDUNGEN;
  }

  public int getMinEntwurfsentscheidungen() {
    return minEntwurfsentscheidungen;
  }

  public int getSollEntwurfsentscheidungen() {
    return sollEntwurfsentscheidungen;
  }

  public boolean setSollEntwurfsentscheidungen(int anzahl) {
    if (anzahl >= minEntwurfsentscheidungen && anzahl <= maxEntwurfsentscheidungen) {
      sollEntwurfsentscheidungen = anzahl;
      return true;
    }
    return false;
  }

  public int getMaxEntwurfsentscheidungen() {
    return maxEntwurfsentscheidungen;
  }

  private double checkAnzahlEntwurfsentscheidungen() {
    return EntwurfsentscheidungDAO.getInstance().findAll(super.getArcId()).size()
            >= sollEntwurfsentscheidungen
        ? 0.5
        : 0;
  }

  /**
   * Checks the completeness of Entwurfsentscheidungen
   *
   * @return value between 0.0 and 0.5
   */
  private double checkEEComplete() {
    List<EntwurfsentscheidungDTO> dtos =
        EntwurfsentscheidungDAO.getInstance().findAll(super.getArcId());
    double counterComplete = 0;
    for (EntwurfsentscheidungDTO dto : dtos) {
      if (!dto.getEntscheidung().equals("")
          && !dto.getBegruendung().equals("")
          && !dto.getKonsequenz().equals("")
          && dto.getWichtigkeit() != null) {
        ++counterComplete;
      }
    }
    return dtos.size() == 0 ? 0.0 : (counterComplete / dtos.size()) / 2.0;
  }
}
