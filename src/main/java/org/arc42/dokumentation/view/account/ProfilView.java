package org.arc42.dokumentation.view.account;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.arc42.dokumentation.model.user.ProfilDAO;
import org.arc42.dokumentation.model.user.ProfilDTO;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.data.Links;
import org.arc42.dokumentation.view.util.data.NotificationType;
import org.arc42.dokumentation.view.util.data.Roles;

@Route(value = Links.PROFIL, layout = MainLayout.class)
@PageTitle("Account")
public class ProfilView extends Div implements BeforeEnterObserver {

  private final Button save = new Button("Save");
  private final Button cancel = new Button("Cancel");
  private String profilID;
  private Span usernameField = new Span();
  private final EmailField mailField = setUpEmailField();
  private final TextField firstNameField = new TextField("Vorname");
  private final TextField lastNameField = new TextField("Nachname");
  private final TextField phoneField = new TextField("Telefonnummer");
  Image profilBild = new Image("user.png", "Icon");

  @Override
  public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
    profilID = (String) UI.getCurrent().getSession().getAttribute(Roles.CURRENTUSER);
    // if this is null, redirect to login
    if (profilID == null) {
      UI.getCurrent().navigate("login");
    }
    usernameField = new Span("Username: " + profilID);
    this.doSetLayout();
    System.out.println("Getting profile for " + profilID);
    ProfilDTO currentProfile = ProfilDAO.getInstance().findByUsername(profilID);
    System.out.println("Profile: " + currentProfile);
    // fill in fields
    ProfilDTO insertable;
    if (currentProfile != null) {
      insertable = currentProfile.makeSaveableCopy();
      System.out.println("Insertable Copy L60: " + insertable);
      mailField.setValue(insertable.getEmail());
      firstNameField.setValue(insertable.getFirstName());
      lastNameField.setValue(insertable.getLastName());
      phoneField.setValue(insertable.getPhone());
    } else {
      new NotificationWindow(
          "Profil nicht gefunden, viel Spaß beim Erstellen deines ersten Profils!",
          NotificationType.MEDIUM,
          NotificationType.ERROR);
      // insertable = Profi
      mailField.clear();
      firstNameField.clear();
      lastNameField.clear();
    }
  }

  public void doSetLayout() {
    HorizontalLayout header = new HorizontalLayout();
    VerticalLayout centerHeader = new VerticalLayout();
    profilBild.setHeight("100px");
    centerHeader.add(profilBild);
    centerHeader.setAlignItems(FlexComponent.Alignment.CENTER);
    header.setAlignItems(FlexComponent.Alignment.CENTER);
    header.setAlignSelf(FlexComponent.Alignment.CENTER);
    header.add(centerHeader);
    final FormLayout formLayout = new FormLayout();
    formLayout.add(
        this.usernameField,
        this.firstNameField,
        this.lastNameField,
        this.mailField,
        this.phoneField,
        this.save,
        this.cancel);
    formLayout.setColspan(this.usernameField, 2);
    formLayout.setColspan(this.firstNameField, 1);
    formLayout.setColspan(this.lastNameField, 1);
    formLayout.setColspan(this.mailField, 1);
    formLayout.setColspan(this.phoneField, 1);
    formLayout.setColspan(this.mailField, 1);
    formLayout.setColspan(this.save, 1);
    formLayout.setColspan(this.cancel, 1);
    formLayout.getStyle().set("margin-left", "20px");
    formLayout.getStyle().set("margin-top", "20px");
    formLayout.getStyle().set("margin-bottom", "20px");
    formLayout.getStyle().set("margin-right", "20px");
    // center the formlayout in the wrapper and have it be 60% of the sreen width
    VerticalLayout formLayoutWrapper = new VerticalLayout(formLayout);
    formLayoutWrapper.getStyle().set("width", "60%");
    formLayoutWrapper.getStyle().set("margin", "auto");
    formLayoutWrapper.setAlignItems(Alignment.CENTER);

    save.addClickListener(
        event -> {
          // find username funktioniert net
          ProfilDTO dbProfilDTO = ProfilDAO.getInstance().findByUsername(profilID);
          if (dbProfilDTO == null) {
            dbProfilDTO = ProfilDAO.getInstance().createProfilForUserIfExists(profilID);
          }
          if (dbProfilDTO != null) {
            setAttributes(dbProfilDTO);

            ProfilDAO.getInstance().update(dbProfilDTO);
            NotificationWindow n =
                new NotificationWindow(
                    "Profiländerung gespeichert!",
                    NotificationType.SHORT,
                    NotificationType.SUCCESS);
            n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
          } else {
            new NotificationWindow(
                "Ne dbProfilDTO ist null", NotificationType.MEDIUM, NotificationType.ERROR);
          }
        });

    this.add(header, formLayoutWrapper);
  }

  public static void showProfilPage() {
    // UI.getCurrent().getSession().setAttribute(Roles.CURRENTUSER, null);
    UI.getCurrent().navigate("profil");
  }

  public EmailField setUpEmailField() {
    EmailField invalidEmailField = new EmailField();
    invalidEmailField.setLabel("Email address");
    invalidEmailField.getElement().setAttribute("name", "email");
    invalidEmailField.setErrorMessage("Enter a valid email address");
    invalidEmailField.setClearButtonVisible(true);
    invalidEmailField.setInvalid(true);
    return invalidEmailField;
  }

  public void split() {}

  public void setAttributes(ProfilDTO dbProfilDTO) {
    dbProfilDTO.setEmail(mailField.getValue());
    dbProfilDTO.setFirstName(firstNameField.getValue());
    dbProfilDTO.setLastName(lastNameField.getValue());
    dbProfilDTO.setPhone(phoneField.getValue());
  }
}