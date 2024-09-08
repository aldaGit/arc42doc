package org.arc42.analyse.control.service;

import static org.neo4j.driver.Values.parameters;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.arc42.analyse.model.dto.DesignDecisionDTOForSA;
import org.arc42.analyse.model.dto.GitHubSecurityAdvisoryDTO;
import org.arc42.analyse.model.util.DateFormater;
import org.arc42.dokumentation.model.dto.documentation.HardwareDTO;
import org.arc42.dokumentation.model.dto.documentation.InterfaceDTO;
import org.arc42.dokumentation.model.dto.documentation.LoesungsStrategieDTO;
import org.arc42.dokumentation.model.dto.documentation.MeetingDTO;
import org.arc42.dokumentation.model.dto.documentation.QualityDTO;
import org.arc42.dokumentation.model.dto.documentation.QualityGoalDTO;
import org.arc42.dokumentation.model.dto.documentation.QualityScenarioDTO;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.SessionConfig;

public class Neo4jConnectForNeo4j implements AutoCloseable {

  private final Driver driver;

  public Neo4jConnectForNeo4j(String uri, String user, String password) {
    driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
  }

  public void close() {
    driver.close();
  }

  public Driver getDriver() {
    return driver;
  }

  public String getTitleByArcId(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id return a.name", parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("a.name").asString();
          }
          return "";
        });
  }

  public int countDocumentedElemente(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match (a)-[r]-(b) return count(b)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(b)").asInt();
          }
          return 0;
        });
  }

  public int countAufgabenstellung(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match (d:Arc42) where Id(d)=$id match"
                      + " (d)-[r:hasRequirement]->(a:Aufgabenstellung) return count(a)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(a)").asInt();
          }
          return 0;
        });
  }

  public int countQualitaetsZiele(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  " match (d:Arc42) where Id(d)=$id match"
                      + " (d)-[r:hasQualityGoal]->(a:Qualitaetsziel) return count(a)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(a)").asInt();
          }
          return 0;
        });
  }

  public int countStakeholder(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match (s:Stakeholder)<-[r:hasStakeholder]-(a)"
                      + " return count(s)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(s)").asInt();
          }
          return 0;
        });
  }

  public int countTechnischRandbedingung(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match (d:Arc42) where Id(d)=$id"
                      + " match(d)-[r:hatTechnischRandbedingung]->(a:TechnischeRandbedingung)"
                      + " return count(a)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(a)").asInt();
          }
          return 0;
        });
  }

  public int countOrganisatorischRandbedingung(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match (d:Arc42) where Id(d)=$id match"
                      + " (d)-[r:hatOrganisatorischRandbedingung]->(a:OrganisatorischRandbedingung)"
                      + " return count(a)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(a)").asInt();
          }
          return 0;
        });
  }

  public int countKonventionRandbedingung(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match (d:Arc42) where Id(d)=$id match (d)-[r:hatKonvention]->(a:Konvention)"
                      + " return count(a)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(a)").asInt();
          }
          return 0;
        });
  }

  public int countFachlicherKontext(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match"
                      + " (a)-[r:kontext]->(s:Kontext:FachlichKontext) return count(s)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(s)").asInt();
          }
          return 0;
        });
  }

  public String getFachlicherKontext(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match"
                      + " (a)-[r:kontext]->(s:Kontext:FachlichKontext) return s.fkontext",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("s.fkontext").asString();
          }
          return "";
        });
  }

  public int countTechnischerKontext(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "    match(a:Arc42) where Id(a)=$id match"
                      + " (a)-[r:kontext]->(s:Kontext:TechnischhKontext) return count(s)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(s)").asInt();
          }
          return 0;
        });
  }

  public String getTechnischerKontext(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match"
                      + " (a)-[r:kontext]->(s:Kontext:TechnischhKontext) return s.tkontext",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("s.tkontext").asString();
          }
          return "";
        });
  }

  public int countLoesungsStrategie(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match (a)-[r:hatLoesung]->(s:LoesungStrategie)"
                      + " return count(s)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(s)").asInt();
          }
          return 0;
        });
  }

  public String getLoesungsStrategie(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match (a)-[r:hatLoesung]->(s:LoesungStrategie)"
                      + " return s.loesung",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("s.loesung").asString();
          }
          return "";
        });
  }

  // Fiona:
  public LoesungsStrategieDTO getLoesungsStrategieFiona(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id "
                      + "match (a)-[r:hatLoesung]->(s:LoesungStrategie) "
                      + "return s.strategy, s.nId",
                  parameters("id", arcId));
          List<Record> records = result.list();
          for (Record r : records) {
            LoesungsStrategieDTO dto;
            if (r.get("l.nId").isNull()) {
              dto = new LoesungsStrategieDTO(r.get("l.strategy").asString());
              return dto;
            }
          }
          return null;
        });
  }

  // End Fiona
  public int countBausteinDiagram(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match (a)-[r:bausteinsicht]->(s:Image:Baustein)"
                      + " return count(s)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(s)").asInt();
          }
          return 0;
        });
  }

  public String getBausteinBeschreibung(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match (a)-[r:bausteinsicht]->(s:Image:Baustein)"
                      + " return s.description",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("s.description").asString();
          }
          return "";
        });
  }

  public int countLaufzeitDiagram(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match (a)-[r:laufzeitsicht]->(s:Image:Laufzeit)"
                      + " return count(s)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(s)").asInt();
          }
          return 0;
        });
  }

  public String getLaufzeitBeschreibung(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match (a)-[r:laufzeitsicht]->(s:Image:Laufzeit)"
                      + " return s.description",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("s.description").asString();
          }
          return "";
        });
  }

  public int countVerteilungDiagram(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match"
                      + " (a)-[r:verteilungssicht]->(s:Image:Verteilung) return count(s)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(s)").asInt();
          }
          return 0;
        });
  }

  public String getVerteilungBeschreibung(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match"
                      + " (a)-[r:verteilungssicht]->(s:Image:Verteilung) return s.description",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("s.description").asString();
          }
          return "";
        });
  }

  public int countKonzepte(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match (a)-[r:hatKonzepte]->(s:Konzepte) return"
                      + " count(s)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(s)").asInt();
          }
          return 0;
        });
  }

  public String getKonzepte(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match (a)-[r:hatKonzepte]->(s:Konzepte) return"
                      + " s.konzepte",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("s.konzepte").asString();
          }
          return "";
        });
  }

  public int countArchitekturEntscheidung(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match"
                      + " (a)-[r:hatEntscheidung]->(s:EntwurfsEntscheidung) return count(s)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(s)").asInt();
          }
          return 0;
        });
  }

  public String getArchitekturEntscheidung(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match"
                      + " (a)-[r:hatEntscheidung]->(s:EntwurfsEntscheidung) return s.entscheidung",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("s.entscheidung").asString();
          }
          return "";
        });
  }

  public int countDesignDecision(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match"
                      + " (a)-[r:bausteinsicht]->(s:Image:Baustein)-[d:hatDecision]->(v:DesignDecision)"
                      + " return count(v)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(v)").asInt();
          }
          return 0;
        });
  }

  public List<DesignDecisionDTOForSA> findDesignDecisionForArcId(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match"
                      + " (a)-[r:bausteinsicht]->(s:Image:Baustein)-[d:hatDecision]->(v:DesignDecision)"
                      + " return v.id, v.title",
                  parameters("id", arcId));
          if (result != null) {
            return new ArrayList<>(
                result.list(
                    single ->
                        new DesignDecisionDTOForSA(
                            single.get("v.id").asString(), single.get("v.title").asString())));
          }
          return null;
        });
  }

  public String getDecisionIdForRationaleId(Integer arcId, String rationaleId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) match(z:Rationale) where Id(a)=$id and z.id=$rationaleId match"
                      + " (a)-[r:bausteinsicht]->(s:Image:Baustein)-[d:hatDecision]->(v:DesignDecision)-[c:wirdBegründetDurch]->(z)"
                      + " return v.id",
                  parameters("id", arcId, "rationaleId", rationaleId));
          if (result != null) {
            return result.single().get("v.id").asString();
          }
          return "";
        });
  }

  public String getReferencedAlternativeId(String ddId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(d:DesignDecision) where d.id=$id match"
                      + " (d)-[a:ausgewaehlt]->(c:DDAlternative) return c.id",
                  parameters("id", ddId));
          if (result != null) {
            return result.single().get("c.id").asString();
          }
          return "";
        });
  }

  public String getReferencedAlternativeTitle(String ddId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(d:DesignDecision) where d.id=$id match"
                      + " (d)-[a:ausgewaehlt]->(c:DDAlternative) return c.name",
                  parameters("id", ddId));
          if (result != null) {
            return result.single().get("c.name").asString();
          }
          return "";
        });
  }

  public String getDesignDecisionTitle(String ddId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(d:DesignDecision) where d.id=$id return d.title", parameters("id", ddId));
          if (result != null) {
            return result.single().get("d.title").asString();
          }
          return "";
        });
  }

  public List<String> getRationaleIdForArcId(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match"
                      + " (a)-[r:bausteinsicht]->(s:Image:Baustein)-[d:hatDecision]->(v:DesignDecision)-[c:wirdBegründetDurch]->(z:Rationale)"
                      + " return z.id",
                  parameters("id", arcId));
          if (result != null) {
            return new ArrayList<>(result.list(record -> record.get("z.id").asString()));
          }
          return null;
        });
  }

  public String getRationaleBeschreibung(Integer arcId, String rationaleId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) match(z:Rationale) where Id(a)=$id and z.id=$rationaleId match"
                      + " (a)-[r:bausteinsicht]->(s:Image:Baustein)-[d:hatDecision]->(v:DesignDecision)-[c:wirdBegründetDurch]->(z)"
                      + " return z.beschreibung",
                  parameters("id", arcId, "rationaleId", rationaleId));
          if (result != null) {
            return result.single().get("z.beschreibung").asString();
          }
          return "";
        });
  }

  public String getRationaleReferenz(Integer arcId, String rationaleId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) match(z:Rationale) where Id(a)=$id and z.id=$rationaleId match"
                      + " (a)-[r:bausteinsicht]->(s:Image:Baustein)-[d:hatDecision]->(v:DesignDecision)-[c:wirdBegründetDurch]->(z)"
                      + " return z.referenz",
                  parameters("id", arcId, "rationaleId", rationaleId));
          if (result != null) {
            return result.single().get("z.referenz").asString();
          }
          return "";
        });
  }

  public int countQualitaetSzenarien(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match"
                      + " (a)-[r:hasQualityScenario]->(s:Qualitaetsscenario) return count(s)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(s)").asInt();
          }
          return 0;
        });
  }

  public int countRisiken(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match (a)-[r:hatRisiken]->(s:Risiko) return"
                      + " count(s)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(s)").asInt();
          }
          return 0;
        });
  }

  public String getRisiken(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match (a)-[r:hatRisiken]->(s:Risiko) return"
                      + " s.risiken",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("s.risiken").asString();
          }
          return "";
        });
  }

  public int countGlossar(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match (a)-[r:hatGlossar]->(s:Glossar) return"
                      + " count(s)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(s)").asInt();
          }
          return 0;
        });
  }

  public String getGlossar(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match (a)-[r:hatGlossar]->(s:Glossar) return"
                      + " s.glossar",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("s.glossar").asString();
          }
          return "";
        });
  }

  public void saveGitHubSecurityAdvisory(GitHubSecurityAdvisoryDTO gitHubSecurityAdvisoryDTO) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "merge(s:SecurityAdvisory {id:$id, published:$published, updated:$updated,"
                        + " title:$title, category:$category, content:$content}) return s.id,"
                        + " s.published, s.updated, s.title, s.category, s.content",
                    parameters(
                        "id",
                        gitHubSecurityAdvisoryDTO.getId(),
                        "published",
                        DateFormater.format(gitHubSecurityAdvisoryDTO.getPublished()),
                        "updated",
                        DateFormater.format(gitHubSecurityAdvisoryDTO.getUpdated()),
                        "title",
                        gitHubSecurityAdvisoryDTO.getTitle(),
                        "category",
                        gitHubSecurityAdvisoryDTO.getCategory(),
                        "content",
                        gitHubSecurityAdvisoryDTO.getContent()));
            GitHubSecurityAdvisoryDTO dto = null;
            if (result != null) {
              Record record = result.single();
              if (record != null) {
                Date published = null;
                Date updated = null;
                try {
                  String publishedString = record.get(1).asString();
                  String updatedString = record.get(2).asString();
                  published = DateFormater.parse(publishedString);
                  updated = DateFormater.parse(updatedString);

                } catch (ParseException e) {
                  e.printStackTrace();
                }

                dto =
                    new GitHubSecurityAdvisoryDTO(
                        record.get(0).asString(),
                        published,
                        updated,
                        record.get(3).asString(),
                        record.get(4).asString(),
                        record.get(5).asString());
              }
            }
            return dto;
          });
    }
  }

  public List<GitHubSecurityAdvisoryDTO> findAllGitHubSecurityAdvisory() {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(s:SecurityAdvisory) return s.id, s.published, s.updated, s.title,"
                        + " s.category, s.content");
            List<GitHubSecurityAdvisoryDTO> dtos = null;
            if (result != null) {
              dtos =
                  result.list(
                      record -> {
                        GitHubSecurityAdvisoryDTO dto = null;
                        Date published;
                        Date updated;
                        try {
                          String publishedString = record.get(1).asString();
                          String updatedString = record.get(2).asString();
                          published = DateFormater.parse(publishedString);
                          updated = DateFormater.parse(updatedString);
                          dto =
                              new GitHubSecurityAdvisoryDTO(
                                  record.get(0).asString(),
                                  published,
                                  updated,
                                  record.get(3).asString(),
                                  record.get(4).asString(),
                                  record.get(5).asString());
                        } catch (ParseException e) {
                          e.printStackTrace();
                        }
                        return dto;
                      });
            }
            return dtos;
          });
    }
  }

  public void deleteAllGitHubSecurityAdvisory() {

    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run("match(s:SecurityAdvisory) detach delete s return count(*)");
            if (result != null) {
              Record record = result.single();
              return record != null && !String.valueOf(record.get("count(*)")).isEmpty();
            }
            return null;
          });
    }
  }

  public Integer countAnalyseResult(Integer arcId) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + " match (a)-[r:analyseergebnisse]->(s:AnalyseErgebnis)"
                          + System.lineSeparator()
                          + "return count(s)",
                      parameters("id", arcId));
              Integer count = null;
              if (result != null) {
                Record record = (result.hasNext()) ? result.single() : null;
                if (record != null) {
                  count = record.get("count(s)").asInt();
                }
              }
              return count;
            });
      }
    }
    return 0;
  }

  public void deleteAnalyseResult(Integer arcId) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      if (arcId != null) {
        session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + " match (a)-[r:analyseergebnisse]->(s:AnalyseErgebnis) delete r,s "
                          + System.lineSeparator()
                          + "return count(s)",
                      parameters("id", arcId));
              Integer count = null;
              if (result != null) {
                Record record = (result.hasNext()) ? result.single() : null;
                if (record != null) {
                  count = record.get("count(s)").asInt();
                }
              }
              return count;
            });
      }
    }
  }

  public void saveAnalseResult(Integer arcId, String analyseResult) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      if (arcId != null) {
        session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + " merge (a)-[r:analyseergebnisse]->(s:AnalyseErgebnis {result:"
                          + " $result}) "
                          + System.lineSeparator()
                          + "return count(s)",
                      parameters("id", arcId, "result", analyseResult));
              Integer count = null;
              if (result != null) {
                Record record = (result.hasNext()) ? result.single() : null;
                if (record != null) {
                  count = record.get("count(s)").asInt();
                }
              }
              return count != 0;
            });
      }
    }
  }

  public String getAffectedArchitekturElement(String id) {
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      if (id != null) {
        return session.writeTransaction(
            transaction -> {
              Result aElementsResult =
                  transaction.run(
                      "match(d:DesignDecision) match(a:ArchitekturElement)<-[r:beeinflusst]-(d)"
                          + " where d.id=$id return a.name",
                      parameters("id", id));
              if (aElementsResult != null) {
                return aElementsResult
                    .list(record -> record.get("a.name").asString().trim())
                    .stream()
                    .reduce("", (partialString, element) -> partialString + ", " + element);
              }
              return "Keine";
            });
      }
      return "Keine";
    }
  }

  public List<QualityScenarioDTO> findAllQualityScenariosByArcId(String arcIdString) {
    if (arcIdString != null && !arcIdString.isEmpty()) {
      Integer arcId = Integer.parseInt(arcIdString);

      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match"
                          + " (a:Arc42)-[:hasQualityScenario]->(b:Qualitaetsscenario)-[:hasQualityCriteria]->(d:Qualitaetskriterium)"
                          + " where id(a)=$id"
                          + System.lineSeparator()
                          + "optional match"
                          + " (b:Qualitaetsscenario)-[:konkretisiert]->(c:Qualitaetsziel)-[:hasQualityCriteria]->(d:Qualitaetskriterium)"
                          + System.lineSeparator()
                          + "return"
                          + " b.qualitaetsscenario,b.stimulus,b.reaction,b.response,b.priority,b.risk,Id(b)"
                          + " as idScenario,collect(d.qualitaetskriterium) AS criteria,Id(c) as"
                          + " idGoal,c.motivation,c.qualitaetsziel",
                      parameters("id", arcId));
              return result.list(
                  r -> {
                    QualityScenarioDTO qualityScenarioDTO =
                        new QualityScenarioDTO(
                            r.get("a.qualitaetsziel").asString(),
                            r.get("a.motivation").asString(),
                            r.get("criteria").asList());
                    qualityScenarioDTO.setId(String.valueOf(r.get("idScenario")));
                    qualityScenarioDTO.setResponse(r.get("b.response").asString());
                    qualityScenarioDTO.setPriority(r.get("b.priority").asString());
                    qualityScenarioDTO.setScenarioName(r.get("b.qualitaetsscenario").asString());
                    qualityScenarioDTO.setRisk(r.get("b.risk").asString());
                    qualityScenarioDTO.setStimulus(r.get("b.stimulus").asString());
                    qualityScenarioDTO.setReaction(r.get("b.reaction").asString());
                    qualityScenarioDTO.setQualityCriteria(r.get("criteria").asList());

                    if (!r.get("idGoal").isNull()) {

                      ArrayList<String> criteria = new ArrayList<>();
                      criteria.addAll(r.get("criteria").asList(String::valueOf));
                      qualityScenarioDTO.setQualityGoalDTO(
                          new QualityGoalDTO(
                              r.get("c.qualitaetsziel").asString(),
                              r.get("c.motivation").asString(),
                              criteria));

                      qualityScenarioDTO
                          .getQualityGoalDTO()
                          .setId(String.valueOf(r.get("idGoal").asInt()));
                    }
                    return qualityScenarioDTO;
                  });
            });
      }
    }
    return new ArrayList<>();
  }

  public List<QualityDTO> findAllQualityGoalsByArcId(String arcIdString) {
    if (arcIdString != null && !arcIdString.isEmpty()) {
      Integer arcId = Integer.parseInt(arcIdString);

      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Qualitaetsziel)"
                          + System.lineSeparator()
                          + "match(c:Qualitaetskriterium)"
                          + System.lineSeparator()
                          + "match(d:Arc42) where Id(d)=$id AND (d)-[:hasQualityGoal]->(a)"
                          + System.lineSeparator()
                          + "match (a)-[r:hasQualityCriteria]->(c) "
                          + System.lineSeparator()
                          + "return a.qualitaetsziel, a.motivation,"
                          + " Id(a),collect(c.qualitaetskriterium) AS criteria",
                      parameters("id", arcId));
              return result.list(
                  r -> {
                    ArrayList<String> criteria = new ArrayList<>();
                    criteria.addAll(r.get("criteria").asList(String::valueOf));
                    QualityGoalDTO qualityGoalDTO =
                        new QualityGoalDTO(
                            r.get("a.qualitaetsziel").asString(),
                            r.get("a.motivation").asString(),
                            criteria);
                    qualityGoalDTO.setId(String.valueOf(r.get("Id(a)")));
                    return qualityGoalDTO;
                  });
            });
      }
    }
    return new ArrayList<>();
  }

  public int countNachhaltigkeitsziele(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  " match (d:Arc42) where Id(d)=$id match"
                      + " (d)-[r:hasNachhaltigkeitsziele]->(a:Nachhaltigkeitsziele) return"
                      + " count(a)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(a)").asInt();
          }
          return 0;
        });
  }

  public int countOekologischRandbedingung(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match (d:Arc42) where Id(d)=$id match"
                      + " (d)-[r:hatOekologischeRandbedingung]->(a:OekologischeRandbedingung)"
                      + " return count(a)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(a)").asInt();
          }
          return 0;
        });
  }

  public int countInterfaces(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id "
                      + "match (a)-[r:hasInterface]->(i:Interface) "
                      + "return count(i)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(i)").asInt();
          }
          return 0;
        });
  }

  public int countLoesungsStrategieToNachhaltigkeitsziele(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id "
                      + "match (a)-[r:hatLoesung]->(l:LoesungsStrategie) "
                      + "return l.strategy, l.nId",
                  parameters("id", arcId));
          int countDocumented = 0;
          List<Record> records = result.list();
          for (Record record : records) {
            if (!record.get("l.nId").isNull() && !record.get("l.strategy").isEmpty()) {
              ++countDocumented;
            }
          }
          return countDocumented;
        });
  }

  public int countMeetings(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id "
                      + "match (a)-[r:hasMeeting]->(m:Meeting) "
                      + "return count(m)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(m)").asInt();
          }
          return 0;
        });
  }

  public List<MeetingDTO> getMeetings(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(m:Meeting) "
                      + "match(a:Arc42) where Id(a)=$id "
                      + "match (a)-[r:hasMeeting]->(m) return Id(m), m.name, "
                      + "m.frequency, m.repetition, m.type",
                  parameters("id", arcId));
          return result.list(
              r -> {
                MeetingDTO meetingDTO =
                    new MeetingDTO(
                        r.get("m.name").asString(),
                        r.get("m.frequency").asInt(),
                        r.get("m.repetition").asString(),
                        r.get("m.type").asString());
                meetingDTO.setId(String.valueOf(r.get("Id(m)")));
                return meetingDTO;
              });
        });
  }

  // Verteilungssicht
  public int countVerteilungDiagramm(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id match"
                      + " (a)-[r:verteilungssicht]->(s:Image:Verteilung) return count(s)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(s)").asInt();
          }
          return 0;
        });
  }

  public int countHardware(Integer arcId) {
    Session session = this.driver.session(SessionConfig.forDatabase(Credentials.DATABASE));
    return session.writeTransaction(
        transaction -> {
          Result result =
              transaction.run(
                  "match(a:Arc42) where Id(a)=$id "
                      + "match (a)-[r:hasHardware]->(h:Hardware) "
                      + "return count(h)",
                  parameters("id", arcId));
          if (result != null) {
            Record single = result.single();
            return single.get("count(h)").asInt();
          }
          return 0;
        });
  }

  public List<HardwareDTO> getHardware(Integer arcId) {
    if (arcId != null) {
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(h:Hardware)"
                          + System.lineSeparator()
                          + "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "match (a)-[r:hasHardware]->(h) return Id(h), h.name,"
                          + " h.powerConsumption, h.isRenewable",
                      parameters("id", arcId));
              return result.list(
                  r -> {
                    HardwareDTO hardwareDTO =
                        new HardwareDTO(
                            r.get("h.name").asString(),
                            r.get("h.powerConsumption").asDouble(),
                            r.get("h.isRenewable").asBoolean());
                    hardwareDTO.setId(String.valueOf(r.get("Id(h)")));
                    return hardwareDTO;
                  });
            });
      }
    }
    return new ArrayList<>();
  }

  public List<InterfaceDTO> findAllInterfaceByArcId(Integer arcId) {
    if (arcId != null) {
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(i:Interface)"
                          + System.lineSeparator()
                          + "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "match (a)-[r:hasInterface]->(i) return Id(i), i.name,"
                          + System.lineSeparator()
                          + "i.documentation, i.calls, i.emissions",
                      parameters("id", arcId));
              return result.list(
                  r -> {
                    InterfaceDTO interfaceDTO =
                        new InterfaceDTO(
                            r.get("i.name").asString(),
                            r.get("i.documentation").toString(),
                            r.get("i.calls").asInt(),
                            r.get("i.emissions").asDouble());
                    interfaceDTO.setId(String.valueOf(r.get("Id(i)")));
                    return interfaceDTO;
                  });
            });
      }
    }
    return new ArrayList<>();
  }
}
