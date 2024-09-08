package org.arc42.analyse.model.evaluation.regelsaetze;

import java.util.List;
import org.arc42.analyse.model.evaluation.EvaluationResult;
import org.arc42.analyse.model.evaluation.EvaluationResultI;
import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.dokumentation.model.dao.arc42documentation.GlossarDAO;
import org.arc42.dokumentation.model.dto.documentation.GlossarEintragDTO;

public class GlossarRegelsatz extends SingleRegelsatzAbstract {

  private int minWortanzahl = 1;
  private int maxWortanzahl = 100;
  private boolean checked = false;

  public GlossarRegelsatz(String arcId) {
    super(arcId);
  }

  public GlossarRegelsatz(
      String arcId,
      String id,
      double gewichtung,
      int minWortanzahl,
      int maxWortanzahl,
      boolean checked) {
    super(arcId, id, gewichtung);
    this.minWortanzahl = minWortanzahl;
    this.maxWortanzahl = maxWortanzahl;
    this.checked = checked;
  }

  @Override
  public EvaluationResultI evaluate() {
    return checked
        ? new EvaluationResult(checkDescriptionLength(), getName())
        : new EvaluationResult(1.0, getName());
  }

  @Override
  public String getName() {
    return TabGlossar.GLOSSAR;
  }

  public int getMinWortanzahl() {
    return minWortanzahl;
  }

  public int getMaxWortanzahl() {
    return maxWortanzahl;
  }

  public boolean isChecked() {
    return checked;
  }

  public void setMinWortanzahl(int minWortanzahl) {
    this.minWortanzahl = minWortanzahl;
  }

  public void setMaxWortanzahl(int maxWortanzahl) {
    this.maxWortanzahl = maxWortanzahl;
  }

  public void setChecked(boolean checked) {
    this.checked = checked;
  }

  private double checkDescriptionLength() {
    List<GlossarEintragDTO> dtos = GlossarDAO.getInstance().findAllByArcId(super.getArcId());
    if (dtos.size() == 0) {
      return 1.0;
    }
    double countPassed = 0.0;
    for (GlossarEintragDTO dto : dtos) {
      int words = dto.getBeschreibung().split("\\s+").length;
      if (!dto.getBeschreibung().equals("") && words >= minWortanzahl && words <= maxWortanzahl) {
        ++countPassed;
      }
    }
    return countPassed / dtos.size();
  }
}
