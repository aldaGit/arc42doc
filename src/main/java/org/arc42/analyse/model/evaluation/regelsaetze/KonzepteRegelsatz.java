package org.arc42.analyse.model.evaluation.regelsaetze;

import java.util.List;
import org.arc42.analyse.model.evaluation.EvaluationResult;
import org.arc42.analyse.model.evaluation.EvaluationResultI;
import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.dokumentation.model.dao.arc42documentation.KonzepteDAO;
import org.arc42.dokumentation.model.dto.documentation.KonzeptDTO;
import org.arc42.dokumentation.view.util.BadgeGlossary;

public class KonzepteRegelsatz extends SingleRegelsatzAbstract {

  // Konzeptkategorien
  List<String> neededConceptCategories = List.of(BadgeGlossary.CONCEPTBADGES.get(0));

  public KonzepteRegelsatz(String arcId) {
    super(arcId);
  }

  public KonzepteRegelsatz(
      String arcId, String id, double gewichtung, List<String> neededConceptCategories) {
    super(arcId, id, gewichtung);
    this.neededConceptCategories = neededConceptCategories;
  }

  @Override
  public EvaluationResultI evaluate() {
    return new EvaluationResult(checkConceptCategories(), getName());
  }

  @Override
  public String getName() {
    return TabGlossar.KONZEPTE;
  }

  public List<String> getNeededConceptCategories() {
    return neededConceptCategories;
  }

  public void setNeededConceptCategories(List<String> neededConceptCategories) {
    this.neededConceptCategories = neededConceptCategories;
  }

  private double checkConceptCategories() {
    List<KonzeptDTO> konzepte = KonzepteDAO.getInstance().findAll(super.getArcId());
    if (neededConceptCategories.size() == 0) {
      return 1.0;
    }
    double counterDocumented = 0.0;
    for (String needed : neededConceptCategories) {
      boolean found = false;
      for (KonzeptDTO konzept : konzepte) {
        List<String> categories = konzept.getConceptCategories();
        for (String category : categories) {
          if (category.equals(needed)) {
            ++counterDocumented;
            found = true;
          }
        }
        if (found) {
          break;
        }
      }
    }
    return Math.min(counterDocumented / neededConceptCategories.size(), 1.0);
  }
}
