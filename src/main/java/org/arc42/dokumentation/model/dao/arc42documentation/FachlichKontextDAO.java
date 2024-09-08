package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.FachlicherKontextDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class FachlichKontextDAO extends ARC42DAOAbstract<FachlicherKontextDTO, String> {

  private static FachlichKontextDAO instance;

  public FachlichKontextDAO() {
    super();
  }

  public static FachlichKontextDAO getInstance() {
    if (instance == null) {
      instance = new FachlichKontextDAO();
    }
    return instance;
  }

  @Override
  public FachlicherKontextDTO save(FachlicherKontextDTO fachlicherKontextDTO) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + " create (a)-[r:hasFachlicherKontext]->(e:FachlicherKontext"
                          + " {partner:$partner,"
                          + " input:$input,"
                          + " output:$output,"
                          + " beschreibung: $beschreibung,"
                          + " risiken: $risiken})"
                          + System.lineSeparator()
                          + "return Id(e), e.partner, e.input, e.output, e.beschreibung, e.risiken",
                      parameters(
                          "id",
                          arcId,
                          "partner",
                          fachlicherKontextDTO.getKommunikationspartner(),
                          "input",
                          fachlicherKontextDTO.getInput(),
                          "output",
                          fachlicherKontextDTO.getOutput(),
                          "beschreibung",
                          fachlicherKontextDTO.getBeschreibung(),
                          "risiken",
                          fachlicherKontextDTO.getRisiken()));
              FachlicherKontextDTO dto = null;
              if (result != null) {
                Record r = result.single();
                dto =
                    new FachlicherKontextDTO(
                        r.get("e.partner").asString(),
                        r.get("e.input").asString(),
                        r.get("e.output").asString(),
                        r.get("e.beschreibung").asString(),
                        r.get("e.risiken").asString());
                dto.setId(String.valueOf(r.get("Id(e)")));
              }
              return dto;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(FachlicherKontextDTO fachlicherKontextDTO) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:FachlicherKontext) where Id(a)=$id"
                        + System.lineSeparator()
                        + "detach delete a return count(*)",
                    parameters("id", Integer.parseInt(fachlicherKontextDTO.getId())));
            Record r = result.single();
            return r != null && !String.valueOf(r.get("count(*)")).isEmpty();
          });
    }
  }

  @Override
  public void update(FachlicherKontextDTO fachlicherKontextDTO) {
    try (Session session = getDriver().session()) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:FachlicherKontext) where Id(a)=$id set a = {partner:$partner,"
                        + " input:$input, output:$output, beschreibung:$beschreibung,"
                        + " risiken:$risiken}"
                        + System.lineSeparator()
                        + "return Id(a), a.partner, a.input, a.output, a.beschreibung, a.risiken",
                    parameters(
                        "id",
                        Integer.parseInt(fachlicherKontextDTO.getId()),
                        "partner",
                        fachlicherKontextDTO.getKommunikationspartner(),
                        "input",
                        fachlicherKontextDTO.getInput(),
                        "output",
                        fachlicherKontextDTO.getOutput(),
                        "beschreibung",
                        fachlicherKontextDTO.getBeschreibung(),
                        "risiken",
                        fachlicherKontextDTO.getRisiken()));
            FachlicherKontextDTO dto = null;
            if (result != null) {
              Record r = result.single();
              dto =
                  new FachlicherKontextDTO(
                      r.get("a.partner").asString(),
                      r.get("a.input").asString(),
                      r.get("a.output").asString(),
                      r.get("a.beschreibung").asString(),
                      r.get("a.risiken").asString());
              dto.setId(String.valueOf(r.get("Id(a)")));
            }
            return dto;
          });
    }
  }

  @Override
  public List<FachlicherKontextDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(arcId.toString());
  }

  public List<FachlicherKontextDTO> findAllByArcId(String arcIdString) {
    if (arcIdString != null && !arcIdString.isEmpty()) {
      Integer arcId = Integer.parseInt(arcIdString);
      try (Session session = getDriver().session()) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:FachlicherKontext)"
                          + System.lineSeparator()
                          + "match(d:Arc42) where Id(d)=$id"
                          + System.lineSeparator()
                          + "match (d)-[r:hasFachlicherKontext]->(a) return Id(a), a.partner,"
                          + " a.input, a.output, a.beschreibung, a.risiken",
                      parameters("id", arcId));
              return result.list(
                  r -> {
                    FachlicherKontextDTO dto =
                        new FachlicherKontextDTO(
                            r.get("a.partner").asString(),
                            r.get("a.input").asString(),
                            r.get("a.output").asString(),
                            r.get("a.beschreibung").asString(),
                            r.get("a.risiken").asString());
                    dto.setId(String.valueOf(r.get("Id(a)")));
                    return dto;
                  });
            });
      }
    }
    return new ArrayList<>();
  }

  @Override
  public FachlicherKontextDTO findById(String id) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(id);
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + " match (a)-[r:kontext]->(s:Kontext:FachlichKontext)"
                          + System.lineSeparator()
                          + "return Id(s), s.fkontext",
                      parameters("id", arcId));
              FachlicherKontextDTO dto = null;
              return dto;
            });
      }
    }
    return null;
  }

  @Override
  public FachlicherKontextDTO createRelationship(FachlicherKontextDTO dto) {
    super.createRelationship(dto);
    return dto;
  }
}
