package org.arc42.analyse.model.evaluation.dao;

import static org.arc42.analyse.model.util.ResultChecker.resultNotEmpty;
import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.analyse.model.evaluation.regelsaetze.VerteilungssichtRegelsatz;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class VerteilungssichtRegelsatzDAO
    extends ARC42DAOAbstract<VerteilungssichtRegelsatz, String> {

  private static VerteilungssichtRegelsatzDAO instance;

  private VerteilungssichtRegelsatzDAO() {
    super();
  }

  public static VerteilungssichtRegelsatzDAO getInstance() {
    if (instance == null) {
      instance = new VerteilungssichtRegelsatzDAO();
    }
    return instance;
  }

  @Override
  public VerteilungssichtRegelsatz save(VerteilungssichtRegelsatz verteilungssichtRegelsatz) {
    try (Session session = getDriver().session()) {
      if (verteilungssichtRegelsatz.getArcId() != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "create (a)-[r:hasVSRegelsatz]->(e:VerteilungssichtRegelsatz"
                          + "{arcId:$arcId, gewichtung:$gewichtung, hardware:$hardware})"
                          + System.lineSeparator()
                          + "return e.arcId, Id(e), e.gewichtung, e.hardware",
                      parameters(
                          "id",
                          Integer.valueOf(verteilungssichtRegelsatz.getArcId()),
                          "arcId",
                          verteilungssichtRegelsatz.getArcId(),
                          "gewichtung",
                          verteilungssichtRegelsatz.getGewichtung(),
                          "hardware",
                          verteilungssichtRegelsatz.getSollHardware()));
              VerteilungssichtRegelsatz vsRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                vsRegelsatz =
                    new VerteilungssichtRegelsatz(
                        r.get("e.arcId").asString(),
                        String.valueOf(r.get("Id(e)")),
                        r.get("e.gewichtung").asDouble(),
                        r.get("e.hardware").asInt());
              }
              return vsRegelsatz;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(VerteilungssichtRegelsatz verteilungssichtRegelsatz) {
    return null;
  }

  @Override
  public void update(VerteilungssichtRegelsatz verteilungssichtRegelsatz) {
    try (Session session = getDriver().session()) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:VerteilungssichtRegelsatz) where Id(a)=$id"
                        + System.lineSeparator()
                        + "set a = {arcId:$arcId, gewichtung:$gewichtung, hardware:$hardware}"
                        + System.lineSeparator()
                        + " return Id(a)",
                    parameters(
                        "id",
                        Integer.valueOf(verteilungssichtRegelsatz.getId()),
                        "arcId",
                        verteilungssichtRegelsatz.getArcId(),
                        "gewichtung",
                        verteilungssichtRegelsatz.getGewichtung(),
                        "hardware",
                        verteilungssichtRegelsatz.getSollHardware()));
            return result;
          });
    }
  }

  @Override
  public List<VerteilungssichtRegelsatz> findAll(String url) {
    return null;
  }

  @Override
  public VerteilungssichtRegelsatz findById(String id) {
    return null;
  }

  public VerteilungssichtRegelsatz findByArcId(String arcId) {
    try (Session session = getDriver().session()) {
      if (arcId != null && !arcId.isEmpty()) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:VerteilungssichtRegelsatz) where a.arcId=$arcId"
                          + System.lineSeparator()
                          + "return Id(a), a.arcId, a.gewichtung, a.hardware",
                      parameters("arcId", arcId));
              VerteilungssichtRegelsatz vsRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                vsRegelsatz =
                    new VerteilungssichtRegelsatz(
                        r.get("a.arcId").asString(),
                        String.valueOf(r.get("Id(a)")),
                        r.get("a.gewichtung").asDouble(),
                        r.get("a.hardware").asInt());
              }
              return vsRegelsatz;
            });
      }
    }
    return null;
  }
}
