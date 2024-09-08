package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.ImageDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class ImageDAOVerteilung extends ARC42DAOAbstract<ImageDTO, String> {
  private static ImageDAOVerteilung instance;

  public ImageDAOVerteilung() {
    super();
  }

  public static ImageDAOVerteilung getInstance() {
    if (instance == null) {
      instance = new ImageDAOVerteilung();
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
                          + " create (a)-[r:verteilungssicht]->(s:Image:Verteilung"
                          + " {bildName:$bildName, bildMimeType:$bildMimeType, bildPath:$bildPath,"
                          + " uxfName:$uxfName, uxfMimeType:$uxfMimeType, uxfPath:$uxfPath,"
                          + " description:$description})"
                          + System.lineSeparator()
                          + "return Id(s), s.bildName, s.bildMimeType, s.bildPath, s.uxfName,"
                          + " s.uxfMimeType, s.uxfPath, s.description",
                      parameters(
                          "id",
                          arcId,
                          "bildName",
                          imageDTO.getBildName(),
                          "bildMimeType",
                          imageDTO.getBildMimeType(),
                          "bildPath",
                          imageDTO.getBildStream(),
                          "uxfName",
                          imageDTO.getUxfName(),
                          "uxfMimeType",
                          imageDTO.getUxfMimeType(),
                          "uxfPath",
                          imageDTO.getUxfStream(),
                          "description",
                          imageDTO.getDescription()));
              ImageDTO dto = null;
              if (result != null) {
                Record r = result.single();
                dto =
                    new ImageDTO(
                        String.valueOf(r.get("Id(s)")),
                        r.get("s.bildName").asString(),
                        r.get("s.bildMimeType").asString(),
                        r.get("s.bildPath").asByteArray(),
                        r.get("s.uxfName").asString(),
                        r.get("s.uxfMimeType").asString(),
                        r.get("s.uxfPath").asByteArray(),
                        r.get("s.description").asString());
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
                      + "match (a)-[r:verteilungssicht]->(s:Image:Verteilung) detach delete s"
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
                          + " match (a)-[r:verteilungssicht]->(s:Image:Verteilung)"
                          + System.lineSeparator()
                          + "return Id(s), s.bildName, s.bildMimeType, s.bildPath, s.uxfName,"
                          + " s.uxfMimeType, s.uxfPath, s.description",
                      parameters("id", arcId));
              ImageDTO dto = null;
              if (result != null) {
                Record r = (result.hasNext()) ? result.single() : null;
                if (r != null) {
                  dto =
                      new ImageDTO(
                          String.valueOf(r.get("Id(s)")),
                          r.get("s.bildName").asString(),
                          r.get("s.bildMimeType").asString(),
                          r.get("s.bildPath").asByteArray(),
                          r.get("s.uxfName").asString(),
                          r.get("s.uxfMimeType").asString(),
                          r.get("s.uxfPath").asByteArray(),
                          r.get("s.description").asString());
                }
              }
              return dto;
            });
      }
    }
    return null;
  }
}
