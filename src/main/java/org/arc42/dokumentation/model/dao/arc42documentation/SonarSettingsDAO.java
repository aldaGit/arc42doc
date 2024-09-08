package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.Collections;
import java.util.List;
import org.arc42.analyse.model.dto.sonar.SonarSettingsDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Query;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class SonarSettingsDAO extends ARC42DAOAbstract<SonarSettingsDAO, String> {
  private static SonarSettingsDAO instance;

  private SonarSettingsDAO() {}

  public static SonarSettingsDAO getInstance() {
    if (instance == null) {
      instance = new SonarSettingsDAO();
    }
    return instance;
  }

  public SonarSettingsDTO getSettings() {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          tx -> {
            Result result =
                tx.run(
                    "MATCH (a:Arc42)-[:HAS_SETTINGS]-> (s:Sonar) WHERE Id(a)=$id RETURN s.url,"
                        + " s.token, s.component",
                    parameters("id", getActualArcId(null)));
            if (!result.hasNext()) return null;
            else {
              Record rec = result.single();
              return new SonarSettingsDTO(
                  rec.get("s.url").asString(),
                  rec.get("s.token").asString(),
                  rec.get("s.component").asString());
            }
          });
    }
  }

  public SonarSettingsDTO save(SonarSettingsDTO newSettings) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          tx -> {
            Query query =
                new Query(
                    "MATCH(n:Arc42) WHERE Id(n)=$id "
                        + "MERGE(n)-[r:HAS_SETTINGS]->(s:Sonar) "
                        + "ON CREATE SET s.url = $url, s.token = $token, s.component=$component "
                        + "ON MATCH SET s.url = $url, s.token =$token, s.component=$component "
                        + "RETURN s.url, s.token, s.component",
                    parameters(
                        "id",
                        getActualArcId(null),
                        "url",
                        newSettings.getUrl(),
                        "token",
                        newSettings.getToken(),
                        "component",
                        newSettings.getComponent()));
            Result result = tx.run(query);
            Record rec = result.single();
            return new SonarSettingsDTO(
                rec.get("s.url").asString(),
                rec.get("s.token").asString(),
                rec.get("s.component").asString());
          });
    }
  }

  @Override
  public SonarSettingsDAO save(SonarSettingsDAO sonarSettingsDAO) {
    return null;
  }

  @Override
  public Boolean delete(SonarSettingsDAO sonarSettingsDAO) {
    return null;
  }

  @Override
  public void update(SonarSettingsDAO sonarSettingsDAO) {}

  @Override
  public List<SonarSettingsDAO> findAll(String url) {
    return Collections.emptyList();
  }

  @Override
  public SonarSettingsDAO findById(String id) {
    return null;
  }
}
