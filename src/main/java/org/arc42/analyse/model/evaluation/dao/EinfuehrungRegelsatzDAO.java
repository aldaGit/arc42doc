package org.arc42.analyse.model.evaluation.dao;

import static org.arc42.analyse.model.util.ResultChecker.resultNotEmpty;
import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.analyse.model.evaluation.regelsaetze.EinfuehrungRegelsatz;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class EinfuehrungRegelsatzDAO extends ARC42DAOAbstract<EinfuehrungRegelsatz, String> {

  private static EinfuehrungRegelsatzDAO instance;

  public EinfuehrungRegelsatzDAO() {
    super();
  }

  public static EinfuehrungRegelsatzDAO getInstance() {
    if (instance == null) {
      instance = new EinfuehrungRegelsatzDAO();
    }
    return instance;
  }

  @Override
  public EinfuehrungRegelsatz save(EinfuehrungRegelsatz einfuehrungRegelsatz) {
    try (Session session = getDriver().session()) {
      if (einfuehrungRegelsatz.getArcId() != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "create (a)-[r:hasEZRegelsatz]->(e:EinfuehrungRegelsatz{arcId:$arcId,"
                          + " gewichtung:$gewichtung,"
                          + " maxTitelLength:$maxTitelLength,minAufgaben:$minAufgaben,"
                          + " minZiele:$minZiele,"
                          + " neededQualityCriteria:$neededQualityCriteria,minStakeholder:$minStakeholder,"
                          + " minNachhaltigkeitsziele:$minNachhaltigkeitsziele})"
                          + System.lineSeparator()
                          + "return e.arcId, Id(e), e.gewichtung, e.maxTitelLength, e.minAufgaben,"
                          + " e.minZiele,e.neededQualityCriteria, e.minStakeholder,"
                          + " e.minNachhaltigkeitsziele",
                      parameters(
                          "id",
                          Integer.valueOf(einfuehrungRegelsatz.getArcId()),
                          "arcId",
                          einfuehrungRegelsatz.getArcId(),
                          "gewichtung",
                          einfuehrungRegelsatz.getGewichtung(),
                          "maxTitelLength",
                          einfuehrungRegelsatz.getMaxTitelLength(),
                          "minAufgaben",
                          einfuehrungRegelsatz.getMinAufgaben(),
                          "minZiele",
                          einfuehrungRegelsatz.getMinZiele(),
                          "neededQualityCriteria",
                          einfuehrungRegelsatz.getNeededQualityCriteria(),
                          "minStakeholder",
                          einfuehrungRegelsatz.getMinStakeholder(),
                          "minNachhaltigkeitsziele",
                          einfuehrungRegelsatz.getMinNachhaltigkeitsziele()));
              EinfuehrungRegelsatz ezRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                List<Object> neededQualityCriteria = r.get("e.neededQualityCriteria").asList();
                List<String> neededQualityCriteriaAsStrings =
                    neededQualityCriteria.stream().map(Object::toString).toList();
                ezRegelsatz =
                    new EinfuehrungRegelsatz(
                        r.get("e.arcId").asString(),
                        String.valueOf(r.get("Id(e)")),
                        r.get("e.gewichtung").asDouble(),
                        r.get("e.maxTitelLength").asInt(),
                        r.get("e.minAufgaben").asInt(),
                        r.get("e.minZiele").asInt(),
                        neededQualityCriteriaAsStrings,
                        r.get("e.minStakeholder").asInt(),
                        r.get("e.minNachhaltigkeitsziele").asInt());
              }
              return ezRegelsatz;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(EinfuehrungRegelsatz einfuehrungRegelsatz) {
    return null;
  }

  @Override
  public void update(EinfuehrungRegelsatz einfuehrungRegelsatz) {
    try (Session session = getDriver().session()) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:EinfuehrungRegelsatz) where Id(a)=$id set a = {arcId:$arcId,"
                        + " gewichtung:$gewichtung,"
                        + " maxTitelLength:$maxTitelLength,minAufgaben:$minAufgaben,"
                        + " minZiele:$minZiele,"
                        + " neededQualityCriteria:$neededQualityCriteria,minStakeholder:$minStakeholder,"
                        + " minNachhaltigkeitsziele:$minNachhaltigkeitsziele}"
                        + System.lineSeparator()
                        + "return Id(a), a.arcId, a.gewichtung, a.maxTitelLength, a.minAufgaben,"
                        + " a.minZiele,a.neededQualityCriteria, a.minStakeholder,"
                        + " a.minNachhaltigkeitsziele",
                    parameters(
                        "id",
                        Integer.parseInt(einfuehrungRegelsatz.getId()),
                        "arcId",
                        einfuehrungRegelsatz.getArcId(),
                        "gewichtung",
                        einfuehrungRegelsatz.getGewichtung(),
                        "maxTitelLength",
                        einfuehrungRegelsatz.getMaxTitelLength(),
                        "minAufgaben",
                        einfuehrungRegelsatz.getMinAufgaben(),
                        "minZiele",
                        einfuehrungRegelsatz.getMinZiele(),
                        "neededQualityCriteria",
                        einfuehrungRegelsatz.getNeededQualityCriteria(),
                        "minStakeholder",
                        einfuehrungRegelsatz.getMinStakeholder(),
                        "minNachhaltigkeitsziele",
                        einfuehrungRegelsatz.getMinNachhaltigkeitsziele()));
            return result;
          });
    }
  }

  @Override
  public List<EinfuehrungRegelsatz> findAll(String url) {
    return null;
  }

  @Override
  public EinfuehrungRegelsatz findById(String id) {
    return null;
  }

  public EinfuehrungRegelsatz findByArcId(String arcId) {
    try (Session session = getDriver().session()) {
      if (arcId != null && !arcId.isEmpty()) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:EinfuehrungRegelsatz) where a.arcId=$arcId"
                          + " return Id(a), a.arcId, a.gewichtung, a.maxTitelLength,"
                          + "a.minAufgaben, a.minZiele, a.neededQualityCriteria, "
                          + "a.minStakeholder, a.minNachhaltigkeitsziele",
                      parameters("arcId", arcId));
              if (resultNotEmpty(result)) { // result is not always null, if no record was found
                Record r = result.single();
                List<Object> neededQualityCriteria = r.get("a.neededQualityCriteria").asList();
                List<String> neededQualityCriteriaAsStrings =
                    neededQualityCriteria.stream().map(Object::toString).toList();
                EinfuehrungRegelsatz ezRegelsatz =
                    new EinfuehrungRegelsatz(
                        r.get("a.arcId").asString(),
                        String.valueOf(r.get("Id(a)")),
                        r.get("a.gewichtung").asDouble(),
                        r.get("a.maxTitelLength").asInt(),
                        r.get("a.minAufgaben").asInt(),
                        r.get("a.minZiele").asInt(),
                        neededQualityCriteriaAsStrings,
                        r.get("a.minStakeholder").asInt(),
                        r.get("a.minNachhaltigkeitsziele").asInt());
                return ezRegelsatz;
              }
              return null;
            });
      }
    }
    return null;
  }
}
