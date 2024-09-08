package org.arc42.analyse.control.service;

public class VollstaendigkeitAnalyseArc42Architektur {

  private final Integer arcId;
  private final Arc42AnalyseService service;
  private String notDokumentedString;
  private Boolean notDokumented;
  private String partialDokumentedString;
  private Boolean partialDokumented;
  private final Integer anzahlDocumented;

  public VollstaendigkeitAnalyseArc42Architektur(Integer arcId) {
    this.notDokumented = false;
    this.partialDokumented = false;
    this.notDokumentedString =
        "<ul style=\"color: #ff0000; font-size: small;\">\n" + "###############\n" + "</ul>\n";
    this.partialDokumentedString =
        "<ul  style=\"color: #0000ff;\">\n" + "---------------\n" + "</ul>\n";

    this.arcId = arcId;
    this.service = Arc42AnalyseService.getInstance();
    this.anzahlDocumented =
        this.service.countDocumentedElemente(this.arcId) - this.service.countAnalyseResult(arcId);
  }

  public void startVollstaendigkeitAnalyseForArcId() {
    String ersatz = "----------";
    String list = "<li><em>----------</em></li>\n";

    if (!(this.arcId == null)) {
      String result = "";
      String result1 = "";

      // Einführung & Ziele
      int countStakeholder = this.service.countStakeholder(this.arcId);
      if (countStakeholder == 0) {
        this.notDokumented = true;
        result = result + list.replaceFirst(ersatz, "Einführung und Ziele --> Stakeholder");
      }

      int countAufgabenstellung = this.service.countAufgabenstellung(this.arcId);
      if (countAufgabenstellung == 0) {
        this.notDokumented = true;
        result = result + list.replaceFirst(ersatz, "Einführung und Ziele --> Aufgabenstellung");
      }

      int countQualitaetsZiele = this.service.countQualitaetsZiele(this.arcId);
      if (countQualitaetsZiele == 0) {
        this.notDokumented = true;
        result = result + list.replaceFirst(ersatz, "Einführung und Ziele --> Qualitätsziele");
      }

      int countNachhaltigkeitsziele = this.service.countNachhaltigkeitsziele(this.arcId);
      if (countNachhaltigkeitsziele == 0) {
        this.notDokumented = true;
        result =
            result + list.replaceFirst(ersatz, "Einführung und Ziele --> Nachhaltigkeitsziele");
      }

      // Randbedingungen
      int countTechnischRandbedingung = this.service.countTechnischRandbedingung(arcId);
      if (countTechnischRandbedingung == 0) {
        this.notDokumented = true;
        result = result + list.replaceFirst(ersatz, "Randbedingungen --> Technisch");
      }

      int countOrganisatorischRandbedingung = this.service.countOrganisatorischRandbedingung(arcId);
      if (countOrganisatorischRandbedingung == 0) {
        this.notDokumented = true;
        result = result + list.replaceFirst(ersatz, "Randbedingungen --> Organisatorisch");
      }

      int countOekologischRandbedingung = this.service.countOekologischRandbedingung(arcId);
      if (countOekologischRandbedingung == 0) {
        this.notDokumented = true;
        result = result + list.replaceFirst(ersatz, "Randbedingungen --> Ökologisch");
      }

      int countKonventionRandbedingung = this.service.countKonventionRandbedingung(arcId);
      if (countKonventionRandbedingung == 0) {
        this.notDokumented = true;
        result = result + list.replaceFirst(ersatz, "Randbedingungen --> Konvention");
      }

      // Kontextabgrenzung
      int countFachlicherKontext = this.service.countFachlicherKontext(arcId);
      if (countFachlicherKontext == 0) {
        this.notDokumented = true;
        result = result + list.replaceFirst(ersatz, "Kontextabgrenzung --> Fachlicher Kontext");

        int countInterfaces = this.service.countInterfaces(arcId);
        if (countInterfaces == 0) {
          this.notDokumented = true;
          result = result + list.replaceFirst(ersatz, "Kontextabgrenzung --> Technischer Kontext");
        } else if (countInterfaces < 3) {
          this.partialDokumented = true;
          result1 =
              result1 + list.replaceFirst(ersatz, "Kontextabgrenzung --> Technischer Kontext");
        }

        // Lösungsstrategie
        String loesungsStrategie = this.service.getAllgemeineLoesungsStrategie(arcId);
        if (loesungsStrategie == null) {
          this.notDokumented = true;
          result =
              result
                  + list.replaceFirst(ersatz, "Lösungsstrategie --> Allgemeine Lösungsstrategie");
        } else {
          if (loesungsStrategie.trim().length() < 150) {
            this.partialDokumented = true;
            result1 =
                result1
                    + list.replaceFirst(ersatz, "Lösungsstrategie --> Allgemeine Lösungsstrategie");
          }
        }

        int countLoesungsStrategieToNachhaltigkeitsziele =
            this.service.countLoesungsStrategieToNachhaltigkeitsziele(arcId);
        if (countLoesungsStrategieToNachhaltigkeitsziele == 0) {
          this.notDokumented = true;
          result = result + list.replaceFirst(ersatz, "Lösungsstrategie --> Nachhaltigkeitsziele");
        } else if (countLoesungsStrategieToNachhaltigkeitsziele < 3) {
          this.partialDokumented = true;
          result1 =
              result1 + list.replaceFirst(ersatz, "Lösungsstrategie --> Nachhaltigkeitsziele");
        }

        int countMeetings = this.service.countMeetings(arcId);
        if (countMeetings == 0) {
          this.notDokumented = true;
          result = result + list.replaceFirst(ersatz, "Lösungsstrategie --> Projektorganisation");
        } else if (countMeetings < 3) {
          this.partialDokumented = true;
          result1 = result1 + list.replaceFirst(ersatz, "Lösungsstrategie --> Projektorganisation");
        }

        // Bausteinsicht
        int countBausteinDiagram = this.service.countBausteinDiagram(arcId);
        if (countBausteinDiagram == 0) {
          this.notDokumented = true;
          result = result + list.replaceFirst(ersatz, "Bausteinsicht --> Bausteinsicht-Diagramm");
        } else {
          String bausteinBeschreibung = this.service.getBausteinBeschreibung(arcId);
          if (bausteinBeschreibung.trim().length() == 0) {
            this.notDokumented = true;
            result =
                result + list.replaceFirst(ersatz, "Bausteinsicht --> Bausteinsicht-Beschreibung");
          } else if (bausteinBeschreibung.trim().length() < 150) {
            this.partialDokumented = true;
            result1 =
                result1 + list.replaceFirst(ersatz, "Bausteinsicht --> Bausteinsicht-Beschreibung");
          }
        }

        // Laufzeitsicht
        int countLaufzeitDiagram = this.service.countLaufzeitDiagram(arcId);
        if (countLaufzeitDiagram == 0) {
          this.notDokumented = true;
          result = result + list.replaceFirst(ersatz, "Laufzeitsicht --> Laufzeitsicht-Diagramm");
        } else {
          String laufzeitDiagram = this.service.getLaufzeitBeschreibung(arcId);
          if (laufzeitDiagram.trim().length() == 0) {
            this.notDokumented = true;
            result =
                result + list.replaceFirst(ersatz, "Laufzeitsicht --> Laufzeitsicht-Beschreibung");
          } else if (laufzeitDiagram.trim().length() < 150) {
            this.partialDokumented = true;
            result1 =
                result1 + list.replaceFirst(ersatz, "Laufzeitsicht --> Laufzeitsicht-Beschreibung");
          }
        }

        // Verteilungssicht
        int countVerteilungDiagram = this.service.countVerteilungDiagramm(arcId);
        if (countVerteilungDiagram == 0) {
          this.notDokumented = true;
          result =
              result + list.replaceFirst(ersatz, "Verteilungssicht --> Verteilungssicht-Diagramm");
        } else {
          String verteilungBeschreibung = this.service.getVerteilungBeschreibung(arcId);
          if (verteilungBeschreibung.trim().length() == 0) {
            this.notDokumented = true;
            result =
                result
                    + list.replaceFirst(
                        ersatz, "Verteilungssicht --> Verteilungssicht-Beschreibung");
          } else if (verteilungBeschreibung.trim().length() < 150) {
            this.partialDokumented = true;
            result1 =
                result1
                    + list.replaceFirst(
                        ersatz, "Verteilungssicht --> Verteilungssicht-Beschreibung");
          }

          int countHardware = this.service.countHardware(arcId);
          if (countHardware == 0) {
            this.notDokumented = true;
            result =
                result
                    + list.replaceFirst(
                        ersatz, "Verteilungssicht --> Verteilungssicht-Diagramm (Hardware)");
          } else if (countHardware < 3) {
            this.partialDokumented = true;
            result1 =
                result1
                    + list.replaceFirst(
                        ersatz, "Verteilungssicht --> Verteilungssicht-Diagramm (Hardware)");
          }
        }
      }
      int countKonzepte = this.service.countKonzepte(arcId);
      if (countKonzepte == 0) {
        this.notDokumented = true;
        result = result + list.replaceFirst(ersatz, "Konzepte");
      } else {
        String konzepte = this.service.getKonzepte(arcId);
        if (konzepte.trim().length() < 150) {
          this.partialDokumented = true;
          result1 = result1 + list.replaceFirst(ersatz, "Konzepte");
        }
      }

      int countArchitekturEntscheidung = this.service.countArchitekturEntscheidung(arcId);
      int countDesignDecision = this.service.countDesignDecision(arcId);
      if (countArchitekturEntscheidung == 0 && countDesignDecision == 0) {
        this.notDokumented = true;
        result = result + list.replaceFirst(ersatz, "Architekturentscheidungen");

      } else if (countArchitekturEntscheidung != 0) {
        String architekturEntscheidung = this.service.getArchitekturEntscheidung(arcId);
        if (architekturEntscheidung.trim().length() < 150) {
          this.partialDokumented = true;
          result1 = result1 + list.replaceFirst(ersatz, "Architekturentscheidungen");
        }
      }

      int countQualitaetSzenarien = this.service.countQualitaetSzenarien(arcId);
      if (countQualitaetSzenarien == 0) {
        this.notDokumented = true;
        result = result + list.replaceFirst(ersatz, "Qualitätsszenarien");
      }

      int countRisiken = this.service.countRisiken(arcId);
      if (countRisiken == 0) {
        this.notDokumented = true;
        result = result + list.replaceFirst(ersatz, "Risiken");
      } else {
        String risiken = this.service.getRisiken(arcId);
        if (risiken.trim().length() < 150) {
          this.partialDokumented = true;
          result1 = result1 + list.replaceFirst(ersatz, "Risiken");
        }
      }

      int countGlossar = this.service.countGlossar(arcId);
      if (countGlossar == 0) {
        this.notDokumented = true;
        result = result + list.replaceFirst(ersatz, "Glossar");
      } else {
        String glossar = this.service.getGlossar(arcId);
        if (glossar.trim().length() < 150) {
          this.partialDokumented = true;
          result1 = result1 + list.replaceFirst(ersatz, "Glossar");
        }
      }
      this.notDokumentedString = this.notDokumentedString.replaceFirst("###############", result);
      this.partialDokumentedString =
          this.partialDokumentedString.replaceFirst("---------------", result1);
    }
  }

  public String getPartialDokumentedString() {

    return partialDokumentedString;
  }

  public String getNotDokumentedString() {
    return notDokumentedString;
  }

  public Boolean getNotDokumented() {
    return notDokumented;
  }

  public Integer getAnzahlDocumented() {
    return anzahlDocumented;
  }

  public Boolean getPartialDokumented() {
    return partialDokumented;
  }
}
