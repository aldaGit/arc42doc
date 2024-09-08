package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.LoesungsStrategieDTO;
import org.arc42.dokumentation.model.dto.documentation.NachhaltigkeitszieleDTO;
import org.arc42.dokumentation.model.dto.documentation.QualityDTO;
import org.arc42.dokumentation.model.dto.documentation.QualityGoalDTO;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.arc42.dokumentation.view.util.data.NotificationType;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class LoesungsStrategieDAO extends ARC42DAOAbstract<LoesungsStrategieDTO, String> {

  private static LoesungsStrategieDAO instance;

  private LoesungsStrategieDAO() {
    super();
  }

  public static LoesungsStrategieDAO getInstance() {
    if (instance == null) {
      instance = new LoesungsStrategieDAO();
    }
    return instance;
  }

  @Override
  public LoesungsStrategieDTO save(LoesungsStrategieDTO loesungsstrategieDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + " create (a)-[r:hatLoesung]->(l:LoesungsStrategie {strategy:$strategy,"
                          + " qgId:$qgId})"
                          + System.lineSeparator()
                          + "return Id(l), l.strategy. l.qgId",
                      parameters(
                          "id",
                          arcId,
                          "strategy",
                          loesungsstrategieDTO.getStrategy(),
                          "qgId",
                          loesungsstrategieDTO.getQualityGoalId()));
              LoesungsStrategieDTO dto = null;
              if (result != null) {
                Record record = result.single();
                dto = new LoesungsStrategieDTO(record.get("l.strategy").asString());
                dto.setId(String.valueOf(record.get("Id(l)")));
              }
              return dto;
            });
      }
    }
    return null;
  }

  public LoesungsStrategieDTO save(
      LoesungsStrategieDTO loesungsStrategieDTO, NachhaltigkeitszieleDTO nachhaltigkeitszieleDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        new NotificationWindow(
            "Speichermethode aufgerufen!!", NotificationType.SHORT, NotificationType.SUCCESS);
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id create"
                          + " (a)-[r:hatLoesung]->(l:LoesungsStrategie {strategy:$strategy,"
                          + " nId:$nId, qgId:$qgId}) return Id(l), l.strategy, l.nId, l.qgId",
                      parameters(
                          "id",
                          arcId,
                          "strategy",
                          loesungsStrategieDTO.getStrategy(),
                          "nId",
                          nachhaltigkeitszieleDTO.getId(),
                          "qgId",
                          loesungsStrategieDTO.getQualityGoalId()));
              LoesungsStrategieDTO dto = null;
              if (result != null) {
                Record record = result.single();
                dto =
                    new LoesungsStrategieDTO(
                        record.get("l.strategy").asString(),
                        record.get("l.nId").asString(),
                        record.get("l.qgId").asString());
                dto.setId(String.valueOf(record.get("Id(l)")));
              }
              return dto;
            });
      }
    }
    return null;
  }

  public LoesungsStrategieDTO save(
      LoesungsStrategieDTO loesungsStrategieDTO, QualityDTO qualityDTO) {
    try (Session session = getDriver().session()) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id create"
                          + " (a)-[r:hatLoesung]->(l:LoesungsStrategie {strategy:$strategy,"
                          + " qgId:$qgId}) return Id(l), l.strategy, l.qgId",
                      parameters(
                          "id",
                          arcId,
                          "strategy",
                          loesungsStrategieDTO.getStrategy(),
                          "qgId",
                          qualityDTO.getId()));
              LoesungsStrategieDTO dto = null;
              if (result != null) {
                Record record = result.single();
                dto =
                    new LoesungsStrategieDTO(
                        record.get("l.strategy").asString(),
                        record.get("l.nId").asString(),
                        record.get("l.qgId").asString());
                dto.setId(String.valueOf(record.get("Id(l)")));
              }
              return dto;
            });
      }
    }
    return null;
  }

  public LoesungsStrategieDTO findByNachhaltigkeitsziel(
      NachhaltigkeitszieleDTO nachhaltigkeitszieleDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      if (nachhaltigkeitszieleDTO.getId() != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(l:LoesungsStrategie) where l.nId=$nId"
                          + System.lineSeparator()
                          + "return Id(l), l.strategy, l.nId",
                      parameters("nId", nachhaltigkeitszieleDTO.getId()));
              LoesungsStrategieDTO dto = null;
              if (result != null) {
                Record record = (result.hasNext()) ? result.single() : null;
                if (record != null) {
                  dto =
                      new LoesungsStrategieDTO(
                          record.get("l.strategy").asString(),
                          record.get("l.nId").asString(),
                          record.get("l.qgId").asString());
                  dto.setId(String.valueOf(record.get("Id(l)")));
                } else {
                  dto = new LoesungsStrategieDTO("");
                }
              }
              return dto;
            });
      }
    }
    return null;
  }

  public LoesungsStrategieDTO findByQualityGoalId(QualityGoalDTO qualityGoalDTO) {
    try (Session session = getDriver().session()) {
      if (qualityGoalDTO.getId() != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(l:LoesungsStrategie) where l.qgId=$qgId"
                          + System.lineSeparator()
                          + "return Id(l), l.strategy, l.nId, l.qgId",
                      parameters("qgId", qualityGoalDTO.getId()));
              LoesungsStrategieDTO dto = null;
              if (result != null) {
                Record record = (result.hasNext()) ? result.single() : null;
                if (record != null) {
                  dto =
                      new LoesungsStrategieDTO(
                          record.get("l.strategy").asString(),
                          record.get("l.nId").asString(),
                          record.get("l.qgId").asString());
                  dto.setId(String.valueOf(record.get("Id(l)")));
                } else {
                  dto = new LoesungsStrategieDTO("");
                }
              }
              return dto;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(LoesungsStrategieDTO loesungsStrategieDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(l:LoesungsStrategie) where Id(l)=$id"
                          + System.lineSeparator()
                          + "detach delete l return count(*)",
                      parameters("id", Integer.parseInt(loesungsStrategieDTO.getId())));
              Record record = result.single();
              return record != null && !String.valueOf(record.get("count(*)")).isEmpty();
            });
      }
    }
    return true;
  }

  public void deleteByNachhaltigkeitsziel(NachhaltigkeitszieleDTO n) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(l:LoesungsStrategie) where l.nId=$id"
                          + System.lineSeparator()
                          + "detach delete l return count(*)",
                      parameters("id", n.getId()));
              Record record = result.single();
              return record != null && !String.valueOf(record.get("count(*)")).isEmpty();
            });
      }
    }
  }

  @Override
  public void update(LoesungsStrategieDTO loesungsStrategieDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(l:LoesungsStrategie) where Id(l)=$id"
                        + System.lineSeparator()
                        + "set l = {strategy:$strategy, nId:$nId, qgId:$qgId}"
                        + System.lineSeparator()
                        + "return Id(l), l.strategy, l.nId, l.qgId",
                    parameters(
                        "id",
                        Integer.parseInt(loesungsStrategieDTO.getId()),
                        "strategy",
                        loesungsStrategieDTO.getStrategy(),
                        "nId",
                        loesungsStrategieDTO.getnId(),
                        "qgId",
                        loesungsStrategieDTO.getQualityGoalId()));
            LoesungsStrategieDTO dto = null;
            if (result != null) {
              Record record = result.single();
              dto =
                  new LoesungsStrategieDTO(
                      record.get("l.strategy").toString(),
                      record.get("l.nId").asString(),
                      record.get("l.qgId").asString());
              dto.setId(String.valueOf(record.get("Id(l)")));
            }
            return dto;
          });
    }
  }

  @Override
  public List<LoesungsStrategieDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(arcId.toString());
  }

  private List<LoesungsStrategieDTO> findAllByArcId(String arcIdString) {
    if (arcIdString != null && !arcIdString.isEmpty()) {
      Integer arcId = Integer.parseInt(arcIdString);
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(l:LoesungsStrategie)"
                          + System.lineSeparator()
                          + "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "match (a)-[r:hatLoesung]->(l) return Id(l), l.strategy, l.nId ,l.qgId",
                      parameters("id", arcId));
              return result.list(
                  r -> {
                    LoesungsStrategieDTO dto;
                    dto =
                        new LoesungsStrategieDTO(
                            r.get("l.strategy").asString(),
                            r.get("l.nId").asString(),
                            r.get("l.qgId").asString());
                    dto.setId(String.valueOf(r.get("Id(l)")));
                    return dto;
                  });
            });
      }
    }
    return new ArrayList<>();
  }

  @Override
  public LoesungsStrategieDTO findById(String id) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      if (id != null) {
        Integer idI = Integer.parseInt(id);
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "match (a)-[r:hatLoesung]->(l:LoesungsStrategie)"
                          + System.lineSeparator()
                          + "return Id(l), l.strategy",
                      parameters("id", idI));
              LoesungsStrategieDTO dto = null;
              if (result != null) {
                Record record = (result.hasNext()) ? result.single() : null;
                if (record != null) {
                  dto = new LoesungsStrategieDTO(record.get("l.strategy").asString());
                } else {
                  dto = new LoesungsStrategieDTO("");
                }
              }
              return dto;
            });
      }
    }
    return null;
  }

  public boolean createRelationship(
      LoesungsStrategieDTO loesungsStrategieDTO, QualityDTO qualityDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      if (loesungsStrategieDTO.getId() != null && qualityDTO.getId() != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(l:LoesungsStrategie) where Id(l)=$lId"
                          + System.lineSeparator()
                          + "match(q:Qualitaetsziel) where Id(q)=$qId"
                          + System.lineSeparator()
                          + "create (l)-[r:isSolutionTo]->(q)"
                          + System.lineSeparator()
                          + "return Id(l), Id(q)",
                      parameters("lId", loesungsStrategieDTO.getId(), "qId", qualityDTO.getId()));
              if (result != null) {
                Record record = result.single();
                return !(record.get("Id(l)").isEmpty() || record.get("Id(q)").isEmpty());
              }
              return false;
            });
      }
    }
    return false;
  }
}
