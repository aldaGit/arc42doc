package org.arc42.analyse.model.evaluation;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.arc42.analyse.model.evaluation.regelsaetze.*;

public class Regelwerk implements RegelsatzI {

  private final String arcId;
  private LinkedHashMap<String, SingleRegelsatzI> regelsaetze;

  public Regelwerk(String arcId) {
    this.arcId = arcId;
    createDefaultRegelsaetze();
  }

  public Regelwerk(String arcId, LinkedHashMap<String, SingleRegelsatzI> regelsaetze) {
    this.arcId = arcId;
    this.regelsaetze = regelsaetze;
  }

  /**
   * Returns the total evaluation result based on the results of the single tabs and their
   * individual weighting
   */
  @Override
  public EvaluationResultI evaluate() {
    double result = 0;
    Collection<SingleRegelsatzI> regelsaetze = this.regelsaetze.values();
    for (SingleRegelsatzI regelsatz : regelsaetze) {
      if (regelsatz != null) result += regelsatz.evaluate().getResult() * regelsatz.getGewichtung();
    }
    return new EvaluationResult(result, TabGlossar.GESAMT);
  }

  @Override
  public String getArcId() {
    return arcId;
  }

  /** instantiates the 12 default rulesets and adds them to regelsaetze HashMap */
  private void createDefaultRegelsaetze() {
    regelsaetze = new LinkedHashMap<>();
    regelsaetze.put(TabGlossar.EINFUEHRUNG, new EinfuehrungRegelsatz(arcId));
    regelsaetze.put(TabGlossar.RANDBEDINGUNGEN, new RandbedingungenRegelsatz(arcId));
    regelsaetze.put(TabGlossar.KONTEXTABGRENZUNG, new KontextRegelsatz(arcId));
    regelsaetze.put(TabGlossar.LOESUNGSSTRATEGIE, new LoesungsstrategieRegelsatz(arcId));
    regelsaetze.put(TabGlossar.BAUSTEINSICHT, new BausteinsichtRegelsatz(arcId));
    regelsaetze.put(TabGlossar.LAUFZEITSICHT, new LaufzeitsichtRegelsatz(arcId));
    regelsaetze.put(TabGlossar.VERTEILUNGSSICHT, new VerteilungssichtRegelsatz(arcId));
    regelsaetze.put(TabGlossar.KONZEPTE, new KonzepteRegelsatz(arcId));
    regelsaetze.put(TabGlossar.ENTWURFSENTSCHEIDUNGEN, new EntwurfsentscheidungenRegelsatz(arcId));
    regelsaetze.put(TabGlossar.QUALITAETSSZENARIEN, new QualitaetsszenarienRegelsatz(arcId));
    regelsaetze.put(TabGlossar.RISIKEN, new RisikenRegelsatz(arcId));
    regelsaetze.put(TabGlossar.GLOSSAR, new GlossarRegelsatz(arcId));
  }

  /** returns all regelsaetze as HashMap with Tab as key */
  public HashMap<String, SingleRegelsatzI> getRegelsaetze() {
    return regelsaetze;
  }

  /** returns regelsatz for given tabname */
  public SingleRegelsatzI getParticularRegelsatz(String name) {
    return regelsaetze.get(name);
  }

  /** returns ruleset for Einführung und Ziele */
  public EinfuehrungRegelsatz getEinfuehrungRegelsatz() {
    return (EinfuehrungRegelsatz) regelsaetze.get(TabGlossar.EINFUEHRUNG);
  }

  /** returns ruleset for Randbedingungen */
  public RandbedingungenRegelsatz getRandbedingungenRegelsatz() {
    return (RandbedingungenRegelsatz) regelsaetze.get(TabGlossar.RANDBEDINGUNGEN);
  }

  /** returns ruleset for Kontextabgrenzung */
  public KontextRegelsatz getKontextRegelsatz() {
    return (KontextRegelsatz) regelsaetze.get(TabGlossar.KONTEXTABGRENZUNG);
  }

  /** returns ruleset for Loesungsstrategie */
  public LoesungsstrategieRegelsatz getLoesungsstrategieRegelsatz() {
    return (LoesungsstrategieRegelsatz) regelsaetze.get(TabGlossar.LOESUNGSSTRATEGIE);
  }

  /** returns ruleset for Bausteinsicht */
  public BausteinsichtRegelsatz getBausteinsichtRegelsatz() {
    return (BausteinsichtRegelsatz) regelsaetze.get(TabGlossar.BAUSTEINSICHT);
  }

  /** returns ruleset for Laufzeitsicht */
  public LaufzeitsichtRegelsatz getLaufzeitsichtRegelsatz() {
    return (LaufzeitsichtRegelsatz) regelsaetze.get(TabGlossar.LAUFZEITSICHT);
  }

  /** returns ruleset for Verteilungssicht */
  public VerteilungssichtRegelsatz getVerteilungssichtRegelsatz() {
    return (VerteilungssichtRegelsatz) regelsaetze.get(TabGlossar.VERTEILUNGSSICHT);
  }

  /** returns ruleset for Konzepte */
  public KonzepteRegelsatz getKonzepteRegelsatz() {
    return (KonzepteRegelsatz) regelsaetze.get(TabGlossar.KONZEPTE);
  }

  /** returns ruleset for Entwurfsentscheidungen */
  public EntwurfsentscheidungenRegelsatz getEntwurfsentscheidungenRegelsatz() {
    return (EntwurfsentscheidungenRegelsatz) regelsaetze.get(TabGlossar.ENTWURFSENTSCHEIDUNGEN);
  }

  /** returns ruleset for Qualitätsszenarien */
  public QualitaetsszenarienRegelsatz getQualitaetsszenarienRegelsatz() {
    return (QualitaetsszenarienRegelsatz) regelsaetze.get(TabGlossar.QUALITAETSSZENARIEN);
  }

  /** returns ruleset for Risiken */
  public RisikenRegelsatz getRisikenRegelsatz() {
    return (RisikenRegelsatz) regelsaetze.get(TabGlossar.RISIKEN);
  }

  /** returns ruleset for Glossar */
  public GlossarRegelsatz getGlossarRegelsatz() {
    return (GlossarRegelsatz) regelsaetze.get(TabGlossar.GLOSSAR);
  }
}
