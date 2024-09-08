package org.arc42.analyse.model.evaluation.dao;

import static org.arc42.analyse.model.util.ResultChecker.resultNotEmpty;
import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.analyse.model.evaluation.regelsaetze.KonzepteRegelsatz;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class KonzepteRegelsatzDAO extends ARC42DAOAbstract<KonzepteRegelsatz, String> {

  private static KonzepteRegelsatzDAO instance;

  private KonzepteRegelsatzDAO() {
    super();
  }

  public static KonzepteRegelsatzDAO getInstance() {
    if (instance == null) {
      instance = new KonzepteRegelsatzDAO();
    }
    return instance;
  }

  @Override
  public KonzepteRegelsatz save(KonzepteRegelsatz konzepteRegelsatz) {
    try (Session session = getDriver().session()) {
      if (konzepteRegelsatz.getArcId() != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "create (a)-[r:hasKRegelsatz]->(e:KonzepteRegelsatz{arcId:$arcId,"
                          + " gewichtung:$gewichtung,"
                          + " neededConceptCategories:$neededConceptCategories})"
                          + System.lineSeparator()
                          + "return e.arcId, Id(e), e.gewichtung, e.neededConceptCategories",
                      parameters(
                          "id",
                          Integer.valueOf(konzepteRegelsatz.getArcId()),
                          "arcId",
                          konzepteRegelsatz.getArcId(),
                          "gewichtung",
                          konzepteRegelsatz.getGewichtung(),
                          "neededConceptCategories",
                          konzepteRegelsatz.getNeededConceptCategories()));
              KonzepteRegelsatz kRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                List<Object> neededConceptCategories = r.get("e.neededConceptCategories").asList();
                List<String> neededConceptCategoriesAsStrings =
                    neededConceptCategories.stream().map(Object::toString).toList();
                kRegelsatz =
                    new KonzepteRegelsatz(
                        r.get("e.arcId").asString(),
                        String.valueOf(r.get("Id(e)")),
                        r.get("e.gewichtung").asDouble(),
                        neededConceptCategoriesAsStrings);
              }
              return kRegelsatz;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(KonzepteRegelsatz konzepteRegelsatz) {
    return null;
  }

  @Override
  public void update(KonzepteRegelsatz konzepteRegelsatz) {
    try (Session session = getDriver().session()) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:KonzepteRegelsatz) where Id(a)=$id"
                        + System.lineSeparator()
                        + "set a = {arcId:$arcId, gewichtung:$gewichtung,"
                        + " neededConceptCategories:$neededConceptCategories}"
                        + System.lineSeparator()
                        + " return Id(a)",
                    parameters(
                        "id",
                        Integer.valueOf(konzepteRegelsatz.getId()),
                        "arcId",
                        konzepteRegelsatz.getArcId(),
                        "gewichtung",
                        konzepteRegelsatz.getGewichtung(),
                        "neededConceptCategories",
                        konzepteRegelsatz.getNeededConceptCategories()));
            return result;
          });
    }
  }

  @Override
  public List<KonzepteRegelsatz> findAll(String url) {
    return null;
  }

  @Override
  public KonzepteRegelsatz findById(String id) {
    return null;
  }

  public KonzepteRegelsatz findByArcId(String arcId) {
    try (Session session = getDriver().session()) {
      if (arcId != null && !arcId.isEmpty()) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:KonzepteRegelsatz) where a.arcId=$arcId"
                          + System.lineSeparator()
                          + "return Id(a), a.arcId, a.gewichtung, a.neededConceptCategories",
                      parameters("arcId", arcId));
              KonzepteRegelsatz kRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                List<Object> neededConceptCategories = r.get("a.neededConceptCategories").asList();
                List<String> neededConceptCategoriesAsStrings =
                    neededConceptCategories.stream().map(Object::toString).toList();
                kRegelsatz =
                    new KonzepteRegelsatz(
                        r.get("a.arcId").asString(),
                        String.valueOf(r.get("Id(a)")),
                        r.get("a.gewichtung").asDouble(),
                        neededConceptCategoriesAsStrings);
              }
              return kRegelsatz;
            });
      }
    }
    return null;
  }
}
