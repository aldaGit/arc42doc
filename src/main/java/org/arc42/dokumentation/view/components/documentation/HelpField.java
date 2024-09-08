package org.arc42.dokumentation.view.components.documentation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class HelpField {

  public static VerticalLayout createDialogLayout(Dialog dialog, String text, String title) {
    Span span = new Span(text);
    H2 headline = new H2("Hilfe " + " " + title);
    headline
        .getStyle()
        .set("margin", "var(--lumo-space-m) 0 0 0")
        .set("font-size", "1.5em")
        .set("font-weight", "bold");

    Button verstanden = new Button("Verstanden!", e -> dialog.close());
    verstanden.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    HorizontalLayout buttonLayout = new HorizontalLayout(verstanden);
    buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

    VerticalLayout dialogLayout = new VerticalLayout(headline, span, buttonLayout);

    dialogLayout.setPadding(false);
    dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
    dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");

    return dialogLayout;
  }
}
