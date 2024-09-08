package org.arc42.analyse.model.evaluation.regelsaetze;

import java.util.ArrayList;
import java.util.List;
import org.arc42.analyse.model.evaluation.EvaluationResult;
import org.arc42.analyse.model.evaluation.EvaluationResultI;
import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.dokumentation.model.dao.arc42documentation.*;
import org.arc42.dokumentation.model.dto.documentation.NachhaltigkeitszieleDTO;
import org.arc42.dokumentation.model.dto.documentation.QualityGoalDTO;

public class EinfuehrungRegelsatz extends SingleRegelsatzAbstract {

  // Titel
  private int maxTitelLength = 30;

  // Aufgabenstellung
  private final int minMinAufgaben = 1;
  private int minAufgaben = 1;
  private final int maxMinAufgaben = 10;

  // Qualitätsziele
  private final int minMinZiele = 1;
  private int minZiele = 1;
  private final int maxMinZiele = 10;

  // Qualitätszielkategorien
  private List<String> neededQualityCriteria = new ArrayList<>();

  // Stakeholder
  private final int minMinStakeholder = 1;
  private int minStakeholder = 1;
  private final int maxMinStakeholder = 10;

  // Nachhaltigkeitsziele
  private final int minMinNachhaltigkeitsziele = 0;
  private int minNachhaltigkeitsziele = 1;
  private final int maxMinNachhaltigkeitsziele = 10;

  public EinfuehrungRegelsatz(String arcId) {
    super(arcId);
  }

  public EinfuehrungRegelsatz(
      String arcId,
      String id,
      double gewichtung,
      int maxTitelLength,
      int minAufgaben,
      int minZiele,
      List<String> neededQualityCriteria,
      int minStakeholder,
      int minNachhaltigkeitsziele) {
    super(arcId, id, gewichtung);
    this.maxTitelLength = maxTitelLength;
    this.minAufgaben = minAufgaben;
    this.minZiele = minZiele;
    this.neededQualityCriteria = neededQualityCriteria;
    this.minStakeholder = minStakeholder;
    this.minNachhaltigkeitsziele = minNachhaltigkeitsziele;
  }

  @Override
  public EvaluationResultI evaluate() {
    return new EvaluationResult(
        checkTitel()
            + checkAufgabenstellung()
            + checkQualityGoals()
            + checkStakeholder()
            + checkNachhaltigkeitsziele(),
        getName());
  }

  @Override
  public String getName() {
    return TabGlossar.EINFUEHRUNG;
  }

  public int getMaxTitelLength() {
    return maxTitelLength;
  }

  public int getMinAufgaben() {
    return minAufgaben;
  }

  public int getMinZiele() {
    return minZiele;
  }

  public List<String> getNeededQualityCriteria() {
    return neededQualityCriteria;
  }

  public int getMinStakeholder() {
    return minStakeholder;
  }

  public int getMinNachhaltigkeitsziele() {
    return minNachhaltigkeitsziele;
  }

  public void setMaxTitelLength(int maxTitelLength) {
    this.maxTitelLength = maxTitelLength;
  }

  public void setMinAufgaben(int minAufgaben) {
    if (minAufgaben >= minMinAufgaben && minAufgaben <= maxMinAufgaben) {
      this.minAufgaben = minAufgaben;
    }
  }

  public void setMinZiele(int minZiele) {
    if (minZiele >= minMinZiele && minZiele <= maxMinZiele) {
      this.minZiele = minZiele;
    }
  }

  public void setNeededQualityCriteria(List<String> neededQualityCriteria) {
    this.neededQualityCriteria = neededQualityCriteria;
  }

  public void setMinStakeholder(int minStakeholder) {
    if (minStakeholder >= minMinStakeholder && minStakeholder <= maxMinStakeholder) {
      this.minStakeholder = minStakeholder;
    }
  }

  public void setMinNachhaltigkeitsziele(int minNachhaltigkeitsziele) {
    if (minNachhaltigkeitsziele >= minMinNachhaltigkeitsziele
        && minNachhaltigkeitsziele <= maxMinNachhaltigkeitsziele) {
      this.minNachhaltigkeitsziele = minNachhaltigkeitsziele;
    }
  }

  public int getMinMinAufgaben() {
    return minMinAufgaben;
  }

  public int getMaxMinAufgaben() {
    return maxMinAufgaben;
  }

  public int getMinMinZiele() {
    return minMinZiele;
  }

  public int getMaxMinZiele() {
    return maxMinZiele;
  }

  public int getMinMinStakeholder() {
    return minMinStakeholder;
  }

  public int getMaxMinStakeholder() {
    return maxMinStakeholder;
  }

  public int getMinMinNachhaltigkeitsziele() {
    return minMinNachhaltigkeitsziele;
  }

  public int getMaxMinNachhaltigkeitsziele() {
    return maxMinNachhaltigkeitsziele;
  }

  private double checkTitel() {
    String titel = Arc42DokuNameDAO.getInstance().findById(super.getArcId()).getName();
    return titel.length() < maxTitelLength ? 0.1 : 0;
  }

  private double checkAufgabenstellung() {
    return RequirementDAO.getInstance().findAll(super.getArcId()).size() >= minAufgaben ? 0.2 : 0;
  }

  private double checkQualityGoals() {
    double result = 0;
    List<QualityGoalDTO> goals = QualityGoalDAO.getInstance().findAll(super.getArcId());
    if (goals.size() >= minZiele) {
      result += 0.1;
    }
    if (goals.size() > 0 && checkQualityGoalsComplete()) {
      result += 0.1;
    }
    if (goals.size() > 0 && checkQualityCriteria()) {
      result += 0.1;
    }
    return result;
  }

  private boolean checkQualityGoalsComplete() {
    List<QualityGoalDTO> goals = QualityGoalDAO.getInstance().findAll(super.getArcId());
    if (goals.size() == 0) {
      return false;
    }
    double counterComplete = 0.0;
    for (QualityGoalDTO goal : goals) {
      if (!(goal.getMotivation() == null
          || goal.getMotivation().equals("")
          || goal.getMotivation().equals("null"))) {
        ++counterComplete;
      }
    }
    return counterComplete == goals.size();
  }

  private boolean checkQualityCriteria() {
    List<QualityGoalDTO> goals = QualityGoalDAO.getInstance().findAll(super.getArcId());
    for (String needed : neededQualityCriteria) {
      boolean isDocumented = false;
      for (QualityGoalDTO goal : goals) {
        List<String> criterias = goal.getQualityCriteria();
        for (String criteria : criterias) {
          if (criteria.equals(needed)) {
            isDocumented = true;
            break;
          }
        }
      }
      if (!isDocumented) {
        return false;
      }
    }
    return true;
  }

  private double checkStakeholder() {
    return StakeholderDAO.getInstance().findAll(super.getArcId()).size() >= minStakeholder
        ? 0.2
        : 0;
  }

  private double checkNachhaltigkeitsziele() {
    double result = 0;
    if (NachhaltigkeitszieleDAO.getInstance().findAllByArcId(super.getArcId()).size()
        >= minNachhaltigkeitsziele) {
      result += 0.1;
    }
    if (checkNachhaltigkeitszieleComplete()) {
      result += 0.1;
    }
    return result;
  }

  private boolean checkNachhaltigkeitszieleComplete() {
    List<NachhaltigkeitszieleDTO> ziele =
        NachhaltigkeitszieleDAO.getInstance().findAllByArcId(super.getArcId());
    if (ziele.size() == 0 && minNachhaltigkeitsziele != 0) {
      return false;
    }
    double counterComplete = 0.0;
    for (NachhaltigkeitszieleDTO ziel : ziele) {
      if (!ziel.getMotivation().equals("")
          && !ziel.getPrio().equals("null")
          && !ziel.getSaving().equals("null")) {
        ++counterComplete;
      }
    }
    return counterComplete == ziele.size();
  }
}
