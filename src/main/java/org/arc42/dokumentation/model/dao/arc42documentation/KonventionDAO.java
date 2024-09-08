package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.KonventionDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class KonventionDAO extends ARC42DAOAbstract<KonventionDTO, String> {
  private static KonventionDAO instance;

  private KonventionDAO() {
    super();
    getDriver();
  }

  public static KonventionDAO getInstance() {
    if (instance == null) {
      instance = new KonventionDAO();
    }
    return instance;
  }

  public KonventionDTO save(KonventionDTO konventionDTO) {
    Integer arcId = getActualArcId(null);
    if (arcId != null) {
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (d:Arc42) where Id(d)=$arc"
                          + System.lineSeparator()
                          + "create (d)-[r:hatKonvention]->(a:Konvention {konvention:$konvention,"
                          + " erlaeuterung:$erlaeuterung}) return a.konvention, a.erlaeuterung,"
                          + " Id(a)",
                      parameters(
                          "arc",
                          arcId,
                          "konvention",
                          konventionDTO.getKonvention(),
                          "erlaeuterung",
                          konventionDTO.getErlaeuterung()));
              Record r = result.next();
              return new KonventionDTO(
                  String.valueOf(r.get("Id(a)")),
                  r.get("a.konvention").toString(),
                  r.get("a.erlaeuterung").toString());
            });
      }
    }
    return null;
  }

  public void update(KonventionDTO konventionDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Konvention) where Id(a)=$id  set a = {konvention:$konvention,"
                        + " erlaeuterung:$erlaeuterung} return a.konvention, a.erlaeuterung, Id(a)",
                    parameters(
                        "id",
                        Integer.parseInt(konventionDTO.getId()),
                        "konvention",
                        konventionDTO.getKonvention(),
                        "erlaeuterung",
                        konventionDTO.getErlaeuterung()));
            KonventionDTO dto = null;
            if (result != null) {
              Record r = result.single();
              dto =
                  new KonventionDTO(
                      String.valueOf(r.get("Id(a)")),
                      r.get("a.konvention").toString(),
                      r.get("a.erlaeuterung").toString());
            }
            return dto;
          });
    }
  }

  public Boolean delete(KonventionDTO konventionDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Konvention)where Id(a)=$id"
                        + System.lineSeparator()
                        + "match(d:Arc42) match (a)<-[r:hatKonvention]-(d) delete r,a return"
                        + " count(*)",
                    parameters("id", Integer.parseInt(konventionDTO.getId())));
            Record r = result.single();
            return r != null && !String.valueOf(r.get("count(*)")).isEmpty();
          });
    }
  }

  public List<KonventionDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(String.valueOf(arcId));
  }

  public List<KonventionDTO> findAllByArcId(String id) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(d:Arc42) where Id(d)=$id match(a:Konvention) match"
                        + " (d)-[r:hatKonvention]->(a) return a.konvention, a.erlaeuterung, Id(a)",
                    parameters("id", Integer.parseInt(id)));
            return result.list(
                r -> {
                  KonventionDTO dto =
                      new KonventionDTO(
                          r.get("a.konvention").asString(), r.get("a.erlaeuterung").asString());
                  dto.setId(String.valueOf(r.get("Id(a)")));
                  return dto;
                });
          });
    }
  }

  public KonventionDTO findById(String id) {
    if (id != null && !id.isEmpty()) {
      Integer arcId = Integer.parseInt(id);
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Konvention) where Id(a)=$id return a.konvention, a.erlaeuterung,"
                          + " Id(a)",
                      parameters("id", arcId));
              Record r = result.single();
              if (r == null) {
                return null;
              }
              return new KonventionDTO(
                  String.valueOf(r.get("Id(a)")),
                  r.get("a.konvention").toString(),
                  r.get("a.erlaeuterung").toString());
            });
      }
    }
    return null;
  }
}
