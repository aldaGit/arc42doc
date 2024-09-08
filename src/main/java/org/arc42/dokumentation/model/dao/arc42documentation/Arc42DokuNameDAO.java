package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.arc42.analyse.model.util.ResultChecker.resultNotEmpty;
import static org.neo4j.driver.Values.parameters;

import com.vaadin.flow.server.VaadinSession;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.arc42.dokumentation.view.util.data.Roles;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.SessionConfig;

public class Arc42DokuNameDAO extends ARC42DAOAbstract<DokuNameDTO, String> {

  private static Arc42DokuNameDAO instance = null;

  public static Arc42DokuNameDAO getInstance() {
    if (instance == null) {
      Credentials.readEnvironment();
      instance = new Arc42DokuNameDAO();
    }
    return instance;
  }

  @Override
  public DokuNameDTO save(DokuNameDTO name) {
    String username = (String) VaadinSession.getCurrent().getAttribute(Roles.CURRENTUSER);
    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          tx -> {
            Result result =
                tx.run(
                    "match(u:Developer {devname:$username})"
                        + System.lineSeparator()
                        + "create (d: Arc42 {name: $name}) "
                        + System.lineSeparator()
                        + "create (u)-[r:create]->(d) return Id(d)",
                    parameters("name", name.getName(), "username", username));
            Record r = result.single();
            return new DokuNameDTO(name.getName(), String.valueOf(r.get("Id(d)")));
          });
    }
  }

  public Boolean delete1(DokuNameDTO s) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Arc42 {name:$name}) where Id(a)=$id"
                        + System.lineSeparator()
                        + "detach delete a return count(*)",
                    parameters("name", s.getName(), "id", Integer.parseInt(s.getId())));
            Record r = result.single();
            return r != null
                && !r.get("count(*)").isNull()
                && !String.valueOf(r.get("count(*)")).isEmpty();
          });
    }
  }

  @Override
  public Boolean delete(DokuNameDTO s) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(a:Arc42 {name:$name}) where Id(a)=$id match (a)-[r]->(c)"
                        + System.lineSeparator()
                        + "detach delete r,a,c return count(*)",
                    parameters("name", s.getName(), "id", Integer.parseInt(s.getId())));
            Record r = result.single();
            return r != null
                && !r.get("count(*)").isNull()
                && !String.valueOf(r.get("count(*)")).isEmpty();
          });
    }
  }

  @Override
  public void update(DokuNameDTO s) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            DokuNameDTO dto = null;
            if (s.getId() != null) {
              Result result =
                  transaction.run(
                      "match (a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "set a.name=$name return Id(a), a.name",
                      parameters("id", Integer.parseInt(s.getId()), "name", s.getName()));
              if (resultNotEmpty(result)) {
                Record r = result.single();
                dto = new DokuNameDTO(r.get("a.name").asString(), String.valueOf(r.get("Id(a)")));
              }
            }
            return dto;
          });
    }
  }

  @Override
  public List<DokuNameDTO> findAll(String url) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result = transaction.run("match(a:Arc42) return a.name, Id(a)");
            return result.list(
                r -> new DokuNameDTO(r.get("a.name").asString(), String.valueOf(r.get("Id(a)"))));
          });
    }
  }

  public List<DokuNameDTO> findAll() {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result = transaction.run("match(a:Arc42) return a.name, Id(a)");
            return result.list(
                r -> new DokuNameDTO(r.get("a.name").asString(), String.valueOf(r.get("Id(a)"))));
          });
    }
  }

  @Override
  public DokuNameDTO findById(String arc42IdString) {
    if (arc42IdString != null && !arc42IdString.isEmpty()) {
      Integer arc42Id = Integer.parseInt(arc42IdString);
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(d:Arc42) where Id(d)=$arc return d.name, Id(d)",
                      parameters("arc", arc42Id));
              Record r = result.single();
              if (r == null) {
                return null;
              }

              return new DokuNameDTO(r.get("d.name").asString(), String.valueOf(r.get("Id(d)")));
            });
      }
    }
    return null;
  }
}
