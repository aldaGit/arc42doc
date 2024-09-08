package org.arc42.analyse.model.evaluation.regelsaetze;

import org.arc42.analyse.model.evaluation.EvaluationResult;
import org.arc42.analyse.model.evaluation.EvaluationResultI;
import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.dokumentation.model.dao.arc42documentation.KonventionDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.OrganisatorischDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.TechnischDAO;

public class RandbedingungenRegelsatz extends SingleRegelsatzAbstract {

  // min und max stellen die Grenzen zum Einstellen der Soll-Mindestanzahl dar
  // -> es können auch stets mehr Randbedingungen dokumentiert werden
  // -> maxTechnisch = 10 bedeutet, es kann maximal eine Mindestanzahl von 10 eingestellt werden

  // Technisch
  private final int minTechnisch = 1;
  private int sollTechnisch = 1;
  private final int maxTechnisch = 10;

  // Organisatorisch
  private final int minOrgan = 1;
  private int sollOrgan = 1;
  private final int maxOrgan = 10;

  // Konventionen
  private final int minKonventionen = 0;
  private int sollKonventionen = 1;
  private final int maxKonventionen = 10;

  public RandbedingungenRegelsatz(String arcId) {
    super(arcId);
  }

  public RandbedingungenRegelsatz(
      String arcId,
      String id,
      double gewichtung,
      int sollTechnisch,
      int sollOrgan,
      int sollKonventionen) {
    super(arcId, id, gewichtung);
    this.sollTechnisch = sollTechnisch;
    this.sollOrgan = sollOrgan;
    this.sollKonventionen = sollKonventionen;
  }

  @Override
  public EvaluationResultI evaluate() {
    return new EvaluationResult(
        Math.round((checkTechnisch() + checkOrganisatorisch() + checkKonventionen()) * 100.0)
            / 100.0,
        getName());
  }

  @Override
  public String getName() {
    return TabGlossar.RANDBEDINGUNGEN;
  }

  public int getMinTechnisch() {
    return minTechnisch;
  }

  public int getSollTechnisch() {
    return sollTechnisch;
  }

  public int getMaxTechnisch() {
    return maxTechnisch;
  }

  public int getMinOrgan() {
    return minOrgan;
  }

  public int getSollOrgan() {
    return sollOrgan;
  }

  public int getMaxOrgan() {
    return maxOrgan;
  }

  public int getMinKonventionen() {
    return minKonventionen;
  }

  public int getSollKonventionen() {
    return sollKonventionen;
  }

  public int getMaxKonventionen() {
    return maxKonventionen;
  }

  public boolean setSollTechnisch(int sollTechnisch) {
    if (sollTechnisch >= minTechnisch && sollTechnisch <= maxTechnisch) {
      this.sollTechnisch = sollTechnisch;
      return true;
    }
    return false;
  }

  public boolean setSollOrgan(int sollOrgan) {
    if (sollOrgan >= minOrgan && sollOrgan <= maxOrgan) {
      this.sollOrgan = sollOrgan;
      return true;
    }
    return false;
  }

  public boolean setSollKonventionen(int sollKonventionen) {
    if (sollKonventionen >= minKonventionen && sollKonventionen <= maxKonventionen) {
      this.sollKonventionen = sollKonventionen;
      return true;
    }
    return false;
  }

  private double checkTechnisch() {
    return TechnischDAO.getInstance().findAll(super.getArcId()).size() >= sollTechnisch ? 0.333 : 0;
  }

  private double checkOrganisatorisch() {
    return OrganisatorischDAO.getInstance().findAll(super.getArcId()).size() >= sollOrgan
        ? 0.333
        : 0;
  }

  private double checkKonventionen() {
    return KonventionDAO.getInstance().findAll(super.getArcId()).size() >= sollKonventionen
        ? 0.333
        : 0;
  }
}
