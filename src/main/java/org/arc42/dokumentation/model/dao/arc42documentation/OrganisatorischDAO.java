package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.OrganisatorischDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class OrganisatorischDAO extends ARC42DAOAbstract<OrganisatorischDTO, String> {

  private static OrganisatorischDAO instance;

  private OrganisatorischDAO() {
    super();
  }

  public static OrganisatorischDAO getInstance() {
    if (instance == null) {
      instance = new OrganisatorischDAO();
    }
    return instance;
  }

  public OrganisatorischDTO save(OrganisatorischDTO organisatorischDTO) {
    Integer arcId = getActualArcId(null);
    if (arcId != null) {
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (d:Arc42) where Id(d)=$arc"
                          + System.lineSeparator()
                          + "create"
                          + " (d)-[r:hatOrganisatorischRandbedingung]->(a:OrganisatorischRandbedingung"
                          + " {randbedingung:$randbedingung, hintergrund:$hintergrund}) return"
                          + " a.randbedingung, a.hintergrund, Id(a)",
                      parameters(
                          "arc",
                          arcId,
                          "randbedingung",
                          organisatorischDTO.getRandbedingung(),
                          "hintergrund",
                          organisatorischDTO.getHintergrund()));
              Record record = result.next();
              return new OrganisatorischDTO(
                  String.valueOf(record.get("Id(a)")),
                  record.get("a.randbedingung").toString(),
                  record.get("a.hintergrund").toString());
            });
      }
    }
    return null;
  }

  public void update(OrganisatorischDTO organisatorischDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:OrganisatorischRandbedingung) where Id(a)=$id  set a ="
                        + " {randbedingung:$randbedingung, hintergrund:$hintergrund} return"
                        + " a.randbedingung, a.hintergrund, Id(a)",
                    parameters(
                        "id",
                        Integer.parseInt(organisatorischDTO.getId()),
                        "randbedingung",
                        organisatorischDTO.getRandbedingung(),
                        "hintergrund",
                        organisatorischDTO.getHintergrund()));
            OrganisatorischDTO dto = null;
            if (result != null) {
              Record record = result.single();
              dto =
                  new OrganisatorischDTO(
                      String.valueOf(record.get("Id(a)")),
                      record.get("a.randbedingung").toString(),
                      record.get("a.hintergrund").toString());
            }
            return dto;
          });
    }
  }

  public Boolean delete(OrganisatorischDTO organisatorischDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:OrganisatorischRandbedingung)where Id(a)=$id"
                        + System.lineSeparator()
                        + "match(d:Arc42) match (a)<-[r:hatOrganisatorischRandbedingung]-(d) delete"
                        + " r,a return count(*)",
                    parameters("id", Integer.parseInt(organisatorischDTO.getId())));
            Record record = result.single();
            return record != null && !String.valueOf(record.get("count(*)")).isEmpty();
          });
    }
  }

  public List<OrganisatorischDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(String.valueOf(arcId));
  }

  public List<OrganisatorischDTO> findAllByArcId(String id) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(d:Arc42) where Id(d)=$id match(a:OrganisatorischRandbedingung) match"
                        + " (d)-[r:hatOrganisatorischRandbedingung]->(a) return a.randbedingung,"
                        + " a.hintergrund, Id(a)",
                    parameters("id", Integer.parseInt(id)));
            return result.list(
                record -> {
                  OrganisatorischDTO dto =
                      new OrganisatorischDTO(
                          record.get("a.randbedingung").asString(),
                          record.get("a.hintergrund").asString());
                  dto.setId(String.valueOf(record.get("Id(a)")));
                  return dto;
                });
          });
    }
  }

  public OrganisatorischDTO findById(String id) {
    if (id != null && !id.isEmpty()) {
      Integer arcId = Integer.parseInt(id);
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:OrganisatorischRandbedingung) where Id(a)=$id return"
                          + " a.randbedingung, a.hintergrund, Id(a)",
                      parameters("id", arcId));
              Record record = result.single();
              if (record == null) {
                return null;
              }
              return new OrganisatorischDTO(
                  String.valueOf(record.get("Id(a)")),
                  record.get("a.randbedingung").toString(),
                  record.get("a.hintergrund").toString());
            });
      }
    }
    return null;
  }
}