package evaluation;

import static org.neo4j.driver.Values.parameters;

import org.arc42.dokumentation.control.service.Neo4jconnection;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

public class TestUtil {

  /** creates a new documentation for testing purposes in the database */
  public static DokuNameDTO createTestDoku(DokuNameDTO dto) {
    String uri = Credentials.URL;
    Neo4jconnection connection = new Neo4jconnection(uri, Credentials.USER, Credentials.PASSWORD);
    Driver driver = connection.getDriver();
    try (Session session = driver.session()) {
      return session.writeTransaction(
          tx -> {
            Result result =
                tx.run(
                    "create (d: Arc42 {name: $name})"
                        + System.lineSeparator()
                        + "create (u)-[r:create]->(d) return Id(d)",
                    parameters("name", dto.getName(), "username", "testuser"));
            Record r = result.single();
            return new DokuNameDTO(dto.getName(), String.valueOf(r.get("Id(d)")));
          });
    }
  }

  /** deletes the documentation and all its related nodes */
  public static void deleteTestDoku(DokuNameDTO doku) {
    String uri = Credentials.URL;
    Neo4jconnection connection = new Neo4jconnection(uri, Credentials.USER, Credentials.PASSWORD);
    Driver driver = connection.getDriver();
    try (Session session = driver.session()) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "MATCH (n)-[r]-(m)"
                        + System.lineSeparator()
                        + "WHERE Id(n)=$id"
                        + System.lineSeparator()
                        + "DELETE n, r, m",
                    parameters("id", Integer.parseInt(doku.getId())));
            return result;
          });
    }
  }
}
