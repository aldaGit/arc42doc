package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.TextEingabeDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class TextEingabeDAO extends ARC42DAOAbstract<TextEingabeDTO, String> {
  private static TextEingabeDAO instance;

  private TextEingabeDAO() {
    super();
  }

  public static TextEingabeDAO getInstance() {
    if (instance == null) {
      instance = new TextEingabeDAO();
    }
    return instance;
  }

  private boolean isInValid(TextEingabeDTO textEingabeDTO) {
    return textEingabeDTO.getContent() == null || textEingabeDTO.getType() == null;
  }

  public TextEingabeDTO save(TextEingabeDTO textEingabeDTO, String url) {
    Integer arcId = getActualArcId(url);
    if (isInValid(textEingabeDTO)) {
      throw new IllegalArgumentException("RisikoDTO is not valid");
    }

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {

      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "create (a)-[r:hasTextEingabe]->(b:TextEingabe {content: $content,"
                          + " type: $type }) return b.content, Id(b)",
                      parameters(
                          "id",
                          arcId,
                          "content",
                          textEingabeDTO.getContent(),
                          "type",
                          textEingabeDTO.getType().name()));
              return textEingabeDTO;
            });
      }
    }
    return null;
  }

  public void update(TextEingabeDAO textEingabeDTO) {
    throw new UnsupportedOperationException();
  }

  public Boolean delete(TextEingabeDTO textEingabeDTO) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:TextEingabe)where Id(a)=$id"
                        + System.lineSeparator()
                        + "match(d:Arc42) match (a)<-[r:hasTextEingabe]-(d) delete r,a"
                        + " return count(*)",
                    parameters("id", Integer.parseInt(textEingabeDTO.getId())));
            Record r = result.single();
            return r != null && !String.valueOf(r.get("count(*)")).isEmpty();
          });
    }
  }

  public List<TextEingabeDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(String.valueOf(arcId));
  }

  public List<TextEingabeDTO> findAllByArcId(String id) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:TextEingabe)"
                        + System.lineSeparator()
                        + "match(d:Arc42) where Id(d)=$id match"
                        + " (d)-[r:hasTextEingabe]->(a) return  Id(a), a.content, a.type",
                    parameters("id", Integer.parseInt(id)));
            return result.list(
                r -> {
                  TextEingabeDTO dto =
                      new TextEingabeDTO(
                          r.get("a.content", ""),
                          TextEingabeDTO.TEXTTYPE.valueOf(r.get("a.type", "STRENGTH")));
                  dto.setId(String.valueOf(r.get("Id(a)")));
                  return dto;
                });
          });
    }
  }

  // following methodis wrong:

  public TextEingabeDTO findById(String id) {
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
              return new TextEingabeDTO(
                  r.get("a.content", ""),
                  (TextEingabeDTO.TEXTTYPE) r.get("a.type", TextEingabeDTO.TEXTTYPE.STRENGTH));
            });
      }
    }
    return null;
  }

  public List<TextEingabeDTO> findTitleByArcId(String url) {
    if (url == null || url.isEmpty()) {
      return new ArrayList<>();
    }
    Integer arcId = getActualArcId(url);

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:TextEingabe)"
                        + System.lineSeparator()
                        + "match(d:Arc42) where Id(d)="
                        + arcId
                        + " AND (a.type ='TITLE') match"
                        + " (d)-[r:hasTextEingabe]->(a) return  Id(a), a.content, a.type",
                    parameters("id", arcId));
            return result.list(
                r -> {
                  TextEingabeDTO dto =
                      new TextEingabeDTO(
                          r.get("a.content", ""),
                          TextEingabeDTO.TEXTTYPE.valueOf(r.get("a.type", "STRENGTH")));
                  dto.setId(String.valueOf(r.get("Id(a)")));
                  return dto;
                });
          });
    }
  }

  @Override
  public void update(TextEingabeDTO t) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:TextEingabe)where Id(a)=$id"
                        + System.lineSeparator()
                        + "set a.content=$content, a.type=$type"
                        + " return count(*)",
                    parameters(
                        "id",
                        Integer.parseInt(t.getId()),
                        "content",
                        t.getContent(),
                        "type",
                        t.getType().name()));
            Record r = result.single();
            return r != null && !String.valueOf(r.get("count(*)")).isEmpty();
          });
    }
  }

  @Override
  public TextEingabeDTO save(TextEingabeDTO t) {
    // alternativ: arcid im dto speichern
    throw new UnsupportedOperationException("Saving associated type needs arcId");
  }
}