package org.arc42.analyse.model.evaluation.dao;

import static org.arc42.analyse.model.util.ResultChecker.resultNotEmpty;
import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.analyse.model.evaluation.regelsaetze.GlossarRegelsatz;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class GlossarRegelsatzDAO extends ARC42DAOAbstract<GlossarRegelsatz, String> {

  private static GlossarRegelsatzDAO instance;

  private GlossarRegelsatzDAO() {
    super();
  }

  public static GlossarRegelsatzDAO getInstance() {
    if (instance == null) {
      instance = new GlossarRegelsatzDAO();
    }
    return instance;
  }

  @Override
  public GlossarRegelsatz save(GlossarRegelsatz glossarRegelsatz) {
    try (Session session = getDriver().session()) {
      if (glossarRegelsatz.getArcId() != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "create (a)-[r:hasGlRegelsatz]->(e:GlossarRegelsatz"
                          + "{arcId:$arcId, gewichtung:$gewichtung, minWortanzahl:$minWortanzahl, "
                          + "maxWortanzahl:$maxWortanzahl, checked:$checked})"
                          + System.lineSeparator()
                          + "return e.arcId, Id(e), e.gewichtung, e.minWortanzahl, e.maxWortanzahl,"
                          + " e.checked",
                      parameters(
                          "id",
                          Integer.valueOf(glossarRegelsatz.getArcId()),
                          "arcId",
                          glossarRegelsatz.getArcId(),
                          "gewichtung",
                          glossarRegelsatz.getGewichtung(),
                          "minWortanzahl",
                          glossarRegelsatz.getMinWortanzahl(),
                          "maxWortanzahl",
                          glossarRegelsatz.getMaxWortanzahl(),
                          "checked",
                          glossarRegelsatz.isChecked()));
              GlossarRegelsatz glRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                glRegelsatz =
                    new GlossarRegelsatz(
                        r.get("e.arcId").asString(),
                        String.valueOf(r.get("Id(e)")),
                        r.get("e.gewichtung").asDouble(),
                        r.get("e.minWortanzahl").asInt(),
                        r.get("e.maxWortanzahl").asInt(),
                        r.get("e.checked").asBoolean());
              }
              return glRegelsatz;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(GlossarRegelsatz glossarRegelsatz) {
    return null;
  }

  @Override
  public void update(GlossarRegelsatz glossarRegelsatz) {
    try (Session session = getDriver().session()) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:GlossarRegelsatz) where Id(a)=$id set a = {arcId:$arcId,"
                        + " gewichtung:$gewichtung, minWortanzahl:$minWortanzahl,"
                        + " maxWortanzahl:$maxWortanzahl, checked:$checked}"
                        + System.lineSeparator()
                        + "return Id(a), a.arcId, a.gewichtung, a.minWortanzahl, a.maxWortanzahl,"
                        + " a.checked",
                    parameters(
                        "id",
                        Integer.parseInt(glossarRegelsatz.getId()),
                        "arcId",
                        glossarRegelsatz.getArcId(),
                        "gewichtung",
                        glossarRegelsatz.getGewichtung(),
                        "minWortanzahl",
                        glossarRegelsatz.getMinWortanzahl(),
                        "maxWortanzahl",
                        glossarRegelsatz.getMaxWortanzahl(),
                        "checked",
                        glossarRegelsatz.isChecked()));
            return result;
          });
    }
  }

  @Override
  public List<GlossarRegelsatz> findAll(String url) {
    return null;
  }

  @Override
  public GlossarRegelsatz findById(String id) {
    return null;
  }

  public GlossarRegelsatz findByArcId(String arcId) {
    try (Session session = getDriver().session()) {
      if (arcId != null && !arcId.isEmpty()) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:GlossarRegelsatz) where a.arcId=$arcId"
                          + " return Id(a), a.arcId, a.gewichtung, a.minWortanzahl, "
                          + "a.maxWortanzahl, a.checked",
                      parameters("arcId", arcId));
              if (resultNotEmpty(result)) {
                Record r = result.single();
                GlossarRegelsatz glRegelsatz =
                    new GlossarRegelsatz(
                        r.get("a.arcId").asString(),
                        String.valueOf(r.get("Id(a)")),
                        r.get("a.gewichtung").asDouble(),
                        r.get("a.minWortanzahl").asInt(),
                        r.get("a.maxWortanzahl").asInt(),
                        r.get("a.checked").asBoolean());
                return glRegelsatz;
              }
              return null;
            });
      }
    }
    return null;
  }
}
