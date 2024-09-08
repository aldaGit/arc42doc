package org.arc42.dokumentation.control.service;

import static org.neo4j.driver.Values.parameters;

import org.arc42.dokumentation.model.dto.general.DbUserDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.*;
import org.neo4j.driver.exceptions.ClientException;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Neo4jconnection implements AutoCloseable {
  private final Driver driver;

  public Neo4jconnection(String uri, String user, String password) {
    driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
  }

  public void close() {
    driver.close();
  }

  public org.neo4j.driver.Driver getDriver() {
    return driver;
  }

  public boolean userExist(final String username) {
    try (Session session = driver.session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.readTransaction(
          tx -> {
            Result result1 =
                tx.run(
                    "MATCH(a:Developer {devname:$username}) RETURN count(a)>0 AS result;",
                    parameters("username", username));
            return result1.single().get(0).asBoolean();
          });
    }
  }

  public DbUserDTO getUserData(final String username, final String password) {
    try (Session session = driver.session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      String result =
          session.writeTransaction(
              tx -> {
                Result result1 =
                    tx.run(
                        "MATCH(a:Developer {devname:$username}) RETURN" + " a.password",
                        parameters("username", username));
                if (result1.peek() == null) {
                  throw new IllegalArgumentException("User not found!");
                }
                return result1.single().get(0).asString();
              });
      DbUserDTO userdto = DbUserDTO.getInstance();
      userdto.setUsername(username);
      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      if (passwordEncoder.matches(password, result.trim())) {
        return userdto;
      } else {
        throw new IllegalArgumentException("NEIINN");
      }
    }
  }

  public void setUserData(final String username, final String password) {
    try (Session session = driver.session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          tx -> {
            Result result1 =
                tx.run(
                    "CREATE(d:Developer {devname:$username, password:$password}) return d.name",
                    parameters("username", username, "password", password));
            return result1.single().get(0).asString();
          });
    }
  }

  public void resetToDefaultExampleDB() {
    try (Session session = driver.session(SessionConfig.forDatabase(Credentials.DATABASE))) {

      session.run("MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n, r;");
      try {

        session.run(
            "CREATE CONSTRAINT ON (node:`UNIQUE IMPORT LABEL`) ASSERT (node.`UNIQUE IMPORT ID`) IS"
                + " UNIQUE;");
      } catch (ClientException e) {
        LoggerFactory.getLogger(getClass()).info("Constraint already exists, proceeding");
      }

      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 1}) SET n +="
              + " {zuletztAktu:\"2023-08-17\", prioSkala:\"\", ansprechpartner:\"Nein\","
              + " notiz:\"\", eintrittswahrscheinlichkeit:\"keine\", orgaEinheit:\"\","
              + " wirkung:\"\", erfasser:\"Marko\", anforderung:\"Risiko1\","
              + " schadenshoehe:\"hoch\", eintrittsdatum:\"2023-08-14\", geschaedigte:\"Nein\","
              + " status:\"unbearbeitet\"} SET n:Risiko;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 2}) SET n +="
              + " {zuletztAktu:\"2023-08-14\", prioSkala:\"mittel\", ansprechpartner:\"\","
              + " notiz:\"\", eintrittswahrscheinlichkeit:\"niedrig\", orgaEinheit:\"\","
              + " wirkung:\"\", erfasser:\"nein\", anforderung:\"hallo\", schadenshoehe:\"mittel\","
              + " eintrittsdatum:\"2023-08-14\", geschaedigte:\"\", status:\"unbearbeitet\"} SET"
              + " n:Risiko;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 11}) SET n += {tkontext:\"Hier wird"
              + " technisch klar, wo der Kontext abgegrenzt wird.\"} SET"
              + " n:TechnischhKontext:Kontext;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 150}) SET n +="
              + " {name:\"Digitalisierung einer Grundschule in Bonn\"} SET n:Arc42;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 6}) SET n +="
              + " {randbedingung:\"Marius\", hintergrund:\"Marius muss anwesend sein, um bei der"
              + " Koordination des Projekts zu helfen.\"} SET n:OrganisatorischRandbedingung;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 3}) SET n +="
              + " {randbedingung:\"Stromversorgung\", hintergrund:\"Die Stromversorgung muss 200kw"
              + " bereitstellen.\"} SET n:TechnischeRandbedingung;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 4}) SET n +="
              + " {qualitaetskriterium:\"Funktionalität\"} SET n:Qualitaetskriterium;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 5}) SET n +="
              + " {qualitaetskriterium:\"Effizienz\"} SET n:Qualitaetskriterium;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 7}) SET n +="
              + " {qualitaetskriterium:\"Zuverlässigkeit\"} SET n:Qualitaetskriterium;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 8}) SET n +="
              + " {qualitaetskriterium:\"Benutzbarkeit\"} SET n:Qualitaetskriterium;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 12}) SET n += {type:\"STRENGTH\","
              + " content:\"Kundenvielfalt\"} SET n:TextEingabe;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 13}) SET n += {type:\"OPPORTUNITY\","
              + " content:\"Neue Industrien\"} SET n:TextEingabe;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 14}) SET n += {glossar:\"Begriff -"
              + " Aufschlüsselung\"} SET n:Glossar;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 10}) SET n += {fkontext:\"Fachlich"
              + " ist hier der Kontext abgegrenzt. \"} SET n:Kontext:FachlichKontext;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 9}) SET n += {erlaeuterung:\"Jeden"
              + " Mittwoch findet ein Weekly Meeting mit dem Kunden statt.\", konvention:\"Weekly -"
              + " Mittwochs\"} SET n:Konvention;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 158}) SET n += {konzepte:\"Hallo,"
              + " Konzept!\"} SET n:Konzepte;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 0}) SET n += {password:\"Test1234\","
              + " devname:\"Tobi\"} SET n:Developer;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 77}) SET n +="
              + " {password:\"Test1234\", devname:\"john\"} SET n:Developer;");
      session.run(
          "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 99}) SET n +="
              + " {password:\"OrgaPaul\", devname:\"OrgaPaul\"} SET n:Developer;");
      // session.run(
      //  "CREATE (n:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`: 139}) SET n += {password:\"james\","
      //    + " devname:\"james\"} SET n:Developer;");
      // ToDo: Add hashing to these statements @John
      session.run(
          "UNWIND [{start: {_id:150}, end: {_id:14}, properties:{}}] AS row MATCH (start:`UNIQUE"
              + " IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id}) MATCH (end:`UNIQUE IMPORT"
              + " LABEL`{`UNIQUE IMPORT ID`: row.end._id}) CREATE (start)-[r:hatGlossar]->(end) SET"
              + " r += row.properties;");

      session.run(
          "UNWIND [{start: {_id:150}, end: {_id:3}, properties:{}}] AS row MATCH (start:`UNIQUE"
              + " IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id}) MATCH (end:`UNIQUE IMPORT"
              + " LABEL`{`UNIQUE IMPORT ID`: row.end._id}) CREATE"
              + " (start)-[r:hatTechnischRandbedingung]->(end) SET r += row.properties;");

      session.run(
          "UNWIND [{start: {_id:150}, end: {_id:10}, properties:{}}] AS row MATCH (start:`UNIQUE"
              + " IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id}) MATCH (end:`UNIQUE IMPORT"
              + " LABEL`{`UNIQUE IMPORT ID`: row.end._id}) CREATE (start)-[r:kontext]->(end) SET r"
              + " += row.properties;");
      session.run(
          "UNWIND [{start: {_id:150}, end: {_id:1}, properties:{}}, {start: {_id:150}, end:"
              + " {_id:2}, properties:{}}] AS row MATCH (start:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT"
              + " ID`: row.start._id}) MATCH (end:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`:"
              + " row.end._id}) CREATE (start)-[r:hasRisiko]->(end) SET r += row.properties;");
      session.run(
          "UNWIND [{start: {_id:150}, end: {_id:6}, properties:{}}] AS row MATCH (start:`UNIQUE"
              + " IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id}) MATCH (end:`UNIQUE IMPORT"
              + " LABEL`{`UNIQUE IMPORT ID`: row.end._id}) CREATE"
              + " (start)-[r:hatOrganisatorischRandbedingung]->(end) SET r += row.properties;");
      session.run(
          "UNWIND [{start: {_id:150}, end: {_id:12}, properties:{}}, {start: {_id:150}, end:"
              + " {_id:13}, properties:{}}] AS row MATCH (start:`UNIQUE IMPORT LABEL`{`UNIQUE"
              + " IMPORT ID`: row.start._id}) MATCH (end:`UNIQUE IMPORT LABEL`{`UNIQUE IMPORT ID`:"
              + " row.end._id}) CREATE (start)-[r:hasTextEingabe]->(end) SET r += row.properties;");

      session.run(
          "UNWIND [{start: {_id:150}, end: {_id:9}, properties:{}}] AS row MATCH (start:`UNIQUE"
              + " IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id}) MATCH (end:`UNIQUE IMPORT"
              + " LABEL`{`UNIQUE IMPORT ID`: row.end._id}) CREATE (start)-[r:hatKonvention]->(end)"
              + " SET r += row.properties;");
      session.run(
          "UNWIND [{start: {_id:150}, end: {_id:11}, properties:{}}] AS row MATCH (start:`UNIQUE"
              + " IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id}) MATCH (end:`UNIQUE IMPORT"
              + " LABEL`{`UNIQUE IMPORT ID`: row.end._id}) CREATE (start)-[r:kontext]->(end) SET r"
              + " += row.properties;");
      session.run(
          "UNWIND [{start: {_id:150}, end: {_id:158}, properties:{}}] AS row MATCH (start:`UNIQUE"
              + " IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id}) MATCH (end:`UNIQUE IMPORT"
              + " LABEL`{`UNIQUE IMPORT ID`: row.end._id}) CREATE (start)-[r:hatKonzepte]->(end)"
              + " SET r += row.properties;");
      session.run(
          "UNWIND [{start: {_id:139}, end: {_id:150}, properties:{}}] AS row MATCH (start:`UNIQUE"
              + " IMPORT LABEL`{`UNIQUE IMPORT ID`: row.start._id}) MATCH (end:`UNIQUE IMPORT"
              + " LABEL`{`UNIQUE IMPORT ID`: row.end._id}) CREATE (start)-[r:create]->(end) SET r"
              + " += row.properties;");
    }
  }
}
