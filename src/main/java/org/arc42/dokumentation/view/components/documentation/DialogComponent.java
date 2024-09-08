package org.arc42.dokumentation.view.components.documentation;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class DialogComponent extends Div {

  public Button getDeleteButton() {
    return this.deleteButton;
  }

  private final Button createButton = new Button("Erstellen");
  private final Button deleteButton = new Button("Löschen");

  private Dialog dialog = new Dialog();
  private TextField titel = new TextField("Titel");

  public Dialog getDialog() {
    return this.dialog;
  }

  public DialogComponent(String dialog) {
    switch (dialog) {
      case "delete":
        this.dialog = createDialogDelete(this.dialog);
        break;
      case "createDoku":
        this.dialog = createDialogCreateDoku();
        break;
      case "notSave":
        this.dialog = createNotSavedDialog();
    }

    this.dialog.open();

    getStyle()
        .set("position", "fixed")
        .set("top", "0")
        .set("right", "0")
        .set("bottom", "0")
        .set("left", "0")
        .set("display", "flex")
        .set("align-items", "center")
        .set("justify-content", "center");
  }

  private Dialog createNotSavedDialog() {
    H3 headline = new H3("Achtung! ");
    headline.getStyle().set("margin-top", "0");
    H4 body =
        new H4(
            "Sie haben Änderungen vorgenommen aber nicht gespeichert. Möchten Sie wirklich"
                + " fortfahren?");

    this.deleteButton.setText("Nein");
    this.createButton.setText("Ja");
    this.createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    HorizontalLayout buttonLayout = new HorizontalLayout(this.deleteButton, this.createButton);
    buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
    buttonLayout.getStyle().set("margin-top", "var(--lumo-space-m)");

    VerticalLayout dialogLayout = new VerticalLayout(headline, body, buttonLayout);
    dialogLayout.setPadding(false);
    dialogLayout.setSpacing(false);
    dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
    dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");
    dialog.add(dialogLayout);

    return dialog;
  }

  private Dialog createDialogDelete(Dialog dialog) {

    H2 headline = new H2("Löschen");
    headline.getStyle().set("margin-top", "0");
    H3 body = new H3("Möchten Sie wirklich diese Dokumentation löschen?");

    Button cancelButton = new Button("Abbrechen", e -> this.dialog.close());
    this.deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
    this.deleteButton.addClickShortcut(Key.ENTER);

    HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, this.deleteButton);
    buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
    buttonLayout.getStyle().set("margin-top", "var(--lumo-space-m)");

    VerticalLayout dialogLayout = new VerticalLayout(headline, body, buttonLayout);
    dialogLayout.setPadding(false);
    dialogLayout.setSpacing(false);
    dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
    dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");
    dialog.add(dialogLayout);

    return dialog;
  }

  private Dialog createDialogCreateDoku() {
    H3 headline = new H3("Erstellung einer neuen Arc42 Dokumentation");
    headline.getStyle().set("margin-top", "0");

    this.titel = new TextField("Titel");
    this.titel.setRequired(true);
    this.titel.setMinLength(1);
    this.titel.setAutofocus(true);

    Button cancelButton = new Button("Abbrechen", e -> this.dialog.close());
    this.createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    this.createButton.addClickShortcut(Key.ENTER);

    HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, createButton);
    buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
    buttonLayout.getStyle().set("margin-top", "var(--lumo-space-m)");

    VerticalLayout dialogLayout = new VerticalLayout(headline, this.titel, buttonLayout);
    dialogLayout.setPadding(false);
    dialogLayout.setSpacing(false);
    dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
    dialogLayout.getStyle().set("width", "20rem").set("max-width", "100%");
    dialog.add(dialogLayout);
    return this.dialog;
  }

  public TextField getTitel() {
    return titel;
  }

  public Button getCreateButton() {
    return this.createButton;
  }
}
