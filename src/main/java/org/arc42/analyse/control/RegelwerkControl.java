package org.arc42.analyse.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.arc42.analyse.model.evaluation.EvaluationResultI;
import org.arc42.analyse.model.evaluation.Regelwerk;
import org.arc42.analyse.model.evaluation.dao.RegelwerkDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.SingleRegelsatzI;

public class RegelwerkControl {

  private Regelwerk regelwerk;

  /** Returns Regelwerk for given arcId */
  public Regelwerk getRegelwerk(String arcId) {
    if (regelwerk == null) {
      regelwerk = RegelwerkDAO.getInstance().findByArcId(arcId);
    }
    if (regelwerk == null
        || regelwerk.getRegelsaetze().isEmpty()
        || regelwerk.getRegelsaetze().containsValue(null)) {
      System.out.println("Creating new Regelwerk");
      createRegelwerk(arcId);
    } else {
      System.out.println("Regelwerk already exists");
      System.out.println("Regelwerk: " + regelwerk.getRegelsaetze().values());
    }
    return regelwerk;
  }

  /** Creates new default Regelwerk for given arcId */
  public boolean createRegelwerk(String arcId) {
    regelwerk = new Regelwerk(arcId);
    try {
      RegelwerkDAO.getInstance().save(regelwerk);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /** Returns a list in which the first elements are tab results and last element is total result */
  public List<EvaluationResultI> getEvaluationResults(String arcId) {
    boolean gotResults = false;
    regelwerk = getRegelwerk(arcId);
    List<EvaluationResultI> results = new ArrayList<>();
    Collection<SingleRegelsatzI> regelsaetze = regelwerk.getRegelsaetze().values();
    for (SingleRegelsatzI regelsatz : regelsaetze) {
      if (regelsatz != null) {
        results.add(regelsatz.evaluate());
        gotResults = true;
      }
    }
    if (gotResults) results.add(regelwerk.evaluate());
    return results;
  }
}
