package org.arc42.analyse.model.evaluation.dao;

import static org.arc42.analyse.model.util.ResultChecker.resultNotEmpty;
import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.analyse.model.evaluation.regelsaetze.EntwurfsentscheidungenRegelsatz;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class EntwurfsentscheidungenRegelsatzDAO
    extends ARC42DAOAbstract<EntwurfsentscheidungenRegelsatz, String> {

  private static EntwurfsentscheidungenRegelsatzDAO instance;

  public EntwurfsentscheidungenRegelsatzDAO() {
    super();
  }

  public static EntwurfsentscheidungenRegelsatzDAO getInstance() {
    if (instance == null) {
      instance = new EntwurfsentscheidungenRegelsatzDAO();
    }
    return instance;
  }

  @Override
  public EntwurfsentscheidungenRegelsatz save(
      EntwurfsentscheidungenRegelsatz entwurfsentscheidungenRegelsatz) {
    try (Session session = getDriver().session()) {
      if (entwurfsentscheidungenRegelsatz.getArcId() != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "create (a)-[r:hasEERegelsatz]->(e:EntwurfsentscheidungenRegelsatz"
                          + "{arcId:$arcId, gewichtung:$gewichtung, minAnzahl:$minAnzahl})"
                          + System.lineSeparator()
                          + "return e.arcId, Id(e), e.gewichtung, e.minAnzahl",
                      parameters(
                          "id",
                          Integer.valueOf(entwurfsentscheidungenRegelsatz.getArcId()),
                          "arcId",
                          entwurfsentscheidungenRegelsatz.getArcId(),
                          "gewichtung",
                          entwurfsentscheidungenRegelsatz.getGewichtung(),
                          "minAnzahl",
                          entwurfsentscheidungenRegelsatz.getSollEntwurfsentscheidungen()));
              EntwurfsentscheidungenRegelsatz eeRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                eeRegelsatz =
                    new EntwurfsentscheidungenRegelsatz(
                        r.get("e.arcId").asString(),
                        String.valueOf(r.get("Id(e)")),
                        r.get("e.gewichtung").asDouble(),
                        r.get("e.minAnzahl").asInt());
              }
              return eeRegelsatz;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(EntwurfsentscheidungenRegelsatz entwurfsentscheidungenRegelsatz) {
    return null;
  }

  @Override
  public void update(EntwurfsentscheidungenRegelsatz entwurfsentscheidungenRegelsatz) {
    try (Session session = getDriver().session()) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:EntwurfsentscheidungenRegelsatz) where Id(a)=$id "
                        + "set a = {arcId:$arcId, gewichtung:$gewichtung, minAnzahl:$minAnzahl}"
                        + System.lineSeparator()
                        + "return Id(a), a.arcId, a.gewichtung, a.minAnzahl",
                    parameters(
                        "id",
                        Integer.parseInt(entwurfsentscheidungenRegelsatz.getId()),
                        "arcId",
                        entwurfsentscheidungenRegelsatz.getArcId(),
                        "gewichtung",
                        entwurfsentscheidungenRegelsatz.getGewichtung(),
                        "minAnzahl",
                        entwurfsentscheidungenRegelsatz.getSollEntwurfsentscheidungen()));
            return result;
          });
    }
  }

  @Override
  public List<EntwurfsentscheidungenRegelsatz> findAll(String url) {
    return null;
  }

  @Override
  public EntwurfsentscheidungenRegelsatz findById(String id) {
    return null;
  }

  public EntwurfsentscheidungenRegelsatz findByArcId(String arcId) {
    try (Session session = getDriver().session()) {
      if (arcId != null && !arcId.isEmpty()) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:EntwurfsentscheidungenRegelsatz) where a.arcId=$arcId"
                          + " return Id(a), a.arcId, a.gewichtung, a.minAnzahl",
                      parameters("arcId", arcId));
              if (resultNotEmpty(result)) {
                Record r = result.single();
                EntwurfsentscheidungenRegelsatz eeRegelsatz =
                    new EntwurfsentscheidungenRegelsatz(
                        r.get("a.arcId").asString(),
                        String.valueOf(r.get("Id(a)")),
                        r.get("a.gewichtung").asDouble(),
                        r.get("a.minAnzahl").asInt());
                return eeRegelsatz;
              }
              return null;
            });
      }
    }
    return null;
  }
}
