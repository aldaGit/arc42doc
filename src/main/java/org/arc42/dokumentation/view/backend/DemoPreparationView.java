package org.arc42.dokumentation.view.backend;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import java.util.List;
import org.arc42.dokumentation.model.dao.arc42documentation.Arc42DokuNameDAO;
import org.arc42.dokumentation.model.dao.general.UserDAO;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.arc42.dokumentation.view.components.documentation.main.DemoLayout;
import org.arc42.dokumentation.view.util.data.Roles;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@PageTitle("Demo")
@Route(value = "demomanagement/dashboard", layout = DemoLayout.class)
public class DemoPreparationView extends Div {

  private static final String DEMO_USER_NAME = "james";
  // this view is kind of equivalent to
  // src/main/java/org/arc42/dokumentation/view/backend/Draft.java
  private static final String PROJECT_NAME = "Digitalisierung einer Grundschule in Bonn";
  private String projectURL = "100000";
  private Component fillButton;
  private VerticalLayout vl;

  private void purgeDatabase() {
    // delete all data from database
    Arc42DokuNameDAO nameDao = Arc42DokuNameDAO.getInstance();
    List<DokuNameDTO> allNames = nameDao.findAll();
    allNames.forEach(nameDao::delete);
  }

  public DemoPreparationView() {

    // layouting:
    // one horizontal layout with a centered vertical layout
    HorizontalLayout hl = new HorizontalLayout();
    hl.setAlignItems(Alignment.CENTER);
    this.vl = new VerticalLayout();
    vl.setAlignItems(Alignment.CENTER);
    vl.add(purgeBox());
    // also possible:

    this.add(vl);
  }

  private Component goToFilling() {
    // heading.getStyle().set("margin-left", "auto");
    // heading.getStyle().set("margin-right", "auto");
    LoggerFactory.getLogger(DemoPreparationView.class)
        .info("getting link to demo project fill options");
    Anchor anchor =
        new Anchor("demomanagement/dashboard" + "/?newArcID=" + projectURL + "/", "Fill Project");
    anchor.getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-secondary-text-color)");
    return anchor;
  }

  private Component purgeBox() {
    // add a button to purge the database
    // and create a new demo project
    Button purgeButton = new Button("Purge Database");
    purgeButton.getStyle().set("display", "block");
    purgeButton.getStyle().set("margin-left", "30%");
    purgeButton.getStyle().set("margin-right", "30%");
    purgeButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
    purgeButton.addClickListener(
        event -> {
          LoggerFactory.getLogger(DemoPreparationView.class).info("Purging database");
          purgeDatabase();
          LoggerFactory.getLogger(DemoPreparationView.class)
              .info("Resetting login for user: james");
          resetLoginForJames();
          LoggerFactory.getLogger(DemoPreparationView.class).info("Creating demo project");
          createDemoProject();

          UserDAO.getInstance().resetToDefaultExampleDB();
        });
    Div div = new Div(makeExplanationForPurge(), makeDataConfigDisplay(), purgeButton);
    div.getStyle().set("border", "1px solid black");
    return div;
  }

  private Component makeDataConfigDisplay() {
    Paragraph p =
        new Paragraph(
            "The username and password for the demo user are both set to \""
                + DEMO_USER_NAME
                + "\".\nThe demo project is called: "
                + PROJECT_NAME
                + ".");

    p.getStyle().set("padding", "10px");
    // get newline to mean something
    p.getStyle().set("white-space", "pre-line");
    return p;
  }

  private Component makeExplanationForPurge() {
    Paragraph p =
        new Paragraph(
            "This button will purge the database and create a new demo project and forcibly make a"
                + " userlogin valid.");

    p.getStyle().set("padding", "10px");
    return p;
  }

  private void resetLoginForJames() {
    if (!UserDAO.getInstance().existUser(DEMO_USER_NAME)) {

      UserDAO.getInstance()
          .setUser(
              DEMO_USER_NAME, new BCryptPasswordEncoder().encode(DEMO_USER_NAME)); // create james
    }
  }

  private void createDemoProject() {
    // put new Arc42 documentation into db and fill it
    Arc42DokuNameDAO nameDao = Arc42DokuNameDAO.getInstance();
    DokuNameDTO name = new DokuNameDTO(PROJECT_NAME);
    // save depends on VaadinSessions Roles.CURRENTUSER being set
    VaadinSession.getCurrent().setAttribute(Roles.CURRENTUSER, DEMO_USER_NAME);
    DokuNameDTO dbDoku = nameDao.save(name);
    if (dbDoku == null) {
      LoggerFactory.getLogger(DemoPreparationView.class).error("Error while creating demo project");
      return;
    }
    this.projectURL = dbDoku.getId();
    this.fillButton = goToFilling();
    LoggerFactory.getLogger(DemoPreparationView.class).info("adding fill button");
    this.vl.add(this.fillButton);
    // add some content to the new project
    // LoesungStrategieDAO loesungStrategieDAO = LoesungStrategieDAO.getInstance();
    // loesungStrategieDAO.save(new ImageDTO("Loesungsstrtegie1")); // does this depend on our url?
  }
}
