package org.arc42.dokumentation.model.dao.arc42documentation;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import org.arc42.analyse.control.SonarService;
import org.arc42.analyse.model.dto.sonar.*;
import org.arc42.analyse.model.util.RawHtml;
import org.arc42.analyse.model.util.StringFormatter;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Route(value = "sonar", layout = MainLayout.class)
public class ARC42SonarAnalyse extends DocumentationView {
  SonarService sonarService = SonarService.getInstance();

  SonarSettingsDAO sonarSettingsDAO = SonarSettingsDAO.getInstance();

  private Map<Tab, Component> tabComponentMap = new LinkedHashMap<>();

  private static final String GENERIC_ERROR_MSG = "Da ist etwas schiefgegangen.";

  public ARC42SonarAnalyse() throws MalformedURLException {}

  @Override
  public void init() {
    super.init();
    try {
      if (sonarSettingsDAO.getSettings() != null && sonarService.getRules() != null)
        RuleList.getInstance().setRules(sonarService.getRules().getRules());
      else createDialogWindow().open();
    } catch (IOException e) {
      Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
          .severe("Rules konnten nicht geladen werden. \n" + e.getMessage());
    }
    setWidth("100%");

    TabSheet tabSheet = new TabSheet();
    tabSheet.setWidth("100%");
    add(tabSheet);

    Button setupButton = new Button("SonarQube Setup", VaadinIcon.COG.create());
    setupButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    setupButton.setIconAfterText(true);
    setupButton.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            e -> {
              Dialog dialog = createDialogWindow();
              dialog.open();
            });
    FlexLayout wrapperSetUp = new FlexLayout(setupButton);
    HorizontalLayout topRow =
        new HorizontalLayout(
            new BreadCrumbComponent(new String[] {"Energy Smell Analyse"}), wrapperSetUp);
    topRow.setWidth("100%");
    topRow.expand(wrapperSetUp);
    wrapperSetUp.setJustifyContentMode(JustifyContentMode.END);
    topRow.setAlignItems(Alignment.CENTER);

    VerticalLayout topContent = new VerticalLayout(topRow, tabSheet);
    add(topContent);

    tabSheet.add("Analyse", createAnalyseTab());
    tabSheet.add("Regeln", createRuleTab());
  }

  public Grid<Issue> createIssueGrid() {
    Grid<Issue> grid = new Grid<>(Issue.class, false);
    grid.addColumn(Issue::getRuleshort).setHeader("Energy Smell").setSortable(true);
    grid.addColumn(Issue::getTag).setHeader("Typ").setSortable(true);
    grid.addColumn(Issue::getSeverity).setHeader("Severity").setSortable(true);
    grid.addColumn(Issue::getStatus).setHeader("Status").setSortable(true);
    grid.setSelectionMode(Grid.SelectionMode.MULTI);
    grid.setItemDetailsRenderer(createSmellDetailRenderer());
    return grid;
  }

  public VerticalLayout createAnalyseTab() {
    VerticalLayout analyseTab = new VerticalLayout();
    analyseTab.setSizeFull();

    Span text = new Span("Lassen Sie ihren Code auf Energieeffizienz analysieren. ");

    Grid<Issue> grid = createIssueGrid();

    Button analyzeButton = new Button("Analyse");
    analyzeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    analyzeButton.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              try {
                IssueList issues = sonarService.getIssues();
                if (issues == null) {
                  createClosableNotification(
                          NotificationVariant.LUMO_ERROR,
                          3000,
                          "SonarQube ist nicht korrekt eingebunden.")
                      .open();
                } else if (issues.getIssues().isEmpty()) {
                  createClosableNotification(
                          NotificationVariant.LUMO_WARNING, 3000, "Keine Code Smells gefunden.")
                      .open();
                  grid.setItems();
                } else {
                  for (Issue i : issues.getIssues()) {
                    i.setRuleshort(StringFormatter.formatRuleName(i.getRule()));
                    i.formatTag();
                    i.setComponent(
                        i.getComponent().substring(i.getComponent().lastIndexOf(":") + 1));
                  }
                  grid.setItems(issues.getIssues());
                }
              } catch (IOException e) {
                createClosableNotification(
                        NotificationVariant.LUMO_ERROR,
                        3000,
                        GENERIC_ERROR_MSG + "\n Ist die Verbindung zu SonarQube eingerichtet?")
                    .open();
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
                    .severe("Issues konnten nicht geladen werden \n" + e.getMessage());
              }
            });

    Button ignoreButton = new Button("Ignorieren");
    ignoreButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    ignoreButton.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            e -> {
              IssueList issueList = new IssueList(grid.getSelectedItems().stream().toList());
              if (!issueList.getIssues().isEmpty()) {
                try {
                  int ignoreResult =
                      sonarService.bulkChangeIssues(
                          "wontfix",
                          issueList.getKeys().toString().replace("[", "").replace("]", ""));
                  if (ignoreResult != 200) {
                    createClosableNotification(
                            NotificationVariant.LUMO_ERROR,
                            6000,
                            "Smells konnten nicht erfolgreich ignoriert werden.")
                        .open();
                  } else analyzeButton.click();
                } catch (IOException ex) {
                  createClosableNotification(
                          NotificationVariant.LUMO_ERROR, 3000, GENERIC_ERROR_MSG)
                      .open();
                  Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
                      .severe(
                          "Issues konnten nicht als ignoriert markiert werden. \n"
                              + ex.getMessage());
                }
              }
            });

    Button fixedButton = new Button("Als gelöst markieren");
    fixedButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    fixedButton.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            e -> {
              IssueList issueList = new IssueList(grid.getSelectedItems().stream().toList());
              if (!issueList.getIssues().isEmpty()) {
                try {
                  int ignoreResult =
                      sonarService.bulkChangeIssues(
                          "resolve",
                          issueList.getKeys().toString().replace("[", "").replace("]", ""));
                  if (ignoreResult != 200) {
                    createClosableNotification(
                            NotificationVariant.LUMO_ERROR,
                            6000,
                            "Smells konnten nicht erfolgreich ignoriert werden.")
                        .open();
                  } else analyzeButton.click();
                } catch (IOException ex) {
                  createClosableNotification(
                          NotificationVariant.LUMO_ERROR, 3000, GENERIC_ERROR_MSG)
                      .open();
                  Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
                      .severe(
                          "Issues konnten nicht als fixed markiert werden. \n" + ex.getMessage());
                }
              }
            });

    HorizontalLayout footerButtons = new HorizontalLayout(ignoreButton, fixedButton);
    analyseTab.add(text, analyzeButton, grid, footerButtons);
    analyseTab.setWidth("100%");
    grid.setWidth("100%");
    return analyseTab;
  }

  public VerticalLayout createRuleTab() {
    VerticalLayout content = new VerticalLayout();
    content.getStyle().set("padding-top", "0px");
    if ((RuleList.getInstance().getRules() != null)) {
      for (Rule rule : RuleList.getInstance().getRules()) {
        RulesRoot temp;
        try {
          temp = sonarService.getRuleDetails(rule.getKey());
          rule.completeRuleDetails(temp.getRule());
          rule.setActives(temp.getActives());
          createRuleTabContent(rule);
        } catch (IOException e) {
          createClosableNotification(
                  NotificationVariant.LUMO_ERROR, 6000, "Fehler beim Laden der Regeln")
              .open();
          Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
              .severe("Rules konnten nicht geladen werden. \n" + e.getMessage());
        }
      }
    }
    // based on https://stackoverflow.com/a/57675685
    Tabs tabs = new Tabs(tabComponentMap.keySet().toArray(new Tab[] {}));
    tabs.setOrientation(Tabs.Orientation.VERTICAL);

    tabs.addSelectedChangeListener(
        e -> {
          content.removeAll();
          content.add(tabComponentMap.get(e.getSelectedTab()));
        });
    if (!tabComponentMap.isEmpty()) {
      content.add(tabComponentMap.get(tabs.getSelectedTab()));
    } else {
      content.add(
          new Span(
              "Es konnten keine Regeln abgerufen werden. Ist die Verbindung zu SonarQube"
                  + " eingerichtet?"));
    }

    Span explanation =
        new Span(
            "Hier können die aktivierten Energy Smell Regeln eingesehen werden. "
                + "Regeln mit Parametern können angepasst werden.");

    HorizontalLayout horizontalLayout = new HorizontalLayout(tabs, content);
    horizontalLayout.setWidth("100%");
    VerticalLayout verticalLayout = new VerticalLayout(explanation, horizontalLayout);
    verticalLayout.getStyle().set("padding", "0px");
    return verticalLayout;
  }

  public void createRuleTabContent(Rule rule) {
    VerticalLayout tab = new VerticalLayout();
    tab.getStyle().set("padding-top", "0px");

    Document htmlDesc = Jsoup.parse(rule.getHtmlDesc());
    HorizontalLayout htmlExamples = createCodeExamples(htmlDesc);
    RawHtml message = new RawHtml();
    message.setHtml(Objects.requireNonNull(htmlDesc.select("p").first()).toString());

    final VerticalLayout detail =
        new VerticalLayout(new H3(StringFormatter.formatRuleName(rule.getKey())), message);
    detail.getStyle().set("flex-grow", "1");
    tab.add(detail);

    if (!rule.getParams().isEmpty() && !rule.getActives().isEmpty()) {
      Span para = new Span();
      String html =
          "<b>Parameter</b></br>"
              + rule.getParams().get(0).getParamDescr()
              + "</br>Standardwert: "
              + rule.getParams().get(0).getDefaultValue();
      para.getElement().setProperty("innerHTML", html);
      IntegerField value = new IntegerField("Aktueller Wert");
      value.setMin(0);
      int currentValue = Integer.parseInt(rule.getActives().get(0).getParams().get(0).getValue());
      value.setValue(currentValue);

      Button changeParam = new Button("Wert ändern");
      changeParam.addClickListener(
          e -> {
            try {
              int code =
                  SonarService.getInstance()
                      .changeParameterValue(
                          rule.getKey(), rule.getParams().get(0).getKey(), value.getValue());
              if (code != 204 && code != 200) {
                createClosableNotification(
                        NotificationVariant.LUMO_ERROR,
                        6000,
                        "Parameterwert konnte nicht erfolgreich geändert werden.")
                    .open();
                value.setValue(currentValue);
              } else {
                createClosableNotification(
                        NotificationVariant.LUMO_SUCCESS,
                        6000,
                        "Parameterwert erfolgreich geändert.")
                    .open();
              }
            } catch (IOException ex) {
              Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
                  .severe("Parameterwert konnte nicht geändert werden. \n" + ex.getMessage());
            }
          });

      HorizontalLayout changeValueButton = new HorizontalLayout(value, changeParam);
      changeValueButton.setVerticalComponentAlignment(Alignment.END, changeParam);
      VerticalLayout parameterLayout = new VerticalLayout(para, changeValueButton);
      parameterLayout.setSpacing(false);
      parameterLayout.getStyle().set("padding", "0px");
      detail.add(parameterLayout);
    }

    detail.add(htmlExamples);

    tabComponentMap.put(new Tab(StringFormatter.formatRuleName(rule.getKey())), tab);
  }

  public static HorizontalLayout createCodeExamples(Document htmlDesc) {
    final RawHtml noncompliant = new RawHtml();
    final RawHtml compliant = new RawHtml();
    final HorizontalLayout htmlExamples = new HorizontalLayout(noncompliant, compliant);
    Elements pre = htmlDesc.select("pre");
    Elements b = htmlDesc.select("b");

    htmlExamples.setWidth("100%");
    htmlExamples.expand(noncompliant, compliant);

    Objects.requireNonNull(pre.first()).attr("style", "padding:24px");
    Objects.requireNonNull(pre.last()).attr("style", "padding:24px");
    noncompliant.setHtml(
        Objects.requireNonNull(b.first()) + Objects.requireNonNull(pre.first()).toString());
    compliant.setHtml(
        Objects.requireNonNull(b.last()) + Objects.requireNonNull(pre.last()).toString());
    return htmlExamples;
  }

  private Notification createClosableNotification(
      NotificationVariant notificationVariant, int i, String s) {
    Notification notif = new Notification();
    notif.addThemeVariants(notificationVariant);
    notif.setPosition(Notification.Position.MIDDLE);
    notif.setDuration(i);
    Div text = new Div(new Text(s));

    Button closeButton = new Button(new Icon("lumo", "cross"));
    closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
    closeButton.setAriaLabel("Close");
    closeButton.addClickListener(cl -> notif.close());
    notif.add(new HorizontalLayout(text, closeButton));
    return notif;
  }

  public Dialog createDialogWindow() {
    Dialog dialog;
    dialog = new Dialog();
    dialog.setHeaderTitle("SonarQube Settings");

    SonarSettingsDTO oldSonarSettings = sonarSettingsDAO.getSettings();

    TextField url = new TextField("URL");
    PasswordField token = new PasswordField("Token");
    token.setRequiredIndicatorVisible(true);
    TextField component = new TextField("Component");

    if (oldSonarSettings != null) {
      url.setValue(oldSonarSettings.getUrl());
      token.setValue(oldSonarSettings.getToken());
      component.setValue(oldSonarSettings.getComponent());
    }

    VerticalLayout dialogLayout = new VerticalLayout(url, token, component);
    dialogLayout.setPadding(false);
    dialogLayout.setSpacing(false);
    dialogLayout.setAlignItems(Alignment.STRETCH);
    dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");
    dialog.setCloseOnOutsideClick(false);
    dialog.add(dialogLayout);

    Button saveButton =
        new Button(
            "Überprüfen und Speichern",
            ev -> {
              SonarSettingsDTO newSettings =
                  new SonarSettingsDTO(url.getValue(), token.getValue(), component.getValue());
              try {
                boolean check = sonarService.checkSettings(newSettings);
                if (!check) {
                  Notification notification =
                      createClosableNotification(
                          NotificationVariant.LUMO_ERROR,
                          0,
                          "Check fehlgeschlagen. Überprüfen Sie die eingegebenen Daten.");
                  notification.open();
                } else {
                  SonarSettingsDTO result = sonarSettingsDAO.save(newSettings);
                  if (newSettings.equals(result)) {
                    Notification notification =
                        createClosableNotification(
                            NotificationVariant.LUMO_SUCCESS,
                            3000,
                            "SonarQube-Einbindung erfolgreich.");
                    notification.open();
                    dialog.close();
                  }
                }
              } catch (IOException e) {
                createClosableNotification(
                        NotificationVariant.LUMO_ERROR,
                        0,
                        "Check fehlgeschlagen. Überprüfen Sie die eingegebenen Daten.")
                    .open();
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
                    .severe("SonarSettings konnten nicht überprüft werden. \n" + e.getMessage());
              }
            });
    saveButton.getStyle().set("margin-right", "auto");
    saveButton.getStyle().set("margin-left", "auto");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    Button closeButton = new Button(new Icon("lumo", "cross"), e -> dialog.close());
    closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    dialog.getHeader().add(closeButton);
    dialog.getFooter().add(saveButton);
    return dialog;
  }

  public static class SmellDetailsFormLayout extends FormLayout {
    private final RawHtml message = new RawHtml();
    private final Span location = new Span();
    public final VerticalLayout detail = new VerticalLayout(location, message);

    public SmellDetailsFormLayout() {
      detail.getStyle().set("flex-grow", "1");
      add(detail);
    }

    public void setSmellDetails(Issue issue) {
      Document htmlDesc = Jsoup.parse(RuleList.getInstance().toMap().get(issue.getRule()));
      detail.add(createCodeExamples(htmlDesc));
      message.setHtml(Objects.requireNonNull(htmlDesc.select("p").first()).toString());
      location.setText(issue.getComponent() + " " + issue.getLine());
      location.getStyle().set("font-style", "italic");
    }
  }

  private static ComponentRenderer<SmellDetailsFormLayout, Issue> createSmellDetailRenderer() {
    return new ComponentRenderer<>(
        SmellDetailsFormLayout::new, SmellDetailsFormLayout::setSmellDetails);
  }
}
