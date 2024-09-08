package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.GlossarEintragDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class GlossarDAO extends ARC42DAOAbstract<GlossarEintragDTO, String> {

  private static GlossarDAO instance;

  public GlossarDAO() {
    super();
  }

  public static GlossarDAO getInstance() {
    if (instance == null) {
      instance = new GlossarDAO();
    }
    return instance;
  }

  @Override
  public GlossarEintragDTO save(GlossarEintragDTO glossarDTO) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + " create (a)-[r:hasGlossar]->(s:Glossar {begriff:$begriff,"
                          + " beschreibung:$beschreibung})"
                          + System.lineSeparator()
                          + "return Id(s), s.begriff, s.beschreibung",
                      parameters(
                          "id",
                          arcId,
                          "begriff",
                          glossarDTO.getBegriff(),
                          "beschreibung",
                          glossarDTO.getBeschreibung()));
              GlossarEintragDTO dto = null;
              if (result != null) {
                Record r = result.single();
                dto =
                    new GlossarEintragDTO(
                        r.get("s.begriff").asString(), r.get("s.beschreibung").asString());
                dto.setId(String.valueOf(r.get("Id(s)")));
              }
              return dto;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(GlossarEintragDTO glossarEintragDTO) {
    try (Session session = getDriver().session()) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Glossar) where Id(a)=$id"
                        + System.lineSeparator()
                        + "detach delete a return count(*)",
                    parameters("id", Integer.parseInt(glossarEintragDTO.getId())));
            Record r = result.single();
            return r != null && !String.valueOf(r.get("count(*)")).isEmpty();
          });
    }
  }

  @Override
  public void update(GlossarEintragDTO glossarDTO) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Glossar) where Id(a)=$id set a = {begriff:$begriff,"
                        + " beschreibung:$beschreibung}"
                        + System.lineSeparator()
                        + "return Id(a), a.begriff, a.beschreibung",
                    parameters(
                        "id",
                        Integer.parseInt(glossarDTO.getId()),
                        "begriff",
                        glossarDTO.getBegriff(),
                        "beschreibung",
                        glossarDTO.getBeschreibung()));
            GlossarEintragDTO dto = null;
            if (result != null) {
              Record r = result.single();
              dto =
                  new GlossarEintragDTO(
                      r.get("a.begriff").asString(), r.get("a.beschreibung").asString());
              dto.setId(String.valueOf(r.get("Id(a)")));
            }
            return dto;
          });
    }
  }

  @Override
  public List<GlossarEintragDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(arcId.toString());
  }

  public List<GlossarEintragDTO> findAllByArcId(String arcIdString) {
    if (arcIdString != null && !arcIdString.isEmpty()) {
      Integer arcId = Integer.parseInt(arcIdString);

      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Glossar)"
                          + System.lineSeparator()
                          + "match(d:Arc42) where Id(d)=$id"
                          + System.lineSeparator()
                          + "match (d)-[r:hasGlossar]->(a) return Id(a), a.begriff,"
                          + " a.beschreibung",
                      parameters("id", arcId));
              return result.list(
                  r -> {
                    GlossarEintragDTO glossarDTO =
                        new GlossarEintragDTO(
                            r.get("a.begriff").asString(), r.get("a.beschreibung").asString());
                    glossarDTO.setId(String.valueOf(r.get("Id(a)")));
                    return glossarDTO;
                  });
            });
      }
    }
    return new ArrayList<>();
  }

  @Override
  public GlossarEintragDTO findById(String id) {
    if (id != null && !id.isEmpty()) {
      Integer idI = Integer.parseInt(id);
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:Glossar) where Id(a)=$id return return Id(a), a.begriff,"
                          + " a.beschreibung",
                      parameters("id", idI));
              GlossarEintragDTO dto = null;
              if (result != null) {
                Record r = result.single();
                new GlossarEintragDTO(
                    r.get("a.begriff").asString(), r.get("a.beschreibung").asString());
                dto.setId(String.valueOf(r.get("Id(a)")));
              }
              return dto;
            });
      }
    }
    return null;
  }
}
