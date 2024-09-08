package org.arc42.dokumentation.model.dao.arc42documentation;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO.Eintrittswahrscheinlichkeit;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO.Schadenshoehe;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO.Status;
import org.arc42.dokumentation.view.util.data.Credentials;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.exceptions.NoSuchRecordException;

public class RisikoDAO extends ARC42DAOAbstract<RisikoDTO, String> {

  private static RisikoDAO instance;

  public RisikoDAO() {
    super();
  }

  public static RisikoDAO getInstance() {
    if (instance == null) {
      instance = new RisikoDAO();
    }
    return instance;
  }

  @Override
  public RisikoDTO save(RisikoDTO risikoDTO) {
    if (risikoDTO == null || isInValid(risikoDTO)) {
      throw new IllegalArgumentException("RisikoDTO is not valid");
    }
    // ab hier alle daten OK
    System.out.println("SAVING DTO TO DB: " + risikoDTO);

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      Integer arcId = getActualArcId(null);
      if (arcId != null) {
        return session.writeTransaction(
            transaction -> {
              //  delete(null); ???
              Result result =
                  transaction.run(
                      "match(a:Arc42) where Id(a)=$id"
                          + System.lineSeparator()
                          + "create (a)-[r:hasRisiko]->(b:Risiko {anforderung: $anforderung,"
                          + " eintrittswahrscheinlichkeit: $eintrittswahrscheinlichkeit,"
                          + " schadenshoehe: $schadenshoehe,"
                          + " zuletztAktu: $zuletztAktu, orgaEinheit: $orgaEinheit, wirkung:"
                          + " $wirkung, prioSkala: $prioSkala, erfasser: $erfasser,  status:"
                          + " $status, notiz: $notiz, ansprechpartner: $ansprechpartner,"
                          + " eintrittsdatum: $eintrittsdatum, geschaedigte: $geschaedigte})"
                          + " return b.anforderung, Id(b)",
                      parameters(
                          "id",
                          arcId,
                          "anforderung",
                          risikoDTO.getAnforderung(),
                          "eintrittswahrscheinlichkeit",
                          risikoDTO.getEintrittswahrscheinlichkeit().getLabel(),
                          "schadenshoehe",
                          risikoDTO.getSchadenshoehe().getLabel(),
                          "erfasser",
                          risikoDTO.getErfasser(),
                          "status",
                          risikoDTO.getStatus().getLabel(),
                          "zuletztAktu",
                          risikoDTO.getZuletztAktu(),
                          "orgaEinheit",
                          risikoDTO.getOrgaEinheit(),
                          "wirkung",
                          risikoDTO.getWirkung(),
                          "prioSkala",
                          risikoDTO.getPrioSkala().getLabel(),
                          "eintrittsdatum",
                          risikoDTO.getEintrittsDatum(),
                          "notiz",
                          risikoDTO.getNotiz(),
                          "ansprechpartner",
                          risikoDTO.getAnsprechpartner(),
                          "geschaedigte",
                          risikoDTO.getGeschaedigte()));

              RisikoDTO dto = null;

              if (result != null) {
                dto = new RisikoDTO();
              }
              return dto;
            });
      }
    }
    return null;
  }

  private boolean isInValid(RisikoDTO risikoDTO) {
    return risikoDTO.getAnforderung() == null
        || risikoDTO.getEintrittswahrscheinlichkeit() == null
        || risikoDTO.getSchadenshoehe() == null
        || risikoDTO.getStatus() == null;
  }

  @Override
  public Boolean delete(RisikoDTO risikoDTO) {
    if (risikoDTO != null) {
      try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
        session.writeTransaction(
            transaction -> {
              transaction.run(
                  "match(a:Risiko)where Id(a)=$id" + System.lineSeparator() + "detach delete(a)",
                  parameters("id", Integer.parseInt(risikoDTO.getId())));
              // org.neo4j.driver.exceptions.ClientException: Cannot delete node<234>, because it
              // still has relationships. To delete this node, you must first delete its
              // relationships.
              return null;
            });
      }
    }

    return true;
  }

  public List<RisikoDTO> findAllByArcId(String arcIdFromUrl) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      return session.writeTransaction(
          transaction -> {
            Result result =
                transaction.run(
                    "match(d:Arc42) where Id(d)=$id match(a:Risiko) match (d)-[r:hasRisiko]->(a)"
                        + " return a.anforderung, a.schadenshoehe, a.eintrittswahrscheinlichkeit,"
                        + " a.status, a.erfasser, a.ansprechpartner,"
                        + " a.zuletztAktu, a.orgaEinheit, a.prioSkala, a.wirkung, a.geschaedigte,"
                        + " a.eintrittsdatum, a.notiz, Id(a)",
                    // + " a.risiken, a.titel, a.status, a.erfasser, Id(a)",
                    parameters("id", Integer.parseInt(arcIdFromUrl)));

            return result.list(
                r -> {
                  String valid = r.get("a.eintrittswahrscheinlichkeit", "");
                  if (valid == null || valid.equals("")) {
                    valid = "";
                  }
                  String validStaus = r.get("a.status", "");
                  if (validStaus == null || validStaus.equals("")) {
                    validStaus = "";
                  }
                  String validSchadenshoehe = r.get("a.schadenshoehe", "");
                  if (validStaus == null || validStaus.equals("")) {
                    validStaus = "";
                  }
                  RisikoDTO dto = new RisikoDTO();
                  dto.setAnforderung(r.get("a.anforderung").asString());
                  dto.setSchadenshoehe(Schadenshoehe.fromString(validSchadenshoehe));
                  dto.setEintrittswahrscheinlichkeit(Eintrittswahrscheinlichkeit.fromString(valid));
                  dto.setErfasser(r.get("a.erfasser").toString());
                  dto.setStatus(Status.fromString(validStaus));
                  dto.setAnsprechpartner(r.get("a.ansprechpartner").toString());
                  dto.setZuletztAktu(r.get("a.zuletztAktu").toString());
                  dto.setOrgaEinheit(r.get("a.orgaEinheit").toString());
                  dto.setWirkung(r.get("a.wirkung").toString());
                  dto.setGeschaedigte(r.get("a.geschaedigte").toString());
                  dto.setNotiz(r.get("a.notiz").asString());
                  dto.setId(String.valueOf(r.get("Id(a)")));

                  dto.setEintrittsDatum(r.get("a.eintrittsdatum").asString());

                  valid = r.get("a.prioSkala", "hoch");
                  if (valid == null || valid.equals("")) {
                    valid = "";
                  }
                  dto.setPrioSkala(Eintrittswahrscheinlichkeit.fromString(valid));
                  return dto;
                });
          });
    }
  }

  @Override
  public RisikoDTO findById(String id) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {

      if (id != null && !id.equals("")) {
        return session.writeTransaction(
            transaction -> {
              Result result =
                  transaction.run(
                      "match(a:Risiko) where Id(a)=$id return a.anforderung, a.schadenshoehe,"
                          + " a.eintrittswahrscheinlichkeit, a.status, a.geschaedigte ,"
                          + " a.notiz, a.wirkung, a.eintrittsdatum, a.erfasser, a.ansprechpartner,"
                          + " a.zuletztAktu, a.orgaEinheit, a.prioSkala,Id(a)",
                      parameters("id", Integer.parseInt(id)));
              if (result == null || !result.hasNext()) {
                System.out.println("No Risiko found");
                return null;
              }
              Record r = result.next();

              String valid = r.get("a.eintrittswahrscheinlichkeit", "");
              if (valid == null || valid.equals("")) {
                valid = "";
              }
              String validStaus = r.get("a.status", "");
              if (validStaus == null || validStaus.equals("")) {
                validStaus = "";
              }
              String validSchadenshoehe = r.get("a.schadenshoehe", "");
              if (validStaus == null || validStaus.equals("")) {
                validStaus = "";
              }
              RisikoDTO dto = new RisikoDTO();
              dto.setEintrittswahrscheinlichkeit(Eintrittswahrscheinlichkeit.fromString(valid));
              dto.setAnforderung(r.get("a.anforderung").asString());
              dto.setSchadenshoehe(Schadenshoehe.fromString(validSchadenshoehe));
              dto.setErfasser(r.get("a.erfasser").toString());
              dto.setStatus(Status.fromString(validStaus));
              dto.setAnsprechpartner(r.get("a.ansprechpartner").toString());
              dto.setZuletztAktu(r.get("a.zuletztAktu").toString());
              dto.setOrgaEinheit(r.get("a.orgaEinheit").toString());
              dto.setWirkung(r.get("a.wirkung").toString());
              dto.setGeschaedigte(r.get("a.geschaedigte").toString());
              dto.setEintrittsDatum(r.get("a.eintrittsdatum").asString());
              dto.setNotiz(r.get("a.notiz").asString());
              dto.setId(String.valueOf(r.get("Id(a)")));

              valid = r.get("a.prioSkala", "hoch");
              if (valid == null || valid.equals("")) {
                valid = "";
              }
              dto.setPrioSkala(Eintrittswahrscheinlichkeit.fromString(valid));
              return dto;
            });
      }
    }
    return null;
  }

  @Override
  public void update(RisikoDTO t) {

    Credentials.readEnvironment();
    try (Session session = getDriver().session(SessionConfig.forDatabase(Credentials.DATABASE))) {
      session.writeTransaction(
          transaction -> {
            System.out.println("attempting to update: " + t.getId());
            System.out.println("id: " + t.getId());
            System.out.println("beschreibung: " + t.getAnforderung());
            System.out.println("erfasser: " + t.getErfasser());
            System.out.println("status: " + t.getStatus());
            if (t.getPrioSkala() == null) {
              // TODO?
              t.setPrioSkala(Eintrittswahrscheinlichkeit.fromString(""));
            } else {
              System.out.println("prioskala: " + t.getPrioSkala().getLabel());
            }
            Result result =
                transaction.run(
                    "MATCH (a:Risiko) where Id(a)=$id   SET a = {anforderung: $anforderung,"
                        + " eintrittswahrscheinlichkeit: $eintrittswahrscheinlichkeit,"
                        + " schadenshoehe: $schadenshoehe,  erfasser: $erfasser,"
                        + " status: $status, ansprechpartner: $ansprechpartner,"
                        + " zuletztAktu: $zuletztAktu, orgaEinheit: $orgaEinheit, prioSkala:"
                        + " $prioSkala, wirkung: $wirkung, geschaedigte: $geschaedigte,"
                        + " eintrittsdatum: $eintrittsdatum, notiz: $notiz} return"
                        + " Id(a), a.anforderung, a.schadenshoehe, a.eintrittswahrscheinlichkeit,"
                        + " a.status",
                    parameters(
                        "id",
                        Integer.parseInt(t.getId()),
                        "anforderung",
                        t.getAnforderung(),
                        "schadenshoehe",
                        t.getSchadenshoehe().getLabel(),
                        "eintrittswahrscheinlichkeit",
                        t.getEintrittswahrscheinlichkeit().getLabel(),
                        "status",
                        t.getStatus().getLabel(),
                        "erfasser",
                        t.getErfasser(),
                        "ansprechpartner",
                        t.getAnsprechpartner(),
                        "zuletztAktu",
                        t.getZuletztAktu(),
                        "orgaEinheit",
                        t.getOrgaEinheit(),
                        "prioSkala",
                        t.getPrioSkala().getLabel(),
                        "wirkung",
                        t.getWirkung(),
                        "geschaedigte",
                        t.getGeschaedigte(),
                        "eintrittsdatum",
                        t.getEintrittsDatum(),
                        "notiz",
                        t.getNotiz()));

            RisikoDTO dto = null;
            if (result != null) {
              try {
                Record r = result.single();

                dto = new RisikoDTO();
                dto.setAnforderung(r.get("a.anforderung").asString());
                dto.setEintrittswahrscheinlichkeit(
                    Eintrittswahrscheinlichkeit.fromString(
                        r.get("a.eintrittswahrscheinlichkeit").asString()));
                dto.setSchadenshoehe(Schadenshoehe.fromString(r.get("a.schadenshoehe").asString()));
                dto.setStatus(Status.fromString(r.get("a.status").asString()));
                dto.setErfasser(r.get("a.erfasser").asString());
                dto.setAnsprechpartner(r.get("a.ansprechpartner").asString());
                dto.setZuletztAktu(r.get("a.zuletztAktu").asString());
                dto.setOrgaEinheit(r.get("a.orgaEinheit").asString());
                dto.setPrioSkala(
                    Eintrittswahrscheinlichkeit.fromString(
                        r.get("a.eintrittswahrscheinlichkeit").asString()));
                dto.setWirkung(r.get("a.wirkung").asString());
                dto.setGeschaedigte(r.get("a.geschaedigte").asString());
                dto.setEintrittsDatum(r.get("a.eintrittsdatum").asString());
                dto.setNotiz(r.get("a.notiz").asString());

                dto.setId(String.valueOf(r.get("Id(a)")));
              } catch (NoSuchRecordException e) {
                System.out.println("Error in update: " + e.getMessage());
              }
            }
            return dto;
          });
    }
  }

  @Override
  public List<RisikoDTO> findAll(String url) {
    try {
      Integer id = Integer.parseInt(url);
      return this.findAllByArcId("" + id);

    } catch (NumberFormatException e) {
      return this.findAllByArcId("" + getActualArcId(url));
    }
  }

  public List<String> getAllIds(String url) {
    List<RisikoDTO> all = this.findAll(url);
    List<String> idList = new ArrayList<>();
    for (RisikoDTO risikoDTO : all) {
      idList.add(risikoDTO.getId());
    }
    return idList;
  }

  public List<String> getAllBeschreibungen(String url) {
    List<RisikoDTO> all = this.findAll(url);
    List<String> bList = new ArrayList<>();
    for (RisikoDTO risikoDTO : all) {
      bList.add(risikoDTO.getAnforderung());
    }
    return bList;
  }

  public String getBeschreibungToID(String id) {
    RisikoDTO existing = this.findById(id);
    if (existing == null) {
      System.out.println("Risiko mit der ID " + id + " nicht gefunden!");
      return "";
    }
    return existing.getAnforderung();
  }
}