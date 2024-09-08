package org.arc42.dokumentation.view.util.data;

public class Credentials {
  public static String DATABASE;
  public static String URL;
  public static String PROJECT_ID;
  public static String USER;
  public static String PASSWORD;

  private Credentials() {
    throw new IllegalStateException("Utility class");
  }

  public static void readEnvironment() {
    String env_url = System.getenv("NEO4J_URL");
    // System.out.println(env_url);
    String env_user = System.getenv("NEO4J_USER");
    String env_passwort = System.getenv("NEO4J_PASSWORD");
    String env_database = System.getenv("NEO4J_DATABASE");
    if (env_passwort != null
        && env_user != null
        && env_url != null
        && env_database != null
        && env_database != ""
        && env_passwort != ""
        && !env_user.isEmpty()
        && !env_url.isEmpty()) {
      URL = env_url;
      DATABASE = env_database;
      USER = env_user;
      PASSWORD = env_passwort;
      System.out.print(URL + "  " + DATABASE + "  " + PASSWORD + "  " + USER);
    } else {
      DATABASE = "neo4j";
      URL = "bolt://localhost:7687";
      PROJECT_ID = "yourProjectID"; // from GoogleBigQuery
      USER = "neo4j";
      PASSWORD = "yourPassword";
    }
  }
}
