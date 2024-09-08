package org.arc42.analyse.model.evaluation.regelsaetze;

import java.util.List;
import org.arc42.analyse.model.evaluation.EvaluationResult;
import org.arc42.analyse.model.evaluation.EvaluationResultI;
import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.dokumentation.model.dao.arc42documentation.FachlichKontextDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.InterfaceDAO;
import org.arc42.dokumentation.model.dto.documentation.FachlicherKontextDTO;
import org.arc42.dokumentation.model.dto.documentation.InterfaceDTO;

public class KontextRegelsatz extends SingleRegelsatzAbstract {

  // Fachlicher Kontext
  private final int minFachlich = 1;
  private int sollFachlich = 1;
  private final int maxFachlich = 10;

  // Technischer Kontext
  private final int minTechnisch = 1;
  private int sollTechnisch = 1;
  private final int maxTechnisch = 10;

  public KontextRegelsatz(String arcId) {
    super(arcId);
  }

  public KontextRegelsatz(
      String arcId, String id, double gewichtung, int sollFachlich, int sollTechnisch) {
    super(arcId, id, gewichtung);
    this.sollFachlich = sollFachlich;
    this.sollTechnisch = sollTechnisch;
  }

  @Override
  public EvaluationResultI evaluate() {
    return new EvaluationResult(checkFachlich() + checkTechnisch(), getName());
  }

  @Override
  public String getName() {
    return TabGlossar.KONTEXTABGRENZUNG;
  }

  public int getMinFachlich() {
    return minFachlich;
  }

  public int getSollFachlich() {
    return sollFachlich;
  }

  public boolean setSollFachlich(int sollFachlich) {
    if (sollFachlich >= minFachlich && sollFachlich <= maxFachlich) {
      this.sollFachlich = sollFachlich;
      return true;
    }
    return false;
  }

  public int getMaxFachlich() {
    return maxFachlich;
  }

  public int getMinTechnisch() {
    return minTechnisch;
  }

  public int getSollTechnisch() {
    return sollTechnisch;
  }

  public boolean setSollTechnisch(int sollTechnisch) {
    if (sollTechnisch >= minTechnisch && sollTechnisch <= maxTechnisch) {
      this.sollTechnisch = sollTechnisch;
      return true;
    }
    return false;
  }

  public int getMaxTechnisch() {
    return maxTechnisch;
  }

  private double checkFachlich() {
    double result = 0;
    if (FachlichKontextDAO.getInstance().findAll(super.getArcId()).size() >= sollFachlich) {
      result += 0.25;
    }
    if (checkFachlichComplete()) {
      result += 0.25;
    }
    return result;
  }

  private boolean checkFachlichComplete() {
    List<FachlicherKontextDTO> dtos =
        FachlichKontextDAO.getInstance().findAllByArcId(super.getArcId());
    if (dtos.size() == 0) {
      return false;
    }
    double counterComplete = 0.0;
    for (FachlicherKontextDTO dto : dtos) {
      if (!dto.getInput().equals("")
          && !dto.getOutput().equals("")
          && !dto.getBeschreibung().equals("")
          && !dto.getRisiken().equals("")) {
        ++counterComplete;
      }
    }
    return counterComplete == dtos.size();
  }

  private double checkTechnisch() {
    double result = 0;
    if (InterfaceDAO.getInstance().findAll(super.getArcId()).size() >= sollFachlich) {
      result += 0.25;
    }
    if (checkTechnischComplete()) {
      result += 0.25;
    }
    return result;
  }

  private boolean checkTechnischComplete() {
    List<InterfaceDTO> dtos = InterfaceDAO.getInstance().findAll(super.getArcId());
    if (dtos.size() == 0) {
      return false;
    }
    double counterComplete = 0.0;
    for (InterfaceDTO dto : dtos) {
      if (!dto.getDocumentation().equals("")) {
        ++counterComplete;
      }
    }
    return counterComplete == dtos.size();
  }
}
