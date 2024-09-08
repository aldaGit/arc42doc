package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.HardwareDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class HardwareDAO extends ARC42DAOAbstract<HardwareDTO, String> {
  private static HardwareDAO instance;

  public HardwareDAO() {
    super();
  }

  public static HardwareDAO getInstance() {
    if (instance == null) {
      instance = new HardwareDAO();
    }
    return instance;
  }

  @Override
  public HardwareDTO save(HardwareDTO hardwareDTO) {

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
                          + " create (a)-[r:hasHardware]->(h:Hardware {name:$name,"
                          + " powerConsumption:$powerConsumption, isRenewable:$isRenewable})"
                          + System.lineSeparator()
                          + "return Id(h), h.name, h.powerConsumption, h.isRenewable",
                      parameters(
                          "id",
                          arcId,
                          "name",
                          hardwareDTO.getName(),
                          "powerConsumption",
                          hardwareDTO.getPowerConsumption(),
                          "isRenewable",
                          hardwareDTO.isRenewable()));
              HardwareDTO dto = null;
              if (result != null) {
                Record record = result.single();
                dto =
                    new HardwareDTO(
                        record.get("h.name").asString(),
                        record.get("h.powerConsumption").asDouble(),
                        record.get("h.isRenewable").asBoolean());
                dto.setId(String.valueOf(record.get("Id(h)")));
              }
              return dto;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(HardwareDTO hardwareDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(h:Hardware) where Id(h)=$id"
                        + System.lineSeparator()
                        + "detach delete h return count(*)",
                    parameters("id", Integer.parseInt(hardwareDTO.getId())));
            Record record = result.single();
            return record != null && !String.valueOf(record.get("count(*)")).isEmpty();
          });
    }
  }

  @Override
  public void update(HardwareDTO hardwareDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(h:Hardware) where Id(h)=$id"
                        + System.lineSeparator()
                        + "set h = {name:$name, powerConsumption:$powerConsumption,"
                        + " isRenewable:$isRenewable}"
                        + System.lineSeparator()
                        + "return Id(h), h.name, h.powerConsumption, h.isRenewable",
                    parameters(
                        "id",
                        Integer.parseInt(hardwareDTO.getId()),
                        "name",
                        hardwareDTO.getName(),
                        "powerConsumption",
                        hardwareDTO.getPowerConsumption(),
                        "isRenewable",
                        hardwareDTO.isRenewable()));
            HardwareDTO dto = null;
            if (result != null) {
              Record record = result.single();
              dto =
                  new HardwareDTO(
                      record.get("h.name").asString(),
                      record.get("h.powerConsumption").asDouble(),
                      record.get("h.isRenewable").asBoolean());
              dto.setId(String.valueOf(record.get("Id(h)")));
            }
            return dto;
          });
    }
  }

  @Override
  public List<HardwareDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(arcId.toString());
  }

  private List<HardwareDTO> findAllByArcId(String arcIdString) {
    if (arcIdString != null && !arcIdString.isEmpty()) {
      Integer arcId = Integer.parseInt(arcIdString);
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(h:Hardware)"
                          + System.lineSeparator()
                          + "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "match (a)-[r:hasHardware]->(h) return Id(h), h.name,"
                          + " h.powerConsumption, h.isRenewable",
                      parameters("id", arcId));
              return result.list(
                  r -> {
                    HardwareDTO hardwareDTO =
                        new HardwareDTO(
                            r.get("h.name").asString(),
                            r.get("h.powerConsumption").asDouble(),
                            r.get("h.isRenewable").asBoolean());
                    hardwareDTO.setId(String.valueOf(r.get("Id(h)")));
                    return hardwareDTO;
                  });
            });
      }
    }
    return new ArrayList<>();
  }

  @Override
  public HardwareDTO findById(String id) {
    return new HardwareDTO("", 0, false);
  }

  @Override
  public HardwareDTO createRelationship(HardwareDTO dto) {
    super.createRelationship(dto);
    return dto;
  }
}
