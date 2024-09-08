package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.QualityGoalDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.Value;

public class QualityGoalDAO extends ARC42DAOAbstract<QualityGoalDTO, String> {

  private static QualityGoalDAO instance;

  public static QualityGoalDAO getInstance() {
    if (instance == null) {
      instance = new QualityGoalDAO();
    }
    return instance;
  }

  private QualityGoalDAO() {
    super();
  }

  @Override
  public QualityGoalDTO save(QualityGoalDTO qualityGoalDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (d:Arc42) where Id(d)=$id"
                          + System.lineSeparator()
                          + "create (d)-[r:hasQualityGoal]->(a:Qualitaetsziel"
                          + " {qualitaetsziel:$quality, motivation: $motivation})"
                          + System.lineSeparator()
                          + "return a.qualitaetsziel, a.motivation, Id(a)",
                      parameters(
                          "id",
                          arcId,
                          "quality",
                          qualityGoalDTO.getQualitaetsziel(),
                          "motivation",
                          qualityGoalDTO.getMotivation()));
              QualityGoalDTO dto = null;
              if (result != null) {
                Record r = result.single();
                dto =
                    new QualityGoalDTO(
                        r.get("a.qualitaetsziel").asString(),
                        r.get("a.motivation").asString(),
                        qualityGoalDTO.getQualityCriteria());
                dto.setId(String.valueOf(r.get("Id(a)")));
              }

              return dto;
            });
      }
    }
    return null;
  }

  public void upadateRelationship(QualityGoalDTO qualityGoalDTO) {
    createQualityCriteria(qualityGoalDTO);

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        for (int i = 0; i < qualityGoalDTO.getQualityCriteria().size(); i++) {
          String query =
              "MATCH (a:Qualitaetsziel), (b:Qualitaetskriterium) WHERE  b.qualitaetskriterium='"
                  + qualityGoalDTO.getQualityCriteria().get(i)
                  + "'  AND id(a)="
                  + qualityGoalDTO.getId()
                  + " CREATE (a)-[r:hasQualityCriteria]->(b) RETURN type(r)";
          session.run(query);
        }
      }
    }
  }

  public QualityGoalDTO createRelationship(QualityGoalDTO qualityGoalDTO) {
    createQualityCriteria(qualityGoalDTO);
    QualityGoalDTO qualityGoalDTO1 = save(qualityGoalDTO);

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        for (int i = 0; i < qualityGoalDTO1.getQualityCriteria().size(); i++) {
          String query =
              "MATCH (a:Qualitaetsziel), (b:Qualitaetskriterium) WHERE  b.qualitaetskriterium='"
                  + qualityGoalDTO1.getQualityCriteria().get(i)
                  + "'  AND id(a)="
                  + qualityGoalDTO1.getId()
                  + " CREATE (a)-[r:hasQualityCriteria]->(b) RETURN type(r)";
          session.run(query);
        }
      }
    }
    return qualityGoalDTO1;
  }

  public void deleteRelationship(QualityGoalDTO qualityGoalDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Qualitaetsziel) where Id(a)=$id"
                        + System.lineSeparator()
                        + "match(d:Arc42) match (a)<-[r:hasQualityGoal]-(d)"
                        + System.lineSeparator()
                        + "match (e)<-[n:hasQualityCriteria]-(a)"
                        + System.lineSeparator()
                        + "delete n"
                        + System.lineSeparator()
                        + "return count(n)",
                    parameters("id", Integer.parseInt(qualityGoalDTO.getId())));
            Record r = result.single();
            return r != null && !String.valueOf(r.get("count(*)")).isEmpty();
          });
    }
  }

  @Override
  public Boolean delete(QualityGoalDTO qualityGoalDTO) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      boolean deletionComplete = false;
      deletionComplete =
          session.writeTransaction(
              transaction -> {
                Result result =
                    transaction.run(
                        "match(a:Qualitaetsziel) where Id(a)=$id"
                            + System.lineSeparator()
                            + "match(d:Arc42) match (a)<-[r:hasQualityGoal]-(d)"
                            + System.lineSeparator()
                            + "match (e)<-[n:hasQualityCriteria]-(a)"
                            + System.lineSeparator()
                            + "delete n,r,a"
                            + System.lineSeparator()
                            + "return count(n)",
                        parameters("id", Integer.parseInt(qualityGoalDTO.getId())));
                Record r = result.single();
                return r != null && !String.valueOf(r.get("count(*)")).isEmpty();
              });
      deletionComplete =
          session.writeTransaction(
              transaction -> {
                Result result =
                    transaction.run(
                        "match(l:LoesungsStrategie) where l.qgId=$qgId"
                            + System.lineSeparator()
                            + "detach delete l"
                            + System.lineSeparator()
                            + "return count(l)",
                        parameters("qgId", qualityGoalDTO.getId()));
                Record r = result.single();
                return r != null && !String.valueOf(r.get("count(*)")).isEmpty();
              });
      return deletionComplete;
    }
  }

  public void createQualityCriteria(QualityGoalDTO qualityGoalDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            for (int i = 0; i < qualityGoalDTO.getQualityCriteria().size(); i++) {
              transaction.run(
                  "MERGE (n:Qualitaetskriterium {qualitaetskriterium: $criteria}) RETURN id(n)",
                  parameters("criteria", qualityGoalDTO.getQualityCriteria().get(i)));
            }
            return true;
          });
    }
  }

  // Updaten der Qualitätsziele funktioniert nicht zuverlässig - Fehler von Vorgängern
  @Override
  public void update(QualityGoalDTO qualityGoalDTO) {
    deleteRelationship(qualityGoalDTO);
    upadateRelationship(qualityGoalDTO);

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Qualitaetsziel) where Id(a)=$id "
                        + System.lineSeparator()
                        + "match(c:Qualitaetskriterium)"
                        + System.lineSeparator()
                        + "match (a)-[r:hasQualityCriteria]->(c)"
                        + System.lineSeparator()
                        + "set a = {qualitaetsziel:$quality, motivation:$motif} "
                        + System.lineSeparator()
                        + "return a.qualitaetsziel, a.motivation,"
                        + " Id(a),collect(c.qualitaetskriterium) AS criteria",
                    parameters(
                        "id",
                        Integer.parseInt(qualityGoalDTO.getId()),
                        "quality",
                        qualityGoalDTO.getQualitaetsziel(),
                        "motif",
                        qualityGoalDTO.getMotivation()));
            QualityGoalDTO dto = null;
            if (result != null) {
              Record r = result.single();
              dto =
                  new QualityGoalDTO(
                      r.get("a.qualitaetsziel").asString(),
                      r.get("a.motivation").asString(),
                      r.get("criteria").asList(Value::asString));
              dto.setId(String.valueOf(r.get("Id(a)")));
            }
            return dto;
          });
    }
  }

  @Override
  public List<QualityGoalDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(arcId.toString());
  }

  public List<QualityGoalDTO> findAllByArcId(String arcIdString) {
    if (arcIdString != null && !arcIdString.isEmpty()) {
      Integer arcId = Integer.parseInt(arcIdString);

      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Qualitaetsziel)"
                          + System.lineSeparator()
                          + "match(c:Qualitaetskriterium)"
                          + System.lineSeparator()
                          + "match(d:Arc42) where Id(d)=$id AND (d)-[:hasQualityGoal]->(a)"
                          + System.lineSeparator()
                          + "match (a)-[r:hasQualityCriteria]->(c) "
                          + System.lineSeparator()
                          + "return a.qualitaetsziel, a.motivation,"
                          + " Id(a),collect(c.qualitaetskriterium) AS criteria",
                      parameters("id", arcId));
              return result.list(
                  r -> {
                    QualityGoalDTO qualityGoalDTO =
                        new QualityGoalDTO(
                            r.get("a.qualitaetsziel").asString(),
                            r.get("a.motivation").asString(),
                            r.get("criteria").asList(Value::asString));
                    qualityGoalDTO.setId(String.valueOf(r.get("Id(a)")));
                    return qualityGoalDTO;
                  });
            });
      }
    }
    return new ArrayList<>();
  }

  @Override
  public QualityGoalDTO findById(String id) {
    if (id != null && !id.isEmpty()) {
      Integer idI = Integer.parseInt(id);
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:Qualitaetsziel) where Id(a)=$id return a.qualitaetsziel,"
                          + " a.motivation, Id(a)",
                      parameters("id", idI));
              QualityGoalDTO dto = null;
              if (result != null) {
                Record r = result.single();
                dto =
                    new QualityGoalDTO(
                        r.get("a.qualitaetsziel").asString(),
                        r.get("a.motivation").asString(),
                        r.get("a.categories").asList(Value::asString));
                dto.setId(String.valueOf(r.get("Id(a)")));
              }
              return dto;
            });
      }
    }
    return null;
  }
}
