package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.InterfaceDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class InterfaceDAO extends ARC42DAOAbstract<InterfaceDTO, String> {

  private static InterfaceDAO instance;

  private InterfaceDAO() {
    super();
  }

  public static InterfaceDAO getInstance() {
    if (instance != null) {
      return instance;
    }
    instance = new InterfaceDAO();
    return instance;
  }

  public InterfaceDTO save(InterfaceDTO interfaceDTO) {

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
                          + " create (a)-[r:hasInterface]->(i:Interface {name:$name,"
                          + System.lineSeparator()
                          + "documentation:$documentation, calls:$calls, emissions:$emissions})"
                          + System.lineSeparator()
                          + "return Id(i), i.name, i.documentation, i.calls, i.emissions",
                      parameters(
                          "id",
                          arcId,
                          "name",
                          interfaceDTO.getName(),
                          "documentation",
                          interfaceDTO.getDocumentation(),
                          "calls",
                          interfaceDTO.getCallsPerMonth(),
                          "emissions",
                          interfaceDTO.getEmissions()));
              InterfaceDTO dto = null;
              if (result != null) {
                Record record = result.single();
                dto =
                    new InterfaceDTO(
                        record.get("i.name").asString(),
                        record.get("i.documentation").asString(),
                        record.get("i.calls").asInt(),
                        record.get("i.emissions").asDouble());
                dto.setId(String.valueOf(record.get("Id(i)")));
              }
              return dto;
            });
      }
    }
    return null;
  }

  public void update(InterfaceDTO selected) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(i:Interface) where Id(i)=$id"
                        + System.lineSeparator()
                        + "set i = {name:$name, documentation:$documentation,"
                        + System.lineSeparator()
                        + "calls:$calls, emissions:$emissions}"
                        + System.lineSeparator()
                        + "return Id(i), i.name, i.documentation, i.calls, i.emissions",
                    parameters(
                        "id",
                        Integer.parseInt(selected.getId()),
                        "name",
                        selected.getName(),
                        "documentation",
                        selected.getDocumentation(),
                        "calls",
                        selected.getCallsPerMonth(),
                        "emissions",
                        selected.getEmissions()));
            InterfaceDTO dto = null;
            if (result != null) {
              Record record = result.single();
              dto =
                  new InterfaceDTO(
                      record.get("i.name").asString(),
                      record.get("i.documentation").asString(),
                      record.get("i.calls").asInt(),
                      record.get("i.emissions").asDouble());
              dto.setId(String.valueOf(record.get("Id(i)")));
            }
            return dto;
          });
    }
  }

  public Boolean delete(InterfaceDTO selected) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(i:Interface) where Id(i)=$id"
                        + System.lineSeparator()
                        + "detach delete i return count(*)",
                    parameters("id", Integer.parseInt(selected.getId())));
            Record record = result.single();
            return record != null && !String.valueOf(record.get("count(*)")).isEmpty();
          });
    }
  }

  public List<InterfaceDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(arcId.toString());
  }

  private List<InterfaceDTO> findAllByArcId(String arcIdString) {
    if (arcIdString != null && !arcIdString.isEmpty()) {
      Integer arcId = Integer.parseInt(arcIdString);
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(i:Interface)"
                          + System.lineSeparator()
                          + "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "match (a)-[r:hasInterface]->(i) return Id(i), i.name,"
                          + System.lineSeparator()
                          + "i.documentation, i.calls, i.emissions",
                      parameters("id", arcId));
              return result.list(
                  r -> {
                    InterfaceDTO interfaceDTO =
                        new InterfaceDTO(
                            r.get("i.name").asString(),
                            r.get("i.documentation").toString().replaceAll("\"", ""),
                            r.get("i.calls").asInt(),
                            r.get("i.emissions").asDouble());
                    interfaceDTO.setId(String.valueOf(r.get("Id(i)")));
                    return interfaceDTO;
                  });
            });
      }
    }
    return new ArrayList<>();
  }

  @Override
  public InterfaceDTO findById(String id) {
    if (id != null && !id.isEmpty()) {
      Integer idI = Integer.parseInt(id);
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (i:Interface) where Id(i)=$id"
                          + System.lineSeparator()
                          + "return Id(i), i.name, i.documentation, i.calls, i.emissions",
                      parameters("id", idI));
              InterfaceDTO dto = null;
              if (result != null) {
                Record record = result.single();
                dto =
                    new InterfaceDTO(
                        record.get("i.name").asString(),
                        record.get("i.documentation").asString(),
                        record.get("i.calls").asInt(),
                        record.get("i.emissions").asDouble());
                dto.setId(String.valueOf(record.get("Id(i)")));
              }
              return dto;
            });
      }
    }
    return null;
  }
}
