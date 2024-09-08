package org.arc42.analyse.model.evaluation.dao;

import static org.arc42.analyse.model.util.ResultChecker.resultNotEmpty;
import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.analyse.model.evaluation.regelsaetze.LoesungsstrategieRegelsatz;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class LoesungsstrategieRegelsatzDAO
    extends ARC42DAOAbstract<LoesungsstrategieRegelsatz, String> {

  private static LoesungsstrategieRegelsatzDAO instance;

  private LoesungsstrategieRegelsatzDAO() {
    super();
  }

  public static LoesungsstrategieRegelsatzDAO getInstance() {
    if (instance == null) {
      instance = new LoesungsstrategieRegelsatzDAO();
    }
    return instance;
  }

  @Override
  public LoesungsstrategieRegelsatz save(LoesungsstrategieRegelsatz loesungsstrategieRegelsatz) {
    try (Session session = getDriver().session()) {
      if (loesungsstrategieRegelsatz.getArcId() != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "create (a)-[r:hasLSTRRegelsatz]->(e:LoesungsstrategieRegelsatz"
                          + "{arcId:$arcId, gewichtung:$gewichtung, sollMeetings:$sollMeetings})"
                          + System.lineSeparator()
                          + "return e.arcId, Id(e), e.gewichtung, e.sollMeetings",
                      parameters(
                          "id",
                          Integer.valueOf(loesungsstrategieRegelsatz.getArcId()),
                          "arcId",
                          loesungsstrategieRegelsatz.getArcId(),
                          "gewichtung",
                          loesungsstrategieRegelsatz.getGewichtung(),
                          "sollMeetings",
                          loesungsstrategieRegelsatz.getSollMeetings()));
              LoesungsstrategieRegelsatz lstrRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                lstrRegelsatz =
                    new LoesungsstrategieRegelsatz(
                        r.get("e.arcId").asString(),
                        String.valueOf(r.get("Id(e)")),
                        r.get("e.gewichtung").asDouble(),
                        r.get("e.sollMeetings").asInt());
              }
              return lstrRegelsatz;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(LoesungsstrategieRegelsatz loesungsstrategieRegelsatz) {
    return null;
  }

  @Override
  public void update(LoesungsstrategieRegelsatz loesungsstrategieRegelsatz) {
    try (Session session = getDriver().session()) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:LoesungsstrategieRegelsatz) where Id(a)=$id"
                        + System.lineSeparator()
                        + "set a = {arcId:$arcId, gewichtung:$gewichtung,"
                        + " sollMeetings:$sollMeetings}"
                        + System.lineSeparator()
                        + " return Id(a)",
                    parameters(
                        "id",
                        Integer.valueOf(loesungsstrategieRegelsatz.getId()),
                        "arcId",
                        loesungsstrategieRegelsatz.getArcId(),
                        "gewichtung",
                        loesungsstrategieRegelsatz.getGewichtung(),
                        "sollMeetings",
                        loesungsstrategieRegelsatz.getSollMeetings()));
            return result;
          });
    }
  }

  @Override
  public List<LoesungsstrategieRegelsatz> findAll(String url) {
    return null;
  }

  @Override
  public LoesungsstrategieRegelsatz findById(String id) {
    return null;
  }

  public LoesungsstrategieRegelsatz findByArcId(String arcId) {
    try (Session session = getDriver().session()) {
      if (arcId != null && !arcId.isEmpty()) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:LoesungsstrategieRegelsatz) where a.arcId=$arcId"
                          + System.lineSeparator()
                          + "return Id(a), a.arcId, a.gewichtung, a.sollMeetings",
                      parameters("arcId", arcId));
              LoesungsstrategieRegelsatz lstrRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                lstrRegelsatz =
                    new LoesungsstrategieRegelsatz(
                        r.get("a.arcId").asString(),
                        String.valueOf(r.get("Id(a)")),
                        r.get("a.gewichtung").asDouble(),
                        r.get("a.sollMeetings").asInt());
              }
              return lstrRegelsatz;
            });
      }
    }
    return null;
  }
}
