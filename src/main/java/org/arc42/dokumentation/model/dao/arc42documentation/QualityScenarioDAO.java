package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.QualityGoalDTO;
import org.arc42.dokumentation.model.dto.documentation.QualityScenarioDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.Value;

public class QualityScenarioDAO extends ARC42DAOAbstract<QualityScenarioDTO, String> {

  private static QualityScenarioDAO instance;

  private QualityScenarioDAO() {
    super();
  }

  public static QualityScenarioDAO getInstance() {
    if (instance == null) {
      instance = new QualityScenarioDAO();
    }
    return instance;
  }

  @Override
  public QualityScenarioDTO save(QualityScenarioDTO qualityScenarioDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (d:Arc42) where Id(d)=$id "
                          + System.lineSeparator()
                          + "create (d)-[r:hasQualityScenario]->(a:Qualitaetsscenario"
                          + " {qualitaetsscenario:$scenario,stimulus: $stimulus,"
                          + " reaction:$reaction,response: $response,priority:$priority, risk:"
                          + " $risk})"
                          + System.lineSeparator()
                          + "return"
                          + " a.scenario,a.stimulus,a.reaction,a.response,a.priority,a.risk,Id(a)",
                      parameters(
                          "id", arcId,
                          "scenario", qualityScenarioDTO.getScenarioName(),
                          "stimulus", qualityScenarioDTO.getStimulus(),
                          "reaction", qualityScenarioDTO.getReaction(),
                          "response", qualityScenarioDTO.getResponse(),
                          "priority", qualityScenarioDTO.getCurrentPriority(),
                          "risk", qualityScenarioDTO.getRisk()));
              QualityScenarioDTO dto = null;
              if (result != null) {
                Record r = result.single();
                dto =
                    new QualityScenarioDTO(
                        qualityScenarioDTO.getQualitaetsziel(),
                        qualityScenarioDTO.getMotivation(),
                        qualityScenarioDTO.getQualityCriteria());
                dto.setResponse(r.get("a.response").asString());
                dto.setPriority(r.get("a.priority").asString());
                dto.setScenarioName(r.get("a.scenario").asString());
                dto.setRisk(r.get("a.risk").asString());
                dto.setStimulus(r.get("a.stimulus").asString());
                dto.setReaction(r.get("a.reaction").asString());
                dto.setId(String.valueOf(r.get("Id(a)")));
                if (qualityScenarioDTO.getQualityGoalDTO() != null) {
                  dto.setQualityGoalDTO(qualityScenarioDTO.getQualityGoalDTO());
                }
              }

              return dto;
            });
      }
    }
    return null;
  }

  public void upadateRelationship(QualityScenarioDTO qualityScenarioDTO) {
    createQualityCriteria(qualityScenarioDTO);

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        for (int i = 0; i < qualityScenarioDTO.getQualityCriteria().size(); i++) {
          String query =
              "MATCH (a:Qualitaetsscenario), (b:Qualitaetskriterium) WHERE  b.qualitaetskriterium='"
                  + qualityScenarioDTO.getQualityCriteria().get(i)
                  + "'  AND id(a)="
                  + qualityScenarioDTO.getId()
                  + " CREATE (a)-[r:hasQualityCriteria]->(b) RETURN type(r)";
          session.run(query);
        }
      }
    }
  }

  public QualityScenarioDTO createRelationship(QualityScenarioDTO qualityScenarioDTO) {
    createQualityCriteria(qualityScenarioDTO);
    QualityScenarioDTO qualityScenarioDTO1 = save(qualityScenarioDTO);
    if (qualityScenarioDTO1.getQualityGoalDTO() != null) {
      createScenarioRelationship(qualityScenarioDTO1);
    }

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        for (int i = 0; i < qualityScenarioDTO1.getQualityCriteria().size(); i++) {
          String query =
              "MATCH (a:Qualitaetsscenario), (b:Qualitaetskriterium) WHERE  b.qualitaetskriterium='"
                  + qualityScenarioDTO1.getQualityCriteria().get(i)
                  + "'  AND id(a)="
                  + qualityScenarioDTO1.getId()
                  + " CREATE (a)-[r:hasQualityCriteria]->(b) RETURN type(r)";
          session.run(query);
        }
      }
    }
    return qualityScenarioDTO1;
  }

  public void createScenarioRelationship(QualityScenarioDTO qualityScenarioDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        String query =
            "MATCH (a:Qualitaetsscenario), (b:Qualitaetsziel) WHERE  id(b)="
                + qualityScenarioDTO.getQualityGoalDTO().getId()
                + "  AND id(a)="
                + qualityScenarioDTO.getId()
                + " CREATE (a)-[r:konkretisiert]->(b) RETURN type(r)";
        session.run(query);
      }
    }
  }

  public void deleteRelationship(QualityScenarioDTO qualityScenarioDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Qualitaetsscenario) where Id(a)=$id"
                        + System.lineSeparator()
                        + "match(d:Arc42) match (a)<-[r:hasQualityScenario]-(d)"
                        + System.lineSeparator()
                        + "match (e)<-[n:hasQualityCriteria]-(a)"
                        + System.lineSeparator()
                        + "delete n"
                        + System.lineSeparator()
                        + "return count(n)",
                    parameters("id", Integer.parseInt(qualityScenarioDTO.getId())));
            Record r = result.single();
            return r != null && !String.valueOf(r.get("count(*)")).isEmpty();
          });
    }
  }

  @Override
  public Boolean delete(QualityScenarioDTO qualityScenarioDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Qualitaetsscenario) where Id(a)=$id"
                        + System.lineSeparator()
                        + "DETACH DELETE a"
                        + System.lineSeparator()
                        + "return count(a)",
                    parameters("id", Integer.parseInt(qualityScenarioDTO.getId())));
            Record r = result.single();
            return r != null && !String.valueOf(r.get("count(*)")).isEmpty();
          });
    }
  }

  public void createQualityCriteria(QualityScenarioDTO qualityScenarioDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            for (int i = 0; i < qualityScenarioDTO.getQualityCriteria().size(); i++) {
              transaction.run(
                  "MERGE (n:Qualitaetskriterium {qualitaetskriterium: $criteria}) RETURN id(n)",
                  parameters("criteria", qualityScenarioDTO.getQualityCriteria().get(i)));
            }
            return true;
          });
    }
  }

  @Override
  public void update(QualityScenarioDTO qualityScenarioDTO) {
    deleteRelationship(qualityScenarioDTO);
    upadateRelationship(qualityScenarioDTO);

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Qualitaetsscenario)-[r:konkretisiert]->(c:Qualitaetsziel) where"
                        + " Id(a)=$idScenario"
                        + System.lineSeparator()
                        + "match (d:Qualitaetsziel) where Id(d) = $idGoal"
                        + System.lineSeparator()
                        + "CREATE (a)-[r2:konkretisiert]->(d)"
                        + System.lineSeparator()
                        + "SET a = {priority:$prio,qualitaetsscenario:$scenario,reaction:"
                        + " $reaction,response: $response,risk:$risk,stimulus: $stimulus},r2 = r"
                        + System.lineSeparator()
                        + "WITH r,a"
                        + System.lineSeparator()
                        + "DELETE r"
                        + System.lineSeparator()
                        + "return"
                        + " a.qualitaetsscenario,a.stimulus,a.reaction,a.response,a.priority,a.risk,Id(a)",
                    parameters(
                        "idScenario",
                        Integer.parseInt(qualityScenarioDTO.getId()),
                        "idGoal",
                        qualityScenarioDTO.getQualityGoalDTO().getId(),
                        "prio",
                        qualityScenarioDTO.getCurrentPriority(),
                        "scenario",
                        qualityScenarioDTO.getScenarioName(),
                        "reaction",
                        qualityScenarioDTO.getReaction(),
                        "response",
                        qualityScenarioDTO.getResponse(),
                        "risk",
                        qualityScenarioDTO.getRisk(),
                        "stimulus",
                        qualityScenarioDTO.getStimulus()));
            QualityScenarioDTO dto = null;
            if (result != null) {
              Record r = result.single();
              dto =
                  new QualityScenarioDTO(
                      r.get("a.qualitaetsscenario").asString(),
                      r.get("a.stimulus").asString(),
                      null);
              dto.setId(String.valueOf(r.get("Id(a)")));
            }
            return dto;
          });
    }
  }

  @Override
  public List<QualityScenarioDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(arcId.toString());
  }

  public List<QualityScenarioDTO> findAllByArcId(String arcIdString) {
    if (arcIdString != null && !arcIdString.isEmpty()) {
      Integer arcId = Integer.parseInt(arcIdString);

      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match"
                          + " (a:Arc42)-[:hasQualityScenario]->(b:Qualitaetsscenario)-[:hasQualityCriteria]->(d:Qualitaetskriterium)"
                          + " where id(a)=$id"
                          + System.lineSeparator()
                          + "optional match"
                          + " (b:Qualitaetsscenario)-[:konkretisiert]->(c:Qualitaetsziel)-[:hasQualityCriteria]->(d:Qualitaetskriterium)"
                          + System.lineSeparator()
                          + "return"
                          + " b.qualitaetsscenario,b.stimulus,b.reaction,b.response,b.priority,b.risk,Id(b)"
                          + " as idScenario,collect(d.qualitaetskriterium) AS criteria,Id(c) as"
                          + " idGoal,c.motivation,c.qualitaetsziel",
                      parameters("id", arcId));
              return result.list(
                  r -> {
                    QualityScenarioDTO qualityScenarioDTO =
                        new QualityScenarioDTO(
                            r.get("a.qualitaetsziel").asString(),
                            r.get("a.motivation").asString(),
                            r.get("criteria").asList());
                    qualityScenarioDTO.setId(String.valueOf(r.get("idScenario")));
                    qualityScenarioDTO.setResponse(r.get("b.response").asString());
                    qualityScenarioDTO.setPriority(r.get("b.priority").asString());
                    qualityScenarioDTO.setScenarioName(r.get("b.qualitaetsscenario").asString());
                    qualityScenarioDTO.setRisk(r.get("b.risk").asString());
                    qualityScenarioDTO.setStimulus(r.get("b.stimulus").asString());
                    qualityScenarioDTO.setReaction(r.get("b.reaction").asString());
                    qualityScenarioDTO.setQualityCriteria(r.get("criteria").asList());

                    if (!r.get("idGoal").isNull()) {
                      qualityScenarioDTO.setQualityGoalDTO(
                          new QualityGoalDTO(
                              r.get("c.qualitaetsziel").asString(),
                              r.get("c.motivation").asString(),
                              r.get("criteria").asList(Value::asString)));

                      qualityScenarioDTO
                          .getQualityGoalDTO()
                          .setId(String.valueOf(r.get("idGoal").asInt()));
                    }

                    return qualityScenarioDTO;
                  });
            });
      }
    }
    return new ArrayList<>();
  }

  @Override
  public QualityScenarioDTO findById(String id) {
    if (id != null && !id.isEmpty()) {
      Integer idI = Integer.parseInt(id);
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:Qualitaetsscenario) where Id(a)=$id return a.qualitaetsziel,"
                          + " a.motivation, Id(a)",
                      parameters("id", idI));
              QualityScenarioDTO dto = null;
              if (result != null) {
                Record r = result.single();
                dto =
                    new QualityScenarioDTO(
                        r.get("a.qualitaetsziel").asString(),
                        r.get("a.motivation").asString(),
                        r.get("a.categories").asList());
                dto.setId(String.valueOf(r.get("Id(a)")));
              }
              return dto;
            });
      }
    }
    return null;
  }
}
