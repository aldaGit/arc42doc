package org.arc42.analyse.model.evaluation.dao;

import static org.arc42.analyse.model.util.ResultChecker.resultNotEmpty;
import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.analyse.model.evaluation.regelsaetze.RandbedingungenRegelsatz;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class RandbedingungenRegelsatzDAO
    extends ARC42DAOAbstract<RandbedingungenRegelsatz, String> {

  private static RandbedingungenRegelsatzDAO instance;

  public RandbedingungenRegelsatzDAO() {
    super();
  }

  public static RandbedingungenRegelsatzDAO getInstance() {
    if (instance == null) {
      instance = new RandbedingungenRegelsatzDAO();
    }
    return instance;
  }

  @Override
  public RandbedingungenRegelsatz save(RandbedingungenRegelsatz randbedingungenRegelsatz) {
    try (Session session = getDriver().session()) {
      if (randbedingungenRegelsatz.getArcId() != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "create"
                          + " (a)-[r:hasRBRegelsatz]->(e:RandbedingungenRegelsatz{arcId:$arcId,"
                          + " gewichtung:$gewichtung, sollTechnisch:$sollTechnisch,"
                          + " sollOrganisatorisch:$sollOrganisatorisch,"
                          + " sollKonventionen:$sollKonventionen})"
                          + System.lineSeparator()
                          + "return e.arcId, Id(e), e.gewichtung, e.sollTechnisch,"
                          + " e.sollOrganisatorisch, e.sollKonventionen",
                      parameters(
                          "id",
                          Integer.valueOf(randbedingungenRegelsatz.getArcId()),
                          "arcId",
                          randbedingungenRegelsatz.getArcId(),
                          "gewichtung",
                          randbedingungenRegelsatz.getGewichtung(),
                          "sollTechnisch",
                          randbedingungenRegelsatz.getSollTechnisch(),
                          "sollOrganisatorisch",
                          randbedingungenRegelsatz.getSollOrgan(),
                          "sollKonventionen",
                          randbedingungenRegelsatz.getSollKonventionen()));
              RandbedingungenRegelsatz rbRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                rbRegelsatz =
                    new RandbedingungenRegelsatz(
                        r.get("e.arcId").asString(),
                        String.valueOf(r.get("Id(e)")),
                        r.get("e.gewichtung").asDouble(),
                        r.get("e.sollTechnisch").asInt(),
                        r.get("e.sollOrganisatorisch").asInt(),
                        r.get("e.sollKonventionen").asInt());
              }
              return rbRegelsatz;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(RandbedingungenRegelsatz randbedingungenRegelsatz) {
    return null;
  }

  @Override
  public void update(RandbedingungenRegelsatz randbedingungenRegelsatz) {
    try (Session session = getDriver().session()) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:RandbedingungenRegelsatz) where Id(a)=$id"
                        + System.lineSeparator()
                        + "set a = {arcId:$arcId, gewichtung:$gewichtung,"
                        + " sollTechnisch:$sollTechnisch, sollOrganisatorisch:$sollOrganisatorisch,"
                        + " sollKonventionen:$sollKonventionen}"
                        + System.lineSeparator()
                        + " return Id(a)",
                    parameters(
                        "id",
                        Integer.valueOf(randbedingungenRegelsatz.getId()),
                        "arcId",
                        randbedingungenRegelsatz.getArcId(),
                        "gewichtung",
                        randbedingungenRegelsatz.getGewichtung(),
                        "sollTechnisch",
                        randbedingungenRegelsatz.getSollTechnisch(),
                        "sollOrganisatorisch",
                        randbedingungenRegelsatz.getSollOrgan(),
                        "sollKonventionen",
                        randbedingungenRegelsatz.getSollKonventionen()));
            return result;
          });
    }
  }

  @Override
  public List<RandbedingungenRegelsatz> findAll(String url) {
    return null;
  }

  @Override
  public RandbedingungenRegelsatz findById(String id) {
    return null;
  }

  public RandbedingungenRegelsatz findByArcId(String arcId) {
    try (Session session = getDriver().session()) {
      if (arcId != null && !arcId.isEmpty()) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:RandbedingungenRegelsatz) where a.arcId=$arcId"
                          + System.lineSeparator()
                          + "return Id(a), a.arcId, a.gewichtung, "
                          + "a.sollTechnisch, a.sollOrganisatorisch, a.sollKonventionen",
                      parameters("arcId", arcId));
              RandbedingungenRegelsatz rbRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                rbRegelsatz =
                    new RandbedingungenRegelsatz(
                        r.get("a.arcId").asString(),
                        String.valueOf(r.get("Id(a)")),
                        r.get("a.gewichtung").asDouble(),
                        r.get("a.sollTechnisch").asInt(),
                        r.get("a.sollOrganisatorisch").asInt(),
                        r.get("a.sollKonventionen").asInt());
              }
              return rbRegelsatz;
            });
      }
    }
    return null;
  }
}
