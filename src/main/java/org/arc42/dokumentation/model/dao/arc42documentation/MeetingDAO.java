package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.MeetingDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class MeetingDAO extends ARC42DAOAbstract<MeetingDTO, String> {
  private static MeetingDAO instance;

  private MeetingDAO() {
    super();
  }

  public static MeetingDAO getInstance() {
    if (instance != null) {
      return instance;
    }
    instance = new MeetingDAO();
    return instance;
  }

  @Override
  public MeetingDTO save(MeetingDTO meetingDTO) {

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
                          + " create (a)-[r:hasMeeting]->(m:Meeting {name:$name,"
                          + System.lineSeparator()
                          + "frequency:$frequency, repetition:$repetition, type:$type})"
                          + System.lineSeparator()
                          + "return Id(m), m.name, m.frequency, m.repetition, m.type",
                      parameters(
                          "id",
                          arcId,
                          "name",
                          meetingDTO.getMeetingName(),
                          "frequency",
                          meetingDTO.getFrequency(),
                          "repetition",
                          meetingDTO.getRepetition(),
                          "type",
                          meetingDTO.getMeetingType()));
              MeetingDTO dto = null;
              if (result != null) {
                Record record = result.single();
                dto =
                    new MeetingDTO(
                        record.get("m.name").asString(),
                        record.get("m.frequency").asInt(),
                        record.get("m.repetition").asString(),
                        record.get("m.type").asString());
                dto.setId(String.valueOf(record.get("Id(m)")));
              }
              return dto;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(MeetingDTO meetingDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(m:Meeting) where Id(m)=$id"
                        + System.lineSeparator()
                        + "detach delete m return count(*)",
                    parameters("id", Integer.parseInt(meetingDTO.getId())));
            Record record = result.single();
            return record != null && !String.valueOf(record.get("count(*)")).isEmpty();
          });
    }
  }

  @Override
  public void update(MeetingDTO meetingDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(m:Meeting) where Id(m)=$id"
                        + System.lineSeparator()
                        + "set m = {name:$name, frequency:$frequency,"
                        + System.lineSeparator()
                        + "repetition:$repetition, type:$type}"
                        + System.lineSeparator()
                        + "return Id(m), m.name, m.frequency, m.repetition, m.type",
                    parameters(
                        "id",
                        Integer.parseInt(meetingDTO.getId()),
                        "name",
                        meetingDTO.getMeetingName(),
                        "frequency",
                        meetingDTO.getFrequency(),
                        "repetition",
                        meetingDTO.getRepetition(),
                        "type",
                        meetingDTO.getMeetingType()));
            MeetingDTO dto = null;
            if (result != null) {
              Record record = result.single();
              dto =
                  new MeetingDTO(
                      record.get("m.name").asString(),
                      record.get("m.frequency").asInt(),
                      record.get("m.repetition").asString(),
                      record.get("m.type").asString());
              dto.setId(String.valueOf(record.get("Id(m)")));
            }
            return dto;
          });
    }
  }

  @Override
  public List<MeetingDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(arcId.toString());
  }

  private List<MeetingDTO> findAllByArcId(String arcIdString) {
    if (arcIdString != null && !arcIdString.isEmpty()) {
      Integer arcId = Integer.parseInt(arcIdString);
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(m:Meeting)"
                          + System.lineSeparator()
                          + "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "match (a)-[r:hasMeeting]->(m) return Id(m), m.name,"
                          + System.lineSeparator()
                          + "m.frequency, m.repetition, m.type",
                      parameters("id", arcId));
              return result.list(
                  r -> {
                    MeetingDTO meetingDTO =
                        new MeetingDTO(
                            r.get("m.name").asString(),
                            r.get("m.frequency").asInt(),
                            r.get("m.repetition").asString(),
                            r.get("m.type").asString());
                    meetingDTO.setId(String.valueOf(r.get("Id(m)")));
                    return meetingDTO;
                  });
            });
      }
    }
    return new ArrayList<>();
  }

  @Override
  public MeetingDTO findById(String id) {
    if (id != null && !id.isEmpty()) {
      Integer idI = Integer.parseInt(id);
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (m:Meeting) where Id(m)=$id"
                          + System.lineSeparator()
                          + "return Id(m), m.name, m.frequency, m.repetition, m.type",
                      parameters("id", idI));
              MeetingDTO dto = null;
              if (result != null) {
                Record record = result.single();
                dto =
                    new MeetingDTO(
                        record.get("m.name").asString(),
                        record.get("m.frequency").asInt(),
                        record.get("m.repetition").asString(),
                        record.get("m.type").asString());
                dto.setId(String.valueOf(record.get("Id(m)")));
              }
              return dto;
            });
      }
    }
    return null;
  }
}
