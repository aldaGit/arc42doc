package org.arc42.dokumentation.view.components.documentation;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.arc42.dokumentation.control.logic.Login;
import org.arc42.dokumentation.model.dto.general.FeLoginDTO;

public class RegisterComponent extends Div {

  private final Button registerButton = new Button("Registrieren");
  private final Dialog dialog = new Dialog();

  public Dialog getDialog() {
    return dialog;
  }

  public RegisterComponent() {
    VerticalLayout dialogLayout = createDialogLayout(this.dialog);
    this.dialog.add(dialogLayout);
    this.dialog.open();
    getStyle()
        .set("position", "fixed")
        .set("top", "0")
        .set("right", "0")
        .set("bottom", "0")
        .set("left", "0")
        .set("align-items", "center")
        .set("justify-content", "center");
  }

  private VerticalLayout createDialogLayout(Dialog dialog) {
    H2 headline = new H2("Registrierung");
    headline.getStyle().set("margin-top", "0");
    H5 body =
        new H5("Bitte geben Sie für die Registrierung einen Benutzernamen und ein Passwort ein!");
    body.addClassNames("text-s", "s-ms");
    TextField username = new TextField("Benutzername");
    PasswordField password = new PasswordField("Passwort");

    username.setRequired(true);
    password.setRequired(true);
    password.setHelperText("Ein Passwort muss mindestens 8 Zeichen enthalten.");
    username.addValueChangeListener(
        (HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>)
            textFieldStringComponentValueChangeEvent -> username.setInvalid(false));

    Button cancelButton = new Button("Abbrechen", e -> this.dialog.close());
    this.registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    this.registerButton.addClickShortcut(Key.ENTER);

    HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, registerButton);
    buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
    buttonLayout.getStyle().set("margin-top", "var(--lumo-space-m)");

    VerticalLayout dialogLayout =
        new VerticalLayout(headline, body, username, password, buttonLayout);
    dialogLayout.setPadding(false);
    dialogLayout.setSpacing(false);
    dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
    dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

    Binder<FeLoginDTO> binder = new Binder<>(FeLoginDTO.class);
    binder
        .forField(username)
        .asRequired("Benutzername muss angegeben werden!")
        .bind(FeLoginDTO::getUsername, FeLoginDTO::setUsername);
    binder
        .forField(password)
        .asRequired("Bitte Passwort eingeben!")
        .withValidator(new StringLengthValidator("Bitte mindestens 8 Zeichen!", 8, null))
        .bind(FeLoginDTO::getPassword, FeLoginDTO::setPassword);
    binder.addStatusChangeListener(event -> binder.isValid());

    registerButton.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            buttonClickEvent -> {
              if (binder.validate().isOk()) {
                FeLoginDTO logindto = new FeLoginDTO(username.getValue(), password.getValue());
                try {
                  if (Login.userExist(logindto)) {
                    username.setInvalid(true);
                    username.setErrorMessage(
                        "Benutzer existiert bereits! Bitte einen anderen wählen.");
                  } else {
                    Login.register(logindto);
                    dialog.close();
                    Notification notification =
                        new Notification(
                            "Benutzer " + username.getValue() + " erfolgreich erstellt!");
                    notification.setPosition(Notification.Position.TOP_END);
                    notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
                    notification.setDuration(7000);
                    notification.open();
                  }
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            });
    return dialogLayout;
  }
}
