package org.arc42.dokumentation.model.dao.general;

import org.arc42.dokumentation.control.service.Neo4jconnection;
import org.arc42.dokumentation.model.dto.general.DbUserDTO;
import org.arc42.dokumentation.view.util.data.Credentials;

public class UserDAO {
  private static UserDAO instance = null;

  private UserDAO() {}

  public static UserDAO getInstance() {
    if (instance == null) {
      instance = new UserDAO();
      Credentials.readEnvironment();
    }
    return instance;
  }

  public void setUser(String username, String hashedPassword) {
    String uri = Credentials.URL;
    try (Neo4jconnection connection =
        new Neo4jconnection(uri, Credentials.USER, Credentials.PASSWORD)) {
      connection.setUserData(username, hashedPassword);
    }
  }

  public boolean existUser(String username) {
    String uri = Credentials.URL;
    try (Neo4jconnection connection =
        new Neo4jconnection(uri, Credentials.USER, Credentials.PASSWORD)) {
      return connection.userExist(username);
    }
  }

  public DbUserDTO getUser(String username, String password) {
    // System.out.println("Plain: "+password);
    String uri = Credentials.URL;
    try (Neo4jconnection connection =
        new Neo4jconnection(uri, Credentials.USER, Credentials.PASSWORD)) {
      return connection.getUserData(username, password);
    }
  }

  public void resetToDefaultExampleDB() {
    String uri = Credentials.URL;
    try (Neo4jconnection connection =
        new Neo4jconnection(uri, Credentials.USER, Credentials.PASSWORD)) {
      connection.resetToDefaultExampleDB();
    }
  }
}
