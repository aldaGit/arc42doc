package org.arc42.dokumentation.model.dao.arc42documentation;

import static java.lang.Integer.parseInt;
import static org.arc42.analyse.model.util.ResultChecker.resultNotEmpty;
import static org.neo4j.driver.Values.parameters;

import java.util.List;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class AnalyseResultDAO extends ARC42DAOAbstract<String, String> {

  private static AnalyseResultDAO instance;

  public AnalyseResultDAO() {
    super();
  }

  public static AnalyseResultDAO getInstance() {
    if (instance == null) {
      instance = new AnalyseResultDAO();
    }
    return instance;
  }

  @Override
  public String findById(String id) {
    Integer arcId;

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      if (id != null) {
        arcId = parseInt(id);
      } else {
        arcId = getActualArcId(null);
      }
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + " match (a)-[r:analyseergebnisse]->(s:AnalyseErgebnis)"
                          + System.lineSeparator()
                          + "return Id(s), s.result",
                      parameters("id", arcId));
              String aResult = null;
              if (resultNotEmpty(result)) {
                Record r = (result.hasNext()) ? result.single() : null;
                if (r != null) {
                  aResult = r.get("s.result").asString();
                }
              }
              return aResult;
            });
      }
    }
    return null;
  }

  @Override
  public String save(String t) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'save'");
  }

  @Override
  public Boolean delete(String t) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }

  @Override
  public void update(String t) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @Override
  public List<String> findAll(String url) {
    // ToDo: test this method

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction ->
              (List<String>)
                  transaction.run(
                      "match(a:Arc42) where Id(a)=*"
                          + System.lineSeparator()
                          + " match (a)-[r:analyseergebnisse]->(s:AnalyseErgebnis)"
                          + System.lineSeparator()
                          + "return Id(s), s.result",
                      parameters()));
    }
  }
}
