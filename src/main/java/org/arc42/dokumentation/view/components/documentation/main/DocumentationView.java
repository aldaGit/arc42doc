package org.arc42.dokumentation.view.components.documentation.main;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.RoutePrefix;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.view.components.documentation.DialogComponent;
import org.arc42.dokumentation.view.util.data.Links;

@CssImport("./themes/softwaredocumentation/views/my-styles.css")
@RoutePrefix(value = "arc42/:arcId?")
public class DocumentationView extends DocumentationAbstractView {

  private static boolean change = false;
  ARC42DAOAbstract dao;
  private Details einfZiele;
  private Details randbedingunen;
  private Details kontextabgrenzung;
  private Details loesungsstrategie;
  private Details bausteinsicht;
  private Details laufzeitsicht;
  private Details verteilungssicht;
  private Details konzepte;
  private Details architekturentscheidungen;
  private Details qualitaetsszenarien;
  private Details risiken;
  private Details glossar;
  private Details summaryPage;
  private Details sonar;
  private Details resultsPage;

  private Anchor titel;
  private Anchor aufgabenstellung;
  private Anchor qualitaetsziele;
  private Anchor stakeholder;
  private Anchor technisch;
  private Anchor organisatorisch;
  private Anchor konventionen;
  private Anchor fachlicherKontext;
  private Anchor technischerKontext;
  private Anchor bausteinsichtDiagramm;
  private Anchor bausteinsichtBeschreibung;
  private Anchor laufzeitsichtDiagramm;
  private Anchor checkliste;
  private Anchor bogen;
  private Anchor swot;
  private Anchor risikoMap;
  private Anchor laufzeitsichtBeschreibung;
  private Anchor verteilungssichtDiagramm;
  private Anchor verteilungssichtBeschreibung;
  private Anchor qualitaetsszenario;
  private Anchor qualitaetsbaum;

  private Anchor nachhaltigkeitsziele;
  private Anchor oekologisch;
  private Anchor loesungsstrategieAllgemein;
  private Anchor loesungsstrategieQuality;
  private Anchor loesungsstrategieOrganisatorisch;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    super.beforeEnter(event);
    if (url == null) {
      if (event.getRouteParameters().get("arcId") == null) {
        event.rerouteTo(Arc42DocumentationView.class);
      } else {
        url = event.getRouteParameters().get("arcId").get();
        this.init();
      }
    }
  }

  @Override
  public void init() {
    setSizeFull();
    setPadding(true);
    VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setSizeUndefined();

    titel = createStyledAnchor(Links.TITLE, "Titel");
    aufgabenstellung = createStyledAnchor(Links.AUFGABENSTELLUNG, "Aufgabenstellung");
    qualitaetsziele = createStyledAnchor(Links.QUALITAETSZIELE, "Qualitätsziele");
    stakeholder = createStyledAnchor(Links.STAKEHOLDER, "Stakeholder");
    einfZiele =
        createDetails(
            "1. Einführung und Ziele", titel, aufgabenstellung, qualitaetsziele, stakeholder);

    technisch = createStyledAnchor(Links.TECHNISCH, "Technisch");
    organisatorisch = createStyledAnchor(Links.ORGANISATORISCH, "Organisatorisch");
    nachhaltigkeitsziele = createStyledAnchor(Links.NACHHALTIGKEITSZIELE, "Nachhaltigkeitsziele");
    einfZiele =
        createDetails(
            "1. Einführung und Ziele",
            titel,
            aufgabenstellung,
            qualitaetsziele,
            stakeholder,
            nachhaltigkeitsziele);

    technisch = createStyledAnchor(Links.TECHNISCH, "Technisch");
    organisatorisch = createStyledAnchor(Links.ORGANISATORISCH, "Organisatorisch");
    oekologisch = createStyledAnchor(Links.OEKOLOGISCH, "Ökologisch");
    konventionen = createStyledAnchor(Links.KONVENTIONEN, "Konventionen");
    randbedingunen =
        createDetails("2. Randbedingungen", oekologisch, technisch, organisatorisch, konventionen);

    fachlicherKontext = createStyledAnchor(Links.FACHLICHERKONTEXT, "Fachlicher Kontext");
    technischerKontext = createStyledAnchor(Links.TECHNISCHERKONTEXT, "Technischer Kontext");
    kontextabgrenzung =
        createDetails("3. Kontextabgrenzung", fachlicherKontext, technischerKontext);

    loesungsstrategie = new Details();
    loesungsstrategie.setSummary(
        createStyledAnchor(Links.LOESUNGSSTRATEGIE, "4. Lösungsstrategie"));
    loesungsstrategieAllgemein = createStyledAnchor(Links.LOESUNGSSTRATEGIE, "Allgemein");
    loesungsstrategieQuality = createStyledAnchor(Links.LOESUNGTOQUALITY, "Nachhaltigkeitsziele");
    loesungsstrategieOrganisatorisch =
        createStyledAnchor(Links.LOESUNGSSTRATEGIEORGANISATORISCH, "Projektorganisation");
    loesungsstrategie = new Details();
    loesungsstrategie =
        createDetails(
            "4. Lösungsstrategie",
            loesungsstrategieAllgemein,
            loesungsstrategieQuality,
            loesungsstrategieOrganisatorisch);

    bausteinsichtDiagramm =
        createStyledAnchor(Links.BAUSTEINSICHTDIAGRAMM, "Bausteinsicht-Diagramm");
    bausteinsichtBeschreibung =
        createStyledAnchor(Links.BAUSTEINSICHTBESCHREIBUNG, "Bausteinsicht-Beschreibung");
    bausteinsicht =
        createDetails("5. Bausteinsicht", bausteinsichtDiagramm, bausteinsichtBeschreibung);

    checkliste = createStyledAnchor(Links.CHECKLISTE, "Checkliste");
    bogen = createStyledAnchor(Links.BOGEN, "Fragebogen");
    risikoMap = createStyledAnchor(Links.RISIKOMAP, "Risiko-Map");
    swot = createStyledAnchor(Links.SWOT, "SWOT-Analyse");
    risiken = createDetails("11. Risiken", checkliste, bogen, swot, risikoMap);

    laufzeitsichtDiagramm =
        createStyledAnchor(Links.LAUFZEITSICHTDIAGRAMM, "Laufzeitsicht-Diagramm");
    laufzeitsichtBeschreibung =
        createStyledAnchor(Links.LAUFZEITSICHTBESCHREIBUNG, "Laufzeitsicht-Beschreibung");
    laufzeitsicht =
        createDetails("6. Laufzeitsicht", laufzeitsichtDiagramm, laufzeitsichtBeschreibung);

    verteilungssichtDiagramm =
        createStyledAnchor(Links.VERTEILUNGSSICHTDIAGRAMM, "Verteilungssicht-Diagramm");
    verteilungssichtBeschreibung =
        createStyledAnchor(Links.VERTEILUNGSSICHTBESCHREIBUNG, "Verteilungssicht-Beschreibung");
    verteilungssicht =
        createDetails(
            "7. Verteilungssicht", verteilungssichtDiagramm, verteilungssichtBeschreibung);

    konzepte = new Details();
    konzepte.setSummary(createStyledAnchor(Links.KONZEPTE, "8. Konzepte"));
    architekturentscheidungen = new Details();
    architekturentscheidungen.setSummary(
        createStyledAnchor(Links.ARCHITEKTURENTSCHEIDUNGEN, "9. Entwurfsentscheidungen"));
    qualitaetsszenario = createStyledAnchor(Links.QUALITAETSSZENARIO, "Szenarien");
    qualitaetsbaum = createStyledAnchor(Links.QUALITAETSBAUM, "Baum");
    qualitaetsszenarien =
        createDetails("10. Qualitätsszenarien", qualitaetsszenario, qualitaetsbaum);

    // risiken = new Details();
    risiken.setSummary(createStyledAnchor(Links.RISIKEN, "11. Risiken"));
    glossar = new Details();
    glossar.setSummary(createStyledAnchor(Links.GLOSSAR, "12. Glossar"));
    summaryPage = new Details();
    summaryPage.setSummary(createStyledAnchor(Links.SUMMARY, "Zusammenfassung"));
    sonar = new Details();
    sonar.setSummary(createStyledAnchor(Links.SONAR, "13. Energy Smell Analyse"));

    verticalLayout.add(
        einfZiele,
        randbedingunen,
        kontextabgrenzung,
        loesungsstrategie,
        bausteinsicht,
        laufzeitsicht,
        verteilungssicht,
        konzepte,
        architekturentscheidungen,
        qualitaetsszenarien,
        risiken,
        glossar,
        sonar,
        summaryPage);

    verticalLayout.setPadding(false);
    verticalLayout.setSpacing(false);
    verticalLayout.setAlignItems(Alignment.STRETCH);
    verticalLayout.setWidth("300px");
    Button evaluation = new Button("Zur Bewertung");
    evaluation.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    evaluation.addClickListener(
        event -> {
          UI.getCurrent().navigate("arc42/" + url + "/bewertung/ergebnisse");
        });
    verticalLayout.add(evaluation);
    add(verticalLayout);
  }

  private Details createDetails(String summary, Anchor... anchors) {
    Details details = new Details(summary, createContent(anchors));
    details.setOpened(false);
    details.getStyle().set("padding", "0em");
    return details;
  }

  private VerticalLayout createContent(Anchor... anchors) {
    VerticalLayout content = new VerticalLayout();
    content.setAlignItems(Alignment.STRETCH);
    content.setWidthFull();
    content.setPadding(false);
    content.getStyle().set("padding-left", "21px");
    content.setSpacing(false);
    content.add(anchors);
    return content;
  }

  // GETTER

  public String getUrl() {
    return url;
  }

  public Details getEinfZiele() {
    return einfZiele;
  }

  public Details getRandbedingunen() {
    return randbedingunen;
  }

  public Details getKontextabgrenzung() {
    return kontextabgrenzung;
  }

  public Details getLoesungsstrategie() {
    return loesungsstrategie;
  }

  public Details getBausteinsicht() {
    return bausteinsicht;
  }

  public Details getLaufzeitsicht() {
    return laufzeitsicht;
  }

  public Details getVerteilungssicht() {
    return verteilungssicht;
  }

  public Details getKonzepte() {
    return konzepte;
  }

  public Details getArchitekturentscheidungen() {
    return architekturentscheidungen;
  }

  public Details getQualitaetsszenarien() {
    return qualitaetsszenarien;
  }

  public Details getRisiken() {
    return risiken;
  }

  // subpages for risiken
  public Anchor getCheckliste() {
    return checkliste;
  }

  public Anchor getBogen() {
    return bogen;
  }

  public Anchor getRisikoMap() {
    return risikoMap;
  }

  public Anchor getSwot() {
    return swot;
  }

  public Details getGlossar() {
    return glossar;
  }

  public Details getSummaryPage() {
    return summaryPage;
  }

  public Details getSonar() {
    return sonar;
  }

  public Details getResultsPage() {
    return resultsPage;
  }

  public Anchor getTitel() {
    return titel;
  }

  public Anchor getAufgabenstellung() {
    return aufgabenstellung;
  }

  public Anchor getQualitaetsziele() {
    return qualitaetsziele;
  }

  public Anchor getStakeholder() {
    return stakeholder;
  }

  public Anchor getTechnisch() {
    return technisch;
  }

  public Anchor getOrganisatorisch() {
    return organisatorisch;
  }

  public Anchor getKonventionen() {
    return konventionen;
  }

  public Anchor getFachlicherKontext() {
    return fachlicherKontext;
  }

  public Anchor getTechnischerKontext() {
    return technischerKontext;
  }

  public Anchor getBausteinsichtDiagramm() {
    return bausteinsichtDiagramm;
  }

  public Anchor getBausteinsichtBeschreibung() {
    return bausteinsichtBeschreibung;
  }

  public Anchor getLaufzeitsichtDiagramm() {
    return laufzeitsichtDiagramm;
  }

  public Anchor getLaufzeitsichtBeschreibung() {
    return laufzeitsichtBeschreibung;
  }

  public Anchor getVerteilungssichtDiagramm() {
    return verteilungssichtDiagramm;
  }

  public Anchor getVerteilungssichtBeschreibung() {
    return verteilungssichtBeschreibung;
  }

  public Anchor getQualitaetsszenario() {
    return qualitaetsszenario;
  }

  public Anchor getQualitaetsbaum() {
    return qualitaetsbaum;
  }

  @Override
  public void beforeLeave(BeforeLeaveEvent event) {
    if (change) {
      DialogComponent dialogComponent = new DialogComponent("notSave");

      // wenn eine ohne Änderung ohne zu speichern registriert wurde --> Navigation anhalten
      event.postpone();

      // Nein-Button geklickt --> View bleibt
      dialogComponent
          .getDeleteButton()
          .addClickListener(
              (ComponentEventListener<ClickEvent<Button>>)
                  event1 -> dialogComponent.getDialog().close());

      // Ja-Button geklickt --> Navigation wird fortgesetzt
      dialogComponent
          .getCreateButton()
          .addClickListener(
              (ComponentEventListener<ClickEvent<Button>>)
                  event2 -> {
                    event.getContinueNavigationAction().proceed();
                    hasChanges(false);
                    dialogComponent.getDialog().close();
                  });
    }
  }

  public static void hasChanges(boolean changed) {
    change = changed;
  }

  public Anchor getNachhaltigkeitsziele() {
    return nachhaltigkeitsziele;
  }

  public Anchor getOekologisch() {
    return oekologisch;
  }

  public Anchor getLoesungsstrategieAllgemein() {
    return loesungsstrategieAllgemein;
  }

  public Anchor getLoesungsstrategieQuality() {
    return loesungsstrategieQuality;
  }

  public Anchor getLoesungsstrategieOrganisatorisch() {
    return loesungsstrategieOrganisatorisch;
  }
}