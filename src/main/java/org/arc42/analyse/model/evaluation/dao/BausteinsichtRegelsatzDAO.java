package org.arc42.analyse.model.evaluation.dao;

import static org.arc42.analyse.model.util.ResultChecker.resultNotEmpty;
import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.analyse.model.evaluation.regelsaetze.BausteinsichtRegelsatz;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class BausteinsichtRegelsatzDAO extends ARC42DAOAbstract<BausteinsichtRegelsatz, String> {

  private static BausteinsichtRegelsatzDAO instance;

  private BausteinsichtRegelsatzDAO() {
    super();
  }

  public static BausteinsichtRegelsatzDAO getInstance() {
    if (instance == null) {
      instance = new BausteinsichtRegelsatzDAO();
    }
    return instance;
  }

  @Override
  public BausteinsichtRegelsatz save(BausteinsichtRegelsatz bausteinsichtRegelsatz) {
    try (Session session = getDriver().session()) {
      if (bausteinsichtRegelsatz.getArcId() != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "create (a)-[r:hasBSRegelsatz]->(e:BausteinsichtRegelsatz"
                          + "{arcId:$arcId, gewichtung:$gewichtung})"
                          + System.lineSeparator()
                          + "return e.arcId, Id(e), e.gewichtung",
                      parameters(
                          "id",
                          Integer.valueOf(bausteinsichtRegelsatz.getArcId()),
                          "arcId",
                          bausteinsichtRegelsatz.getArcId(),
                          "gewichtung",
                          bausteinsichtRegelsatz.getGewichtung()));
              BausteinsichtRegelsatz bsRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                bsRegelsatz =
                    new BausteinsichtRegelsatz(
                        r.get("e.arcId").asString(),
                        String.valueOf(r.get("Id(e)")),
                        r.get("e.gewichtung").asDouble());
              }
              return bsRegelsatz;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(BausteinsichtRegelsatz bausteinsichtRegelsatz) {
    return null;
  }

  @Override
  public void update(BausteinsichtRegelsatz bausteinsichtRegelsatz) {
    try (Session session = getDriver().session()) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:BausteinsichtRegelsatz) where Id(a)=$id"
                        + System.lineSeparator()
                        + "set a = {arcId:$arcId, gewichtung:$gewichtung}"
                        + System.lineSeparator()
                        + " return Id(a)",
                    parameters(
                        "id",
                        Integer.valueOf(bausteinsichtRegelsatz.getId()),
                        "arcId",
                        bausteinsichtRegelsatz.getArcId(),
                        "gewichtung",
                        bausteinsichtRegelsatz.getGewichtung()));
            return result;
          });
    }
  }

  @Override
  public List<BausteinsichtRegelsatz> findAll(String url) {
    return null;
  }

  @Override
  public BausteinsichtRegelsatz findById(String id) {
    return null;
  }

  public BausteinsichtRegelsatz findByArcId(String arcId) {
    try (Session session = getDriver().session()) {
      if (arcId != null && !arcId.isEmpty()) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:BausteinsichtRegelsatz) where a.arcId=$arcId"
                          + System.lineSeparator()
                          + "return Id(a), a.arcId, a.gewichtung",
                      parameters("arcId", arcId));
              BausteinsichtRegelsatz bsRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                bsRegelsatz =
                    new BausteinsichtRegelsatz(
                        r.get("a.arcId").asString(),
                        String.valueOf(r.get("Id(a)")),
                        r.get("a.gewichtung").asDouble());
              }
              return bsRegelsatz;
            });
      }
    }
    return null;
  }
}
