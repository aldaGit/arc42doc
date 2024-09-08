package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.OekologischDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class OekologischDAO extends ARC42DAOAbstract<OekologischDTO, String> {

  private static OekologischDAO instance;

  private OekologischDAO() {
    super();
  }

  public static OekologischDAO getInstance() {
    if (instance == null) {
      instance = new OekologischDAO();
    }
    return instance;
  }

  public OekologischDTO save(OekologischDTO oekologischDTO) {
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
                          + " (d)-[r:hatOekologischeRandbedingung]->(a:OekologischeRandbedingung"
                          + " {randbedingung:$randbedingung, hintergrund:$hintergrund}) return"
                          + " a.randbedingung, a.hintergrund, Id(a)",
                      parameters(
                          "arc",
                          arcId,
                          "randbedingung",
                          oekologischDTO.getRandbedingung(),
                          "hintergrund",
                          oekologischDTO.getHintergrund()));
              Record record = result.next();
              return new OekologischDTO(
                  String.valueOf(record.get("Id(a)")),
                  record.get("a.randbedingung").toString(),
                  record.get("a.erlaeuterung").toString());
            });
      }
    }
    return null;
  }

  public void update(OekologischDTO oekologischDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:OekologischeRandbedingung) where Id(a)=$id  set a ="
                        + " {randbedingung:$randbedingung, hintergrund:$hintergrund} return"
                        + " a.randbedingung, a.hintergrund, Id(a)",
                    parameters(
                        "id",
                        Integer.parseInt(oekologischDTO.getId()),
                        "randbedingung",
                        oekologischDTO.getRandbedingung(),
                        "hintergrund",
                        oekologischDTO.getHintergrund()));
            OekologischDTO dto = null;
            if (result != null) {
              Record record = result.single();
              dto =
                  new OekologischDTO(
                      String.valueOf(record.get("Id(a)")),
                      record.get("a.randbedingung").toString(),
                      record.get("a.hintergrund").toString());
            }
            return dto;
          });
    }
  }

  public Boolean delete(OekologischDTO oekologischDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:OekologischeRandbedingung)where Id(a)=$id"
                        + System.lineSeparator()
                        + "match(d:Arc42) match (a)<-[r:hatOekologischeRandbedingung]-(d) delete"
                        + " r,a return count(*)",
                    parameters("id", Integer.parseInt(oekologischDTO.getId())));
            Record record = result.single();
            return record != null && !String.valueOf(record.get("count(*)")).isEmpty();
          });
    }
  }

  public List<OekologischDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(String.valueOf(arcId));
  }

  public List<OekologischDTO> findAllByArcId(String id) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(d:Arc42) where Id(d)=$id match(a:OekologischeRandbedingung) match"
                        + " (d)-[r:hatOekologischeRandbedingung]->(a) return a.randbedingung,"
                        + " a.hintergrund, Id(a)",
                    parameters("id", Integer.parseInt(id)));
            return result.list(
                record -> {
                  OekologischDTO dto =
                      new OekologischDTO(
                          record.get("a.randbedingung").asString(),
                          record.get("a.hintergrund").asString());
                  dto.setId(String.valueOf(record.get("Id(a)")));
                  return dto;
                });
          });
    }
  }

  public OekologischDTO findById(String id) {
    if (id != null && !id.isEmpty()) {
      Integer arcId = Integer.parseInt(id);
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:OekologischeRandbedingung) where Id(a)=$id return a.randbedingung,"
                          + " a.hintergrund, Id(a)",
                      parameters("id", arcId));
              Record record = result.single();
              if (record == null) {
                return null;
              }
              return new OekologischDTO(
                  String.valueOf(record.get("Id(a)")),
                  record.get("a.randbedingung").toString(),
                  record.get("a.hintergrund").toString());
            });
      }
    }
    return null;
  }
}
