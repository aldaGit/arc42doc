package org.arc42.analyse.model.evaluation.dao;

import static org.arc42.analyse.model.util.ResultChecker.resultNotEmpty;
import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.analyse.model.evaluation.regelsaetze.QualitaetsszenarienRegelsatz;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class QualitaetsszenarienRegelsatzDAO
    extends ARC42DAOAbstract<QualitaetsszenarienRegelsatz, String> {

  private static QualitaetsszenarienRegelsatzDAO instance;

  private QualitaetsszenarienRegelsatzDAO() {
    super();
  }

  public static QualitaetsszenarienRegelsatzDAO getInstance() {
    if (instance == null) {
      instance = new QualitaetsszenarienRegelsatzDAO();
    }
    return instance;
  }

  @Override
  public QualitaetsszenarienRegelsatz save(
      QualitaetsszenarienRegelsatz qualitaetsszenarienRegelsatz) {
    try (Session session = getDriver().session()) {
      if (qualitaetsszenarienRegelsatz.getArcId() != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "create (a)-[r:hasQSRegelsatz]->(e:QualitaetsszenarienRegelsatz"
                          + "{arcId:$arcId, gewichtung:$gewichtung})"
                          + System.lineSeparator()
                          + "return e.arcId, Id(e), e.gewichtung",
                      parameters(
                          "id",
                          Integer.valueOf(qualitaetsszenarienRegelsatz.getArcId()),
                          "arcId",
                          qualitaetsszenarienRegelsatz.getArcId(),
                          "gewichtung",
                          qualitaetsszenarienRegelsatz.getGewichtung()));
              QualitaetsszenarienRegelsatz qsRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                qsRegelsatz =
                    new QualitaetsszenarienRegelsatz(
                        r.get("e.arcId").asString(),
                        String.valueOf(r.get("Id(e)")),
                        r.get("e.gewichtung").asDouble());
              }
              return qsRegelsatz;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(QualitaetsszenarienRegelsatz qualitaetsszenarienRegelsatz) {
    return null;
  }

  @Override
  public void update(QualitaetsszenarienRegelsatz qualitaetsszenarienRegelsatz) {
    try (Session session = getDriver().session()) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:QualitaetsszenarienRegelsatz) where Id(a)=$id"
                        + System.lineSeparator()
                        + "set a = {arcId:$arcId, gewichtung:$gewichtung}"
                        + System.lineSeparator()
                        + " return Id(a)",
                    parameters(
                        "id",
                        Integer.valueOf(qualitaetsszenarienRegelsatz.getId()),
                        "arcId",
                        qualitaetsszenarienRegelsatz.getArcId(),
                        "gewichtung",
                        qualitaetsszenarienRegelsatz.getGewichtung()));
            return result;
          });
    }
  }

  @Override
  public List<QualitaetsszenarienRegelsatz> findAll(String url) {
    return null;
  }

  @Override
  public QualitaetsszenarienRegelsatz findById(String id) {
    return null;
  }

  public QualitaetsszenarienRegelsatz findByArcId(String arcId) {
    try (Session session = getDriver().session()) {
      if (arcId != null && !arcId.isEmpty()) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:QualitaetsszenarienRegelsatz) where a.arcId=$arcId"
                          + System.lineSeparator()
                          + "return Id(a), a.arcId, a.gewichtung",
                      parameters("arcId", arcId));
              QualitaetsszenarienRegelsatz qsRegelsatz = null;
              if (resultNotEmpty(result)) {
                Record r = result.single();
                qsRegelsatz =
                    new QualitaetsszenarienRegelsatz(
                        r.get("a.arcId").asString(),
                        String.valueOf(r.get("Id(a)")),
                        r.get("a.gewichtung").asDouble());
              }
              return qsRegelsatz;
            });
      }
    }
    return null;
  }
}
