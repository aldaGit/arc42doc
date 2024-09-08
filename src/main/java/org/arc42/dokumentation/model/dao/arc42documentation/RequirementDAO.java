package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.RequirementDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class RequirementDAO extends ARC42DAOAbstract<RequirementDTO, String> {

  private static RequirementDAO instance;

  private RequirementDAO() {
    super();
  }

  public static RequirementDAO getInstance() {
    if (instance == null) {
      instance = new RequirementDAO();
    }
    return instance;
  }

  public RequirementDTO save(RequirementDTO requirementDTO) {
    Integer arcId = getActualArcId(null);
    if (arcId != null) {
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        String ausgabe =
            session.writeTransaction(
                transaction -> {
                  Result result =
                      transaction.run(
                          "match (d:Arc42) where Id(d)=$arc"
                              + System.lineSeparator()
                              + "create (d)-[r:hasRequirement]->(a:Aufgabenstellung {aufgabe:"
                              + " $message}) return a.aufgabe, Id(a)",
                          parameters("arc", arcId, "message", requirementDTO.getAufgabe()));
                  Record r = result.next();
                  return r.get("a.aufgabe") + ":" + r.get("Id(a)");
                });
        RequirementDTO req = new RequirementDTO(ausgabe.split(":")[0]);
        req.setId(String.valueOf(ausgabe.split(":")[1]));
        return req;
      }
    }
    return requirementDTO;
  }

  public void update(RequirementDTO requirementDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Aufgabenstellung) where Id(a)=$id set a.aufgabe=$neu return a.aufgabe,"
                        + " Id(a)",
                    parameters(
                        "id",
                        Integer.parseInt(requirementDTO.getId()),
                        "neu",
                        requirementDTO.getAufgabe()));
            RequirementDTO dto = null;
            if (result != null) {
              Record r = result.single();
              dto = new RequirementDTO(r.get("a.aufgabe").asString());
              dto.setId(String.valueOf(r.get("Id(a)")));
            }
            return dto;
          });
    }
  }

  public Boolean delete(RequirementDTO requirementDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Aufgabenstellung)where Id(a)=$id"
                        + System.lineSeparator()
                        + "match(d:Arc42) match (a)<-[r:hasRequirement]-(d) delete r,a return"
                        + " count(*)",
                    parameters("id", Integer.parseInt(requirementDTO.getId())));
            Record r = result.single();
            return r != null && !String.valueOf(r.get("count(*)")).isEmpty();
          });
    }
  }

  public List<RequirementDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(String.valueOf(arcId));
  }

  public List<RequirementDTO> findAllByArcId(String id) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(d:Arc42) where Id(d)=$id match(a:Aufgabenstellung) match"
                        + " (d)-[r:hasRequirement]->(a) return a.aufgabe, Id(a)",
                    parameters("id", Integer.parseInt(id)));
            return result.list(
                r -> {
                  RequirementDTO req = new RequirementDTO(r.get("a.aufgabe").asString());
                  req.setId(String.valueOf(r.get("Id(a)")));
                  return req;
                });
          });
    }
  }

  public RequirementDTO findById(String id) {
    if (id != null && !id.isEmpty()) {
      Integer arcId = Integer.parseInt(id);
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Aufgabenstellung) where Id(a)=$id return Id(a), a.aufgabe",
                      parameters("id", arcId));
              Record next = result.single();
              if (next == null) {
                return null;
              }
              RequirementDTO req = new RequirementDTO(next.get("a.aufgabe").asString());
              req.setId(String.valueOf(next.get("Id(a)")));
              return req;
            });
      }
    }
    return null;
  }
}
