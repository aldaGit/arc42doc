package org.arc42.analyse.model.evaluation.dao;

import static org.arc42.analyse.model.util.ResultChecker.resultNotEmpty;
import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.analyse.model.evaluation.regelsaetze.KontextRegelsatz;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class KontextRegelsatzDAO extends ARC42DAOAbstract<KontextRegelsatz, String> {

  private static KontextRegelsatzDAO instance;

  private KontextRegelsatzDAO() {
    super();
  }

  public static KontextRegelsatzDAO getInstance() {
    if (instance == null) {
      instance = new KontextRegelsatzDAO();
    }
    return instance;
  }

  @Override
  public KontextRegelsatz save(KontextRegelsatz kontextRegelsatz) {
    try (Session session = getDriver().session()) {
      if (kontextRegelsatz.getArcId() != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "create (a)-[r:hasKARegelsatz]->(e:KontextabgrenzungRegelsatz"
                          + "{arcId:$arcId, gewichtung:$gewichtung, sollFachlich:$sollFachlich, "
                          + "sollTechnisch:$sollTechnisch})"
                          + System.lineSeparator()
                          + "return e.arcId, Id(e), e.gewichtung, e.sollFachlich, e.sollTechnisch",
                      parameters(
                          "id",
                          Integer.valueOf(kontextRegelsatz.getArcId()),
                          "arcId",
                          kontextRegelsatz.getArcId(),
                          "gewichtung",
                          kontextRegelsatz.getGewichtung(),
                          "sollFachlich",
                          kontextRegelsatz.getSollFachlich(),
                          "sollTechnisch",
                          kontextRegelsatz.getSollTechnisch()));
              KontextRegelsatz kRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                kRegelsatz =
                    new KontextRegelsatz(
                        r.get("e.arcId").asString(),
                        String.valueOf(r.get("Id(e)")),
                        r.get("e.gewichtung").asDouble(),
                        r.get("e.sollFachlich").asInt(),
                        r.get("e.sollTechnisch").asInt());
              }
              return kRegelsatz;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(KontextRegelsatz kontextRegelsatz) {
    return null;
  }

  @Override
  public void update(KontextRegelsatz kontextRegelsatz) {
    try (Session session = getDriver().session()) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:KontextabgrenzungRegelsatz) where Id(a)=$id"
                        + System.lineSeparator()
                        + "set a = {arcId:$arcId, gewichtung:$gewichtung,"
                        + " sollFachlich:$sollFachlich, sollTechnisch:$sollTechnisch}"
                        + System.lineSeparator()
                        + " return Id(a)",
                    parameters(
                        "id",
                        Integer.valueOf(kontextRegelsatz.getId()),
                        "arcId",
                        kontextRegelsatz.getArcId(),
                        "gewichtung",
                        kontextRegelsatz.getGewichtung(),
                        "sollFachlich",
                        kontextRegelsatz.getSollFachlich(),
                        "sollTechnisch",
                        kontextRegelsatz.getSollTechnisch()));
            return result;
          });
    }
  }

  @Override
  public List<KontextRegelsatz> findAll(String url) {
    return null;
  }

  @Override
  public KontextRegelsatz findById(String id) {
    return null;
  }

  public KontextRegelsatz findByArcId(String arcId) {
    try (Session session = getDriver().session()) {
      if (arcId != null && !arcId.isEmpty()) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:KontextabgrenzungRegelsatz) where a.arcId=$arcId"
                          + System.lineSeparator()
                          + "return Id(a), a.arcId, a.gewichtung, "
                          + "a.sollFachlich, a.sollTechnisch",
                      parameters("arcId", arcId));
              KontextRegelsatz kRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                kRegelsatz =
                    new KontextRegelsatz(
                        r.get("a.arcId").asString(),
                        String.valueOf(r.get("Id(a)")),
                        r.get("a.gewichtung").asDouble(),
                        r.get("a.sollFachlich").asInt(),
                        r.get("a.sollTechnisch").asInt());
              }
              return kRegelsatz;
            });
      }
    }
    return null;
  }
}
