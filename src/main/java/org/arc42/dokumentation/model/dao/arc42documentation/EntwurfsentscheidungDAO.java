package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.EntwurfsentscheidungDTO;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

public class EntwurfsentscheidungDAO extends ARC42DAOAbstract<EntwurfsentscheidungDTO, String> {

  private static EntwurfsentscheidungDAO instance;

  public EntwurfsentscheidungDAO() {
    super();
  }

  public static EntwurfsentscheidungDAO getInstance() {
    if (instance == null) {
      instance = new EntwurfsentscheidungDAO();
    }
    return instance;
  }

  @Override
  public EntwurfsentscheidungDTO save(EntwurfsentscheidungDTO entwurfsentscheidungenDTO) {
    try (Session session = getDriver().session()) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + " create (a)-[r:hasEntwurfsentscheidung]->(e:Entwurfsentscheidung"
                          + " {entscheidung:$entscheidung,"
                          + " konsequenz:$konsequenz,"
                          + " begruendung:$begruendung,"
                          + " wichtigkeit: $wichtigkeit})"
                          + System.lineSeparator()
                          + "return Id(e), e.entscheidung, e.konsequenz, e.begruendung,"
                          + " e.wichtigkeit",
                      parameters(
                          "id",
                          arcId,
                          "entscheidung",
                          entwurfsentscheidungenDTO.getEntscheidung(),
                          "konsequenz",
                          entwurfsentscheidungenDTO.getKonsequenz(),
                          "begruendung",
                          entwurfsentscheidungenDTO.getBegruendung(),
                          "wichtigkeit",
                          entwurfsentscheidungenDTO.getWichtigkeit().toString()));
              EntwurfsentscheidungDTO dto = null;
              if (result != null) {
                Record r = result.single();
                dto =
                    new EntwurfsentscheidungDTO(
                        r.get("e.entscheidung").asString(),
                        r.get("e.konsequenz").asString(),
                        r.get("e.begruendung").asString(),
                        EntwurfsentscheidungDTO.Wichtigkeit.fromString(
                            r.get("e.wichtigkeit").asString()));
                dto.setId(String.valueOf(r.get("Id(e)")));
              }
              return dto;
            });
      }
    }
    return null;
  }

  @Override
  public Boolean delete(EntwurfsentscheidungDTO entwurfsentscheidungenDTO) {
    try (Session session = getDriver().session()) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Entwurfsentscheidung) where Id(a)=$id"
                        + System.lineSeparator()
                        + "detach delete a return count(*)",
                    parameters("id", Integer.parseInt(entwurfsentscheidungenDTO.getId())));
            Record r = result.single();
            return r != null && !String.valueOf(r.get("count(*)")).isEmpty();
          });
    }
  }

  @Override
  public void update(EntwurfsentscheidungDTO entwurfsentscheidungenDTO) {
    try (Session session = getDriver().session()) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Entwurfsentscheidung) where Id(a)=$id set a ="
                        + " {entscheidung:$entscheidung, konsequenz:$konsequenz,"
                        + " begruendung:$begruendung, wichtigkeit:$wichtigkeit}"
                        + System.lineSeparator()
                        + "return Id(a), a.entscheidung, a.konsequenz, a.begruendung,"
                        + " a.wichtigkeit",
                    parameters(
                        "id",
                        Integer.parseInt(entwurfsentscheidungenDTO.getId()),
                        "entscheidung",
                        entwurfsentscheidungenDTO.getEntscheidung(),
                        "konsequenz",
                        entwurfsentscheidungenDTO.getKonsequenz(),
                        "begruendung",
                        entwurfsentscheidungenDTO.getBegruendung(),
                        "wichtigkeit",
                        entwurfsentscheidungenDTO.getWichtigkeit().toString()));
            EntwurfsentscheidungDTO dto = null;
            if (result != null) {
              Record r = result.single();
              dto =
                  new EntwurfsentscheidungDTO(
                      r.get("a.entscheidung").asString(),
                      r.get("a.konsequenz").asString(),
                      r.get("a.begruendung").asString(),
                      EntwurfsentscheidungDTO.Wichtigkeit.fromString(
                          r.get("a.wichtigkeit").asString()));
              dto.setId(String.valueOf(r.get("Id(a)")));
            }
            return dto;
          });
    }
  }

  @Override
  public EntwurfsentscheidungDTO findById(String id) {
    if (id != null && !id.isEmpty()) {
      Integer idI = Integer.parseInt(id);
      try (Session session = getDriver().session()) {
        session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match (a:Entwurfsentscheidung) where Id(a)=$id return return Id(a),"
                          + " a.entscheidung, a.konsequenz, a.begruendung, a.wichtigkeit",
                      parameters("id", idI));
              EntwurfsentscheidungDTO dto = null;
              if (result != null) {
                Record r = result.single();
                dto =
                    new EntwurfsentscheidungDTO(
                        r.get("a.entscheidung").asString(),
                        r.get("a.konsequenz").asString(),
                        r.get("a.begruendung").asString(),
                        EntwurfsentscheidungDTO.Wichtigkeit.fromString(
                            r.get("a.wichtigkeit").asString()));
                dto.setId(String.valueOf(r.get("Id(a)")));
              }
              return dto;
            });
      }
    }
    return null;
  }

  @Override
  public List<EntwurfsentscheidungDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(arcId.toString());
  }

  public List<EntwurfsentscheidungDTO> findAllByArcId(String arcIdString) {
    if (arcIdString != null && !arcIdString.isEmpty()) {
      Integer arcId = Integer.parseInt(arcIdString);

      try (Session session = getDriver().session()) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Entwurfsentscheidung)"
                          + System.lineSeparator()
                          + "match(d:Arc42) where Id(d)=$id"
                          + System.lineSeparator()
                          + "match (d)-[r:hasEntwurfsentscheidung]->(a) return Id(a),"
                          + " a.entscheidung, a.konsequenz, a.begruendung, a.wichtigkeit",
                      parameters("id", arcId));
              return result.list(
                  r -> {
                    EntwurfsentscheidungDTO dto =
                        new EntwurfsentscheidungDTO(
                            r.get("a.entscheidung").asString(),
                            r.get("a.konsequenz").asString(),
                            r.get("a.begruendung").asString(),
                            EntwurfsentscheidungDTO.Wichtigkeit.fromString(
                                r.get("a.wichtigkeit").asString()));
                    dto.setId(String.valueOf(r.get("Id(a)")));
                    return dto;
                  });
            });
      }
    }
    return new ArrayList<>();
  }
}
