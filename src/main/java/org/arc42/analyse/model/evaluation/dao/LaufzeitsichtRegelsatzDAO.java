package org.arc42.analyse.model.evaluation.dao;

import static org.arc42.analyse.model.util.ResultChecker.resultNotEmpty;
import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.analyse.model.evaluation.regelsaetze.LaufzeitsichtRegelsatz;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class LaufzeitsichtRegelsatzDAO extends ARC42DAOAbstract<LaufzeitsichtRegelsatz, String> {

  private static LaufzeitsichtRegelsatzDAO instance;

  private LaufzeitsichtRegelsatzDAO() {
    super();
  }

  public static LaufzeitsichtRegelsatzDAO getInstance() {
    if (instance == null) {
      instance = new LaufzeitsichtRegelsatzDAO();
    }
    return instance;
  }

  @Override
  public LaufzeitsichtRegelsatz save(LaufzeitsichtRegelsatz laufzeitsichtRegelsatz) {
    try (Session session = getDriver().session()) {
      if (laufzeitsichtRegelsatz.getArcId() != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "create (a)-[r:hasLSRegelsatz]->(e:LaufzeitsichtRegelsatz"
                          + "{arcId:$arcId, gewichtung:$gewichtung})"
                          + System.lineSeparator()
                          + "return e.arcId, Id(e), e.gewichtung",
                      parameters(
                          "id",
                          Integer.valueOf(laufzeitsichtRegelsatz.getArcId()),
                          "arcId",
                          laufzeitsichtRegelsatz.getArcId(),
                          "gewichtung",
                          laufzeitsichtRegelsatz.getGewichtung()));
              LaufzeitsichtRegelsatz lsRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                lsRegelsatz =
                    new LaufzeitsichtRegelsatz(
                        r.get("e.arcId").asString(),
                        String.valueOf(r.get("Id(e)")),
                        r.get("e.gewichtung").asDouble());
              }
              return lsRegelsatz;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(LaufzeitsichtRegelsatz laufzeitsichtRegelsatz) {
    return null;
  }

  @Override
  public void update(LaufzeitsichtRegelsatz laufzeitsichtRegelsatz) {
    try (Session session = getDriver().session()) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:LaufzeitsichtRegelsatz) where Id(a)=$id"
                        + System.lineSeparator()
                        + "set a = {arcId:$arcId, gewichtung:$gewichtung}"
                        + System.lineSeparator()
                        + " return Id(a)",
                    parameters(
                        "id",
                        Integer.valueOf(laufzeitsichtRegelsatz.getId()),
                        "arcId",
                        laufzeitsichtRegelsatz.getArcId(),
                        "gewichtung",
                        laufzeitsichtRegelsatz.getGewichtung()));
            return result;
          });
    }
  }

  @Override
  public List<LaufzeitsichtRegelsatz> findAll(String url) {
    return null;
  }

  @Override
  public LaufzeitsichtRegelsatz findById(String id) {
    return null;
  }

  public LaufzeitsichtRegelsatz findByArcId(String arcId) {
    try (Session session = getDriver().session()) {
      if (arcId != null && !arcId.isEmpty()) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:LaufzeitsichtRegelsatz) where a.arcId=$arcId"
                          + System.lineSeparator()
                          + "return Id(a), a.arcId, a.gewichtung",
                      parameters("arcId", arcId));
              LaufzeitsichtRegelsatz lsRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                lsRegelsatz =
                    new LaufzeitsichtRegelsatz(
                        r.get("a.arcId").asString(),
                        String.valueOf(r.get("Id(a)")),
                        r.get("a.gewichtung").asDouble());
              }
              return lsRegelsatz;
            });
      }
    }
    return null;
  }
}
