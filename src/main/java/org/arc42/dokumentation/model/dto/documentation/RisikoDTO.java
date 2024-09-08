package org.arc42.dokumentation.model.dto.documentation;

import com.fasterxml.jackson.annotation.JsonValue;

public class RisikoDTO {
  private String id;
  private String anforderung = "";
  private Schadenshoehe schadenshoehe = Schadenshoehe.NICHTERFASST;
  private Eintrittswahrscheinlichkeit eintrittswahrscheinlichkeit =
      Eintrittswahrscheinlichkeit.NICHTERFASST;
  private Status status = Status.NICHT_ERFASST;
  private String erfasser = "";
  private String ansprechpartner = "";
  private String zuletztAktu = "";
  private String orgaEinheit = "";
  private Eintrittswahrscheinlichkeit prioSkala = Eintrittswahrscheinlichkeit.NICHTERFASST;

  private String wirkung = "";
  private String geschaedigte = "";
  private String eintrittsDatum = "";
  private String notiz = "";

  private String sanitize(String g) {
    if (g == null || g.equalsIgnoreCase("null") || g.equalsIgnoreCase("\"\"")) {
      return "";
    }
    // trim any quotes from beginning and end of g
    String cleansed = g.replaceAll("^\"|\"$", "");
    return cleansed;
  }

  public RisikoDTO() {

    this.anforderung = "";
    this.schadenshoehe = Schadenshoehe.NICHTERFASST;
    this.eintrittswahrscheinlichkeit = Eintrittswahrscheinlichkeit.NICHTERFASST;
    this.status = Status.NICHT_ERFASST;
    this.erfasser = "";
    this.ansprechpartner = "";
    this.zuletztAktu = "";
    this.orgaEinheit = "";
    this.prioSkala = Eintrittswahrscheinlichkeit.KEINE;
    this.wirkung = "";
    this.geschaedigte = "";
    this.eintrittsDatum = "";
    this.notiz = "";
  }

  public void selfSanitize() {
    this.anforderung = sanitize(this.anforderung);
    this.erfasser = sanitize(this.erfasser);
    this.ansprechpartner = sanitize(this.ansprechpartner);
    this.zuletztAktu = sanitize(this.zuletztAktu);
    this.orgaEinheit = sanitize(this.orgaEinheit);
    this.wirkung = sanitize(this.wirkung);
    this.geschaedigte = sanitize(this.geschaedigte);
    this.eintrittsDatum = sanitize(this.eintrittsDatum);
    this.notiz = sanitize(this.notiz);
  }

  public RisikoDTO(
      String anforderungInput,
      Schadenshoehe schadenshoeheInput,
      Eintrittswahrscheinlichkeit eintrittswahrscheinlichkeitInput,
      Status statusInput,
      String erfasserInput,
      String anspechpartnerInput,
      String zulsetztAktuInput,
      String orgaEinheitInput,
      Eintrittswahrscheinlichkeit prioSkalaInput,
      String wirkungInput,
      String geschaedigteInput,
      String eintrittsDatumInput,
      String notizInput) {

    this.anforderung = sanitize(anforderungInput);
    this.schadenshoehe = schadenshoeheInput;
    this.eintrittswahrscheinlichkeit = eintrittswahrscheinlichkeitInput;
    this.status = statusInput;
    this.erfasser = sanitize(erfasserInput);
    this.ansprechpartner = sanitize(anspechpartnerInput);
    this.zuletztAktu = sanitize(zulsetztAktuInput);
    this.orgaEinheit = sanitize(orgaEinheitInput);
    this.prioSkala = prioSkalaInput;
    this.wirkung = sanitize(wirkungInput);
    this.geschaedigte = sanitize(geschaedigteInput);
    this.eintrittsDatum = sanitize(eintrittsDatumInput);
    this.notiz = sanitize(notizInput);
  }

  public enum Status {
    INBEARBEITUNG("in Bearbeitung"),
    ERLEDIGT("erledigt"),
    UNBEARBEITET("unbearbeitet"),
    NICHT_ERFASST("");

    private final String label;

    Status(String s) {
      this.label = s;
    }

    @JsonValue
    public String getLabel() {
      return label;
    }

    @Override
    public String toString() {
      return this.label;
    }

    public static Status fromString(String into) {
        return switch (into) {
            case "unbearbeitet" -> UNBEARBEITET;
            case "in Bearbeitung" -> INBEARBEITUNG;
            case "erledigt" -> ERLEDIGT;
            case "nicht erfasst" -> NICHT_ERFASST;
            default -> {
                System.out.println("Status.fromString: " + into + " nicht erkannt");
                yield NICHT_ERFASST;
            }
        };
    }
  }

  public enum Eintrittswahrscheinlichkeit {
    KEINE("keine"),
    NIEDRIG("niedrig"),
    MITTEL("mittel"),
    HOCH("hoch"),
    SEHRHOCH("sehr hoch"),
    NICHTERFASST("");

    private final String label;

    Eintrittswahrscheinlichkeit(String s) {
      this.label = s;
    }

    @JsonValue
    public String getLabel() {
      return label;
    }

    @Override
    public String toString() {
      return this.label;
    }

    public static Eintrittswahrscheinlichkeit fromString(String into) {
        return switch (into) {
            case "keine" -> KEINE;
            case "niedrig" -> NIEDRIG;
            case "mittel" -> MITTEL;
            case "hoch" -> HOCH;
            case "sehr hoch" -> SEHRHOCH;
            case "" -> NICHTERFASST;
            default -> {
                System.out.println("Eintrittswahrscheinlichkeit.fromString: " + into + " nicht erkannt");
                yield NICHTERFASST;
            }
        };
    }
  }

  public enum Schadenshoehe {
    GERING("keine"),
    MITTEL("mittel"),
    HOCH("hoch"),
    SEHRHOCH("sehr hoch"),
    NICHTERFASST("");

    private final String label;

    Schadenshoehe(String s) {
      this.label = s;
    }

    @JsonValue
    public String getLabel() {
      return label;
    }

    @Override
    public String toString() {
      return this.label;
    }

    public static Schadenshoehe fromString(String into) {
        return switch (into) {
            case "gering" -> GERING;
            case "mittel" -> MITTEL;
            case "hoch" -> HOCH;
            case "sehr hoch" -> SEHRHOCH;
            default -> {
                System.out.println("Schadenshoehe.fromString: " + into + " nicht erkannt");
                yield NICHTERFASST;
            }
        };
    }
  }

  public String getId() {
    return this.id;
  }

  @Override
  public String toString() {
    return "RisikoDTO [id="
        + this.id
        + ", anforderung="
        + this.anforderung
        + ", schadenshoehe="
        + this.schadenshoehe
        + ", erfasser="
        + this.erfasser
        + ", eintrittswahrscheinlichkeit="
        + this.eintrittswahrscheinlichkeit
        + ", ansprechpartner="
        + this.ansprechpartner
        + ", zuletztAktu="
        + this.zuletztAktu
        + ", orgaEinheit="
        + this.orgaEinheit
        + ", prioSkala="
        + this.prioSkala
        + ", wirkung="
        + this.wirkung
        + ", geschaedigte="
        + this.geschaedigte
        + ", eintrittsdatum="
        + this.eintrittsDatum
        + ", status="
        + this.status
        + ", notiz="
        + this.notiz
        + "]";
  }

  public String getAnforderung() {
    return this.anforderung;
  }

  public void setAnforderung(String a) {
    this.anforderung = a;
  }

  public String getGeschaedigte() {
    return this.geschaedigte;
  }

  public void setGeschaedigte(String a) {
    this.geschaedigte = sanitize(a);
  }

  public String getErfasser() {
    return this.erfasser;
  }

  public void setErfasser(String a) {
    this.erfasser = sanitize(a);
  }

  public Schadenshoehe getSchadenshoehe() {
    return this.schadenshoehe;
  }

  public void setSchadenshoehe(Schadenshoehe g) {
    this.schadenshoehe = g;
  }

  public String getAnsprechpartner() {
    return this.ansprechpartner;
  }

  public void setAnsprechpartner(String g) {
    this.ansprechpartner = sanitize(g);
  }

  public String getZuletztAktu() {
    return this.zuletztAktu;
  }

  public void setZuletztAktu(String g) {
    this.zuletztAktu = sanitize(g);
  }

  public Eintrittswahrscheinlichkeit getPrioSkala() {
    return this.prioSkala;
  }

  public void setPrioSkala(Eintrittswahrscheinlichkeit g) {
    this.prioSkala = g;
  }

  public String getOrgaEinheit() {
    return this.orgaEinheit;
  }

  public void setOrgaEinheit(String g) {

    this.orgaEinheit = sanitize(g);
  }

  public String getWirkung() {
    return this.wirkung;
  }

  public void setWirkung(String g) {
    this.wirkung = sanitize(g);
  }

  public void setEintrittsDatum(String g) {
    this.eintrittsDatum = sanitize(g);
  }

  public String getNotiz() {
    return this.notiz;
  }

  public void setNotiz(String g) {
    this.notiz = sanitize(g);
  }

  public Eintrittswahrscheinlichkeit getEintrittswahrscheinlichkeit() {
    return eintrittswahrscheinlichkeit;
  }

  public String ewk() {
    return eintrittswahrscheinlichkeit.label;
  }

  public void setEintrittswahrscheinlichkeit(Eintrittswahrscheinlichkeit p) {
    this.eintrittswahrscheinlichkeit = p;
  }

  public Status getStatus() {
    return this.status;
  }

  public void setStatus(Status s) {
    this.status = s;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEintrittsDatum() {
    return this.eintrittsDatum;
  }
}