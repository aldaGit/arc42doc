package org.arc42.dokumentation.model.dao.arc42documentation;

import com.vaadin.flow.component.UI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.arc42.dokumentation.control.service.Neo4jconnection;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Driver;

public abstract class ARC42DAOAbstract<T, I> implements ARC42DAOI<T, I> {

  private Driver driver;
  private Matcher m;
  Neo4jconnection connection;

  protected ARC42DAOAbstract() {
    Credentials.readEnvironment();
    String uri = Credentials.URL;
    connection = new Neo4jconnection(uri, Credentials.USER, Credentials.PASSWORD);
    this.driver = connection.getDriver();
  }

  public Driver getDriver() {
    if (driver == null) {
      Credentials.readEnvironment();
      String uri = Credentials.URL;
      connection = new Neo4jconnection(uri, Credentials.USER, Credentials.PASSWORD);
      this.driver = connection.getDriver();
    }

    return driver;
  }

  public Integer getActualArcId(String id) {
    if (id == null) {
      Pattern digitsInURL = Pattern.compile("(?<=\\/)\\d+(?=\\/)");
      /*
       * Actual Pattern: (?<=\/)\d+(?=\/)
       * This pattern matches one or more digits that are surrounded by forward slashes.
       * It also matches the digits if they are preceded by two forward slashes
       * and followed by another forward slash.
       * "http://arc42server.de/123/" -> 123
       * "https://arc42server.de/9876/" -> 9876
       * "ftp://arc42server.de/42/" -> 42
       * "sftp://arc42server.de/999/" -> 999
       * "https://arc42server.de/abc123/def456/789/" -> 789
       */

      UI.getCurrent()
          .getPage()
          .executeJs("return window.location.pathname;")
          .then(String.class, location -> m = digitsInURL.matcher(location));
      if (m == null) {
        m = digitsInURL.matcher(UI.getCurrent().getInternals().getActiveViewLocation().getPath());
      }
      String actualArcId = null;

      // Matcher
      while (m.find()) {
        actualArcId = m.group(0);
      }

      m = null;
      return Integer.parseInt(actualArcId);
    }
    return Integer.parseInt(id);
  }
}