package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.KonzeptDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class KonzepteDAO extends ARC42DAOAbstract<KonzeptDTO, String> {

  private static KonzepteDAO instance;

  public KonzepteDAO() {
    super();
  }

  public static KonzepteDAO getInstance() {
    if (instance == null) {
      instance = new KonzepteDAO();
    }
    return instance;
  }

  @Override
  public KonzeptDTO save(KonzeptDTO konzeptDTO) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + " create (a)-[r:hatKonzept]->(s:Konzept {name:$name, text:$text,"
                          + " conceptCategories:$conceptCategories})"
                          + System.lineSeparator()
                          + "return Id(s), s.name, s.text, s.conceptCategories",
                      parameters(
                          "id",
                          arcId,
                          "name",
                          konzeptDTO.getName(),
                          "text",
                          konzeptDTO.getText(),
                          "conceptCategories",
                          konzeptDTO.getConceptCategories()));
              KonzeptDTO dto = null;
              if (result != null) {
                Record r = result.single();
                List<Object> conceptCategories = r.get("s.conceptCategories").asList();
                List<String> conceptCategoriesAsStrings =
                    conceptCategories.stream().map(Object::toString).toList();
                dto =
                    new KonzeptDTO(
                        r.get("s.name").asString(),
                        r.get("s.text").asString(),
                        conceptCategoriesAsStrings);
                dto.setId(String.valueOf(r.get("Id(s)")));
              }
              return dto;
            });
      }
    }
    return null;
  }

  @Override
  public void update(KonzeptDTO konzeptDTO) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Konzept) where Id(a)=$id"
                          + System.lineSeparator()
                          + " set a = {name:$name, text:$text,"
                          + " conceptCategories:$conceptCategories}"
                          + System.lineSeparator()
                          + "return Id(a), a.name, a.text, a.conceptCategories",
                      parameters(
                          "id",
                          Integer.parseInt(konzeptDTO.getId()),
                          "name",
                          konzeptDTO.getName(),
                          "text",
                          konzeptDTO.getText(),
                          "conceptCategories",
                          konzeptDTO.getConceptCategories()));
              KonzeptDTO dto = null;
              if (result != null) {
                Record r = result.single();
                List<Object> conceptCategories = r.get("a.conceptCategories").asList();
                List<String> conceptCategoriesAsStrings =
                    conceptCategories.stream().map(Object::toString).toList();
                dto =
                    new KonzeptDTO(
                        r.get("a.name").asString(),
                        r.get("a.text").asString(),
                        conceptCategoriesAsStrings);
                dto.setId(String.valueOf(r.get("Id(a)")));
              }
              return dto;
            });
      }
    }
  }

  @Override
  public List<KonzeptDTO> findAll(String url) {
    Integer arcId = getActualArcId(url);
    if (arcId == null) {
      return new ArrayList<>();
    }
    return findAllByArcId(arcId.toString());
  }

  public List<KonzeptDTO> findAllByArcId(String arcIdString) {
    if (arcIdString != null && !arcIdString.isEmpty()) {
      Integer arcId = Integer.parseInt(arcIdString);
      try (Session session = getDriver().session()) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Konzept)"
                          + System.lineSeparator()
                          + "match(d:Arc42) where Id(d)=$id"
                          + System.lineSeparator()
                          + "match (d)-[r:hatKonzept]->(a) return Id(a), a.name,"
                          + " a.text, a.conceptCategories",
                      parameters("id", arcId));
              return result.list(
                  r -> {
                    List<Object> conceptCategories = r.get("a.conceptCategories").asList();
                    List<String> conceptCategoriesAsStrings =
                        conceptCategories.stream().map(Object::toString).toList();

                    KonzeptDTO dto =
                        new KonzeptDTO(
                            r.get("a.name").asString(),
                            r.get("a.text").asString(),
                            conceptCategoriesAsStrings);
                    dto.setId(String.valueOf(r.get("Id(a)")));
                    return dto;
                  });
            });
      }
    }
    return new ArrayList<>();
  }

  @Override
  public Boolean delete(KonzeptDTO konzeptDTO) {
    try (Session session = getDriver().session()) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Konzept) where Id(a)=$id"
                        + System.lineSeparator()
                        + "detach delete a return count(*)",
                    parameters("id", Integer.parseInt(konzeptDTO.getId())));
            Record r = result.single();
            return r != null && !String.valueOf(r.get("count(*)")).isEmpty();
          });
    }
  }

  @Override
  public KonzeptDTO findById(String id) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(id);
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + " match (a)-[r:hatKonzepte]->(s:Konzepte)"
                          + System.lineSeparator()
                          + "return Id(s), s.konzepte",
                      parameters("id", arcId));
              KonzeptDTO dto = null;
              if (result != null) {
                Record r = (result.hasNext()) ? result.single() : null;
                dto = new KonzeptDTO("", "", new ArrayList<>());
                if (r != null) {
                  dto.setText(r.get("s.konzepte").asString());
                }
              }
              return dto;
            });
      }
    }
    return null;
  }
}
