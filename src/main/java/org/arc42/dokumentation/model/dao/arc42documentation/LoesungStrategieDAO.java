package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.ImageDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.SessionConfig;

public class LoesungStrategieDAO extends ARC42DAOAbstract<ImageDTO, String> {

  private static LoesungStrategieDAO instance;

  public LoesungStrategieDAO() {
    super();
  }

  public static LoesungStrategieDAO getInstance() {
    if (instance == null) {
      instance = new LoesungStrategieDAO();
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
                          + " create (a)-[r:hatLoesung]->(s:LoesungStrategie {loesung:$loesung})"
                          + System.lineSeparator()
                          + "return Id(s), s.loesung",
                      parameters("id", arcId, "loesung", imageDTO.getDescription()));
              ImageDTO dto = null;
              if (result != null) {
                Record r = result.single();
                dto = new ImageDTO();
                if (!r.get("s.loesung").asString().equals("")) {
                  dto.setDescription(r.get("s.loesung").asString());
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
                      + "match (a)-[r:hatLoesung]->(s:LoesungStrategie) detach delete s return"
                      + " count(*)",
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
                          + " match (a)-[r:hatLoesung]->(s:LoesungStrategie)"
                          + System.lineSeparator()
                          + "return Id(s), s.loesung",
                      parameters("id", arcId));
              ImageDTO dto = null;
              if (result != null) {
                Record r = (result.hasNext()) ? result.single() : null;
                dto = new ImageDTO();
                if (r != null) {
                  dto.setDescription(r.get("s.loesung").asString());
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
