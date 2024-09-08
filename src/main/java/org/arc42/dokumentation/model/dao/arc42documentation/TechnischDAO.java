package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.TechnischDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class TechnischDAO extends ARC42DAOAbstract<TechnischDTO, String> {

  private static TechnischDAO instance;

  private TechnischDAO() {
    super();
  }

  public static TechnischDAO getInstance() {
    if (instance == null) {
      instance = new TechnischDAO();
    }
    return instance;
  }

  public TechnischDTO save(TechnischDTO technischDTO) {
    Integer arcId = getActualArcId(null);
    if (arcId != null) {
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (d:Arc42) where Id(d)=$arc"
                          + System.lineSeparator()
                          + "create (d)-[r:hatTechnischRandbedingung]->(a:TechnischeRandbedingung"
                          + " {randbedingung:$randbedingung, hintergrund:$hintergrund}) return"
                          + " a.randbedingung, a.hintergrund, Id(a)",
                      parameters(
                          "arc",
                          arcId,
                          "randbedingung",
                          technischDTO.getRandbedingung(),
                          "hintergrund",
                          technischDTO.getHintergrund()));
              Record r = result.next();
              return new TechnischDTO(
                  String.valueOf(r.get("Id(a)")),
                  r.get("a.randbedingung").toString(),
                  r.get("a.erlaeuterung").toString());
            });
      }
    }
    return null;
  }

  public void update(TechnischDTO technischDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:TechnischeRandbedingung) where Id(a)=$id  set a ="
                        + " {randbedingung:$randbedingung, hintergrund:$hintergrund} return"
                        + " a.randbedingung, a.hintergrund, Id(a)",
                    parameters(
                        "id",
                        Integer.parseInt(technischDTO.getId()),
                        "randbedingung",
                        technischDTO.getRandbedingung(),
                        "hintergrund",
                        technischDTO.getHintergrund()));
            TechnischDTO dto = null;
            if (result != null) {
              Record r = result.single();
              dto =
                  new TechnischDTO(
                      String.valueOf(r.get("Id(a)")),
                      r.get("a.randbedingung").toString(),
                      r.get("a.hintergrund").toString());
            }
            return dto;
          });
    }
  }

  public Boolean delete(TechnischDTO technischDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:TechnischeRandbedingung)where Id(a)=$id"
                        + System.lineSeparator()
                        + "match(d:Arc42) match (a)<-[r:hatTechnischRandbedingung]-(d) delete r,a"
                        + " return count(*)",
                    parameters("id", Integer.parseInt(technischDTO.getId())));
            Record r = result.single();
            return r != null && !String.valueOf(r.get("count(*)")).isEmpty();
          });
    }
  }

  public List<TechnischDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(String.valueOf(arcId));
  }

  public List<TechnischDTO> findAllByArcId(String id) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(d:Arc42) where Id(d)=$id match(a:TechnischeRandbedingung) match"
                        + " (d)-[r:hatTechnischRandbedingung]->(a) return a.randbedingung,"
                        + " a.hintergrund, Id(a)",
                    parameters("id", Integer.parseInt(id)));
            return result.list(
                r -> {
                  TechnischDTO dto =
                      new TechnischDTO(
                          r.get("a.randbedingung").asString(), r.get("a.hintergrund").asString());
                  dto.setId(String.valueOf(r.get("Id(a)")));
                  return dto;
                });
          });
    }
  }

  public TechnischDTO findById(String id) {
    if (id != null && !id.isEmpty()) {
      Integer arcId = Integer.parseInt(id);
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:TechnischeRandbedingung) where Id(a)=$id return a.randbedingung,"
                          + " a.hintergrund, Id(a)",
                      parameters("id", arcId));
              Record r = result.single();
              if (r == null) {
                return null;
              }
              return new TechnischDTO(
                  String.valueOf(r.get("Id(a)")),
                  r.get("a.randbedingung").toString(),
                  r.get("a.hintergrund").toString());
            });
      }
    }
    return null;
  }
}
