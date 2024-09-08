package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.ImageDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class TechnischKontextDAO extends ARC42DAOAbstract<ImageDTO, String> {

  private static TechnischKontextDAO instance;

  public TechnischKontextDAO() {
    super();
  }

  public static TechnischKontextDAO getInstance() {
    if (instance == null) {
      instance = new TechnischKontextDAO();
    }
    return instance;
  }

  @Override
  public ImageDTO save(ImageDTO imageDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              delete(null);
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + " create (a)-[r:kontext]->(s:Kontext:TechnischhKontext"
                          + " {tkontext:$tkontext})"
                          + System.lineSeparator()
                          + "return Id(s), s.tkontext",
                      parameters("id", arcId, "tkontext", imageDTO.getDescription()));
              ImageDTO dto = null;
              if (result != null) {
                Record record = result.single();
                dto = new ImageDTO();
                if (!record.get("s.tkontext").asString().equals("")) {
                  dto.setDescription(record.get("s.tkontext").asString());
                } else {
                  dto.setDescription("");
                }
              }
              return dto;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(ImageDTO imageDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        session.writeTransaction(
            transaction -> {
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id"
                      + System.lineSeparator()
                      + "match (a)-[r:kontext]->(s:Kontext:TechnischhKontext) detach delete s"
                      + " return count(*)",
                  parameters("id", arcId));
              return 0;
            });
      }
    }
    return true;
  }

  @Override
  public void update(ImageDTO imageDTO) {}

  @Override
  public List<ImageDTO> findAll(String url) {
    return null;
  }

  @Override
  public ImageDTO findById(String id) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(id);
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + " match (a)-[r:kontext]->(s:Kontext:TechnischhKontext)"
                          + System.lineSeparator()
                          + "return Id(s), s.tkontext",
                      parameters("id", arcId));
              ImageDTO dto = null;
              if (result != null) {
                Record record = (result.hasNext()) ? result.single() : null;
                dto = new ImageDTO();
                if (record != null) {
                  dto.setDescription(record.get("s.tkontext").asString());
                } else {
                  dto.setDescription("");
                }
              }
              return dto;
            });
      }
    }
    return null;
  }

  @Override
  public ImageDTO createRelationship(ImageDTO dto) {
    super.createRelationship(dto);
    return dto;
  }
}
