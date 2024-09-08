package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.NachhaltigkeitszieleDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class NachhaltigkeitszieleDAO extends ARC42DAOAbstract<NachhaltigkeitszieleDTO, String> {

  private static NachhaltigkeitszieleDAO instance;

  private NachhaltigkeitszieleDAO() {
    super();
  }

  public static NachhaltigkeitszieleDAO getInstance() {
    if (instance == null) {
      instance = new NachhaltigkeitszieleDAO();
    }
    return instance;
  }

  public NachhaltigkeitszieleDTO save(NachhaltigkeitszieleDTO nachhaltigkeitszieleDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(d:Arc42) where Id(d)=$id"
                          + System.lineSeparator()
                          + "create (d)-[r:hasNachhaltigkeitsziele]->(a:Nachhaltigkeitsziele"
                          + " {greengoal:$goal, motivation: $motivation, prio:$prio,"
                          + " saving:$saving})"
                          + System.lineSeparator()
                          + "return a.greengoal, a.motivation, a.prio, a.saving, Id(a)",
                      parameters(
                          "id",
                          arcId,
                          "goal",
                          nachhaltigkeitszieleDTO.getGoal(),
                          "motivation",
                          nachhaltigkeitszieleDTO.getMotivation(),
                          "prio",
                          nachhaltigkeitszieleDTO.getPrio(),
                          "saving",
                          nachhaltigkeitszieleDTO.getSaving()));
              NachhaltigkeitszieleDTO dto = null;
              if (result != null) {
                Record record = result.single();
                dto =
                    new NachhaltigkeitszieleDTO(
                        record.get("a.goal").asString(),
                        record.get("a.motivation").asString(),
                        record.get("a.prio").asString(),
                        record.get("a.saving").asString());
                dto.setId(String.valueOf(record.get("Id(a)")));
              }
              return dto;
            });
      }
    }
    return null;
  }

  public void update(NachhaltigkeitszieleDTO nachhaltigkeitszieleDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Nachhaltigkeitsziele) where Id(a)=$id "
                        + System.lineSeparator()
                        + "set a = {greengoal:$goal, motivation:$motivation, prio:$prio,"
                        + " saving:$saving}"
                        + System.lineSeparator()
                        + "return a.greengoal, a.motivation, a.prio, a.saving, Id(a)",
                    parameters(
                        "id",
                        Integer.parseInt(nachhaltigkeitszieleDTO.getId()),
                        "goal",
                        nachhaltigkeitszieleDTO.getGoal(),
                        "motivation",
                        nachhaltigkeitszieleDTO.getMotivation(),
                        "prio",
                        nachhaltigkeitszieleDTO.getPrio(),
                        "saving",
                        nachhaltigkeitszieleDTO.getSaving()));
            NachhaltigkeitszieleDTO dto = null;
            if (result != null) {
              Record record = result.single();
              dto =
                  new NachhaltigkeitszieleDTO(
                      record.get("a.greengoal").asString(),
                      record.get("a.motivation").asString(),
                      record.get("a.prio").asString(),
                      record.get("a.saving").asString());
              dto.setId(String.valueOf(record.get("Id(a)")));
            }
            return dto;
          });
    }
  }

  public Boolean delete(NachhaltigkeitszieleDTO nachhaltigkeitszieleDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Nachhaltigkeitsziele) where Id(a)=$id"
                        + System.lineSeparator()
                        + "detach delete a return count(*)",
                    parameters("id", Integer.parseInt(nachhaltigkeitszieleDTO.getId())));
            Record record = result.single();
            return record != null && !String.valueOf(record.get("count(*)")).isEmpty();
          });
    }
  }

  public List<NachhaltigkeitszieleDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(arcId.toString());
  }

  public List<NachhaltigkeitszieleDTO> findAllByArcId(String arcIdString) {
    if (arcIdString != null && !arcIdString.isEmpty()) {
      Integer arcId = Integer.parseInt(arcIdString);

      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Nachhaltigkeitsziele)"
                          + System.lineSeparator()
                          + "match(d:Arc42) where Id(d)=$id"
                          + System.lineSeparator()
                          + "match (d)-[r:hasNachhaltigkeitsziele]->(a)"
                          + System.lineSeparator()
                          + "return Id(a), a.greengoal, a.motivation, a.prio, a.saving",
                      parameters("id", arcId));
              return result.list(
                  r -> {
                    NachhaltigkeitszieleDTO nachhaltigkeitszieleDTO =
                        new NachhaltigkeitszieleDTO(
                            r.get("a.greengoal").asString(),
                            r.get("a.motivation").asString(),
                            r.get("a.prio").asString(),
                            r.get("a.saving").asString());
                    nachhaltigkeitszieleDTO.setId(String.valueOf(r.get("Id(a)")));
                    return nachhaltigkeitszieleDTO;
                  });
            });
      }
    }
    return new ArrayList<>();
  }

  public NachhaltigkeitszieleDTO findById(String id) {

    if (id != null && !id.isEmpty()) {
      Integer idI = Integer.parseInt(id);
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:Nachhaltigkeitsziele) where Id(a)=$id"
                          + System.lineSeparator()
                          + "return  Id(a), a.greengoal, a.motivation, a.prio, a.saving",
                      parameters("id", idI));
              NachhaltigkeitszieleDTO dto = null;
              if (result != null) {
                Record record = result.single();
                dto =
                    new NachhaltigkeitszieleDTO(
                        record.get("a.greengoal").asString(),
                        record.get("a.motivation").asString(),
                        record.get("a.prio").asString(),
                        record.get("a.saving").asString());
                dto.setId(String.valueOf(record.get("Id(a)")));
              }
              return dto;
            });
      }
    }
    return null;
  }

  public NachhaltigkeitszieleDTO findByGoal(String goal) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "MATCH (a:Nachhaltigkeitsziele) MATCH(d:Arc42) WHERE Id(d)=$id MATCH"
                          + " (d)-[r:hasNachhaltigkeitsziele]->(a) WHERE a.greengoal=$goal RETURN"
                          + " a, Id(a)",
                      parameters("id", arcId, "goal", goal));
              NachhaltigkeitszieleDTO ziel = null;
              if (result.hasNext()) {
                Record record = result.single();
                ziel =
                    new NachhaltigkeitszieleDTO(
                        record.get("a.greengoal").asString(),
                        record.get("a.motivation").asString(),
                        record.get("a.prio").asString(),
                        record.get("a.saving").asString());
                ziel.setId(String.valueOf(record.get("Id(a)")));
              }
              return ziel;
            });
      }
    }
    return null;
  }

  public NachhaltigkeitszieleDTO findByGoal2(String goal) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "MATCH (a:Nachhaltigkeitsziele) MATCH(d:Arc42) WHERE Id(d)=$id MATCH"
                          + " (d)-[r:hasNachhaltigkeitsziele]->(a) WHERE a.greengoal=$goal RETURN"
                          + " a, Id(a)",
                      parameters("id", arcId, "goal", goal));
              NachhaltigkeitszieleDTO ziel = null;
              if (result.hasNext()) {
                Record record = result.single();
                ziel =
                    new NachhaltigkeitszieleDTO(
                        record.get("a.greengoal").asString(),
                        record.get("a.motivation").asString(),
                        record.get("a.prio").asString(),
                        record.get("a.saving").asString());
                ziel.setId(String.valueOf(record.get("Id(a)")));
              }
              return ziel;
            });
      }
    }
    return null;
  }
}
