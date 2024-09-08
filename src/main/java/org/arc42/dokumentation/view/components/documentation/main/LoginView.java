package org.arc42.dokumentation.view.components.documentation.main;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginI18n.ErrorMessage;
import com.vaadin.flow.component.login.LoginI18n.Form;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.arc42.dokumentation.control.logic.Login;
import org.arc42.dokumentation.model.dto.general.FeLoginDTO;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.RegisterComponent;
import org.arc42.dokumentation.view.util.data.Roles;
import org.neo4j.driver.exceptions.NoSuchRecordException;

@PageTitle("Login")
@Route("login")
@RouteAlias(value = "")
@CssImport("themes/softwaredocumentation/loginViewTheme.css")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

  @Override
  public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
    if (UI.getCurrent().getSession().getAttribute(Roles.CURRENTUSER) != null) {
      beforeEnterEvent.rerouteTo(LoginView.class);
    } else {
      this.setUp();
    }
  }

  private void setUp() {
    HorizontalLayout hlBackground = new HorizontalLayout();
    hlBackground.setSizeFull();
    Image background = new Image("stAugustin.jpg", "alt text");
    background.setSizeFull();
    // remove padding
    background.getStyle().set("padding", "0");
    background.getStyle().set("margin", "0");
    // ToDo: add css to add background image
    addClassName("login-view");
    background.getStyle().set("object-fit", "cover");
    hlBackground.getStyle().setBackground("red");
    Form form = new Form();
    form.setTitle("Anmeldung");
    form.setUsername("Benutzername");
    form.setPassword("Passwort");
    form.setSubmit("Anmelden");

    ErrorMessage errorMessage = new ErrorMessage();
    errorMessage.setTitle("Anmeldung fehlgeschlagen");
    errorMessage.setUsername("Benutzername darf nicht leer sein.");
    errorMessage.setPassword("Passwort darf nicht leer sein.");
    LoginI18n i18n = new LoginI18n();
    i18n.setErrorMessage(errorMessage);
    i18n.setForm(form);
    LoginI18n.Header i18nHeader = new LoginI18n.Header();
    LoginI18n.Form i18nForm = i18n.getForm();
    i18n.setHeader(i18nHeader);
    i18nForm.setTitle("Login");
    i18nForm.setUsername("Benutzername");
    i18nForm.setPassword("Passwort");
    i18nForm.setSubmit("Einloggen");
    i18nForm.setForgotPassword("Noch kein Konto? Hier registrieren!");
    i18n.setForm(i18nForm);

    LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
    i18nErrorMessage.setTitle("Authentifizierungsfehler!");
    i18nErrorMessage.setMessage("Ihr Benutzername oder Passwort ist falsch!");
    i18n.setErrorMessage(i18nErrorMessage);

    LoginOverlay loginOverlay = new LoginOverlay();
    Image titleComponent = new Image("logo3.png", "alt text");
    loginOverlay.setTitle(titleComponent);
    loginOverlay.setI18n(i18n);
    loginOverlay.setOpened(true);
    loginOverlay.getElement().setAttribute("autocomplete", "on");
    loginOverlay.getElement().setAttribute("method", "post");
    loginOverlay.getElement().setAttribute("z", "-1");
    // set background color
    // loginOverlay.getStyle().set("background-color", "var(--lumo-contrast-5pct)");
    // set background as image
    // loginOverlay.getStyle().set("background-image", "src/main/resources/images/logo3.png");

    loginOverlay.addLoginListener(
        (ComponentEventListener<AbstractLogin.LoginEvent>)
            loginEvent -> {
              FeLoginDTO logindto =
                  new FeLoginDTO(loginEvent.getUsername(), loginEvent.getPassword());
              try {
                Login.login(logindto);
                loginOverlay.close();
              } catch (NoSuchRecordException e) {
                loginOverlay.setError(true);
                loginOverlay.setOpened(true);
              } catch (Exception e) {
                new NotificationWindow(e.getMessage(), 5000, "error");
                e.printStackTrace();
              }
            });

    loginOverlay.addForgotPasswordListener(
        forgotPasswordEvent -> new RegisterComponent().getDialog().open());
    getStyle().set("background-color", "var(--lumo-contrast-5pct)");
    setJustifyContentMode(JustifyContentMode.CENTER);
    setSizeFull();
    setAlignItems(Alignment.CENTER);
    loginOverlay.getElement().removeProperty("animation");
    loginOverlay.getElement().removeProperty("inset");
    loginOverlay.getElement().removeProperty("position");

    hlBackground.add(loginOverlay);
    hlBackground.add(background);
    loginOverlay
        .getChildren()
        .forEach(
            x -> {
              System.out.println(x.getElement().getTag());
              x.getElement().getStyle().set("background-color", "transparent");
            });
    add(hlBackground);
  }
}