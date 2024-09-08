package org.arc42.analyse.model.evaluation.dao;

import static org.arc42.analyse.model.util.ResultChecker.resultNotEmpty;
import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.analyse.model.evaluation.regelsaetze.RisikenRegelsatz;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class RisikenRegelsatzDAO extends ARC42DAOAbstract<RisikenRegelsatz, String> {

  private static RisikenRegelsatzDAO instance;

  private RisikenRegelsatzDAO() {
    super();
  }

  public static RisikenRegelsatzDAO getInstance() {
    if (instance == null) {
      instance = new RisikenRegelsatzDAO();
    }
    return instance;
  }

  @Override
  public RisikenRegelsatz save(RisikenRegelsatz risikenRegelsatz) {
    try (Session session = getDriver().session()) {
      if (risikenRegelsatz.getArcId() != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "create (a)-[r:hasRNRegelsatz]->(e:RisikenRegelsatz{arcId:$arcId,"
                          + " gewichtung:$gewichtung, sollRisiken:$sollRisiken,"
                          + " sollEntries:$sollEntries})"
                          + System.lineSeparator()
                          + "return e.arcId, Id(e), e.gewichtung, e.sollRisiken, e.sollEntries",
                      parameters(
                          "id",
                          Integer.valueOf(risikenRegelsatz.getArcId()),
                          "arcId",
                          risikenRegelsatz.getArcId(),
                          "gewichtung",
                          risikenRegelsatz.getGewichtung(),
                          "sollRisiken",
                          risikenRegelsatz.getSollRisiken(),
                          "sollEntries",
                          risikenRegelsatz.getSollEntries()));
              RisikenRegelsatz rRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                rRegelsatz =
                    new RisikenRegelsatz(
                        r.get("e.arcId").asString(),
                        String.valueOf(r.get("Id(e)")),
                        r.get("e.gewichtung").asDouble(),
                        r.get("e.sollRisiken").asInt(),
                        r.get("e.sollEntries").asInt());
              }
              return rRegelsatz;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(RisikenRegelsatz risikenRegelsatz) {
    return null;
  }

  @Override
  public void update(RisikenRegelsatz risikenRegelsatz) {
    try (Session session = getDriver().session()) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:RisikenRegelsatz) where Id(a)=$id "
                        + "set a = {arcId:$arcId, gewichtung:$gewichtung, "
                        + "sollRisiken:$sollRisiken, sollEntries:$sollEntries}"
                        + System.lineSeparator()
                        + "return Id(a), a.arcId, a.gewichtung, a.sollRisiken, a.sollEntries",
                    parameters(
                        "id",
                        Integer.parseInt(risikenRegelsatz.getId()),
                        "arcId",
                        risikenRegelsatz.getArcId(),
                        "gewichtung",
                        risikenRegelsatz.getGewichtung(),
                        "sollRisiken",
                        risikenRegelsatz.getSollRisiken(),
                        "sollEntries",
                        risikenRegelsatz.getSollEntries()));
            return result;
          });
    }
  }

  @Override
  public List<RisikenRegelsatz> findAll(String url) {
    return null;
  }

  @Override
  public RisikenRegelsatz findById(String id) {
    return null;
  }

  public RisikenRegelsatz findByArcId(String arcId) {
    try (Session session = getDriver().session()) {
      if (arcId != null && !arcId.isEmpty()) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:RisikenRegelsatz) where a.arcId=$arcId"
                          + " return Id(a), a.arcId, a.gewichtung, a.sollRisiken, a.sollEntries",
                      parameters("arcId", arcId));
              if (resultNotEmpty(result)) {
                Record r = result.single();
                RisikenRegelsatz rRegelsatz =
                    new RisikenRegelsatz(
                        r.get("a.arcId").asString(),
                        String.valueOf(r.get("Id(a)")),
                        r.get("a.gewichtung").asDouble(),
                        r.get("a.sollRisiken").asInt(),
                        r.get("a.sollEntries").asInt());
                return rRegelsatz;
              }
              return null;
            });
      }
    }
    return null;
  }
}
