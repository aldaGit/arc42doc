package org.arc42.dokumentation.view.components.custom;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class OpenableDetailsViewComponent extends Div {
  H4 innerTitle;
  Button editButton;
  HorizontalLayout headline;
  VerticalLayout wrapper = new VerticalLayout();

  public OpenableDetailsViewComponent(String title, String url, Component content) {
    innerTitle = new H4(title);
    if (title.equals("Checkliste")) {
      Label verpflichtend = new Label("Verpflichtend");
      verpflichtend.getStyle().set("color", "red");
      verpflichtend.getStyle().set("font-size", "12px");
      verpflichtend.getStyle().set("margin-left", "auto");
      innerTitle.getStyle().set("margin-left", "auto");
      editButton = new Button("Bearbeiten");
      verpflichtend.getStyle().set("margin-left", "auto");
      verpflichtend.getStyle().set("padding-top", "10px");
      headline = new HorizontalLayout(innerTitle, editButton, verpflichtend);
      editButton.addClickListener(
          buttonClickEvent -> editButton.getUI().ifPresent(ui -> ui.navigate("/arc42/" + url)));
      headline.setVerticalComponentAlignment(Alignment.END, editButton);
      headline.setVerticalComponentAlignment(Alignment.CENTER, verpflichtend);
      content.getElement().getStyle().set("width", "100%");
      content.getElement().getStyle().set("height", "300px");
      wrapper.add(headline, content);
      wrapper.setWidthFull();
      this.setWidthFull();
      // if title does not match "checkliste"
      if (!title.equalsIgnoreCase("checkliste")) {
        wrapper.setVisible(false);
      }
    } else {
      innerTitle.getStyle().set("margin-left", "auto");
      editButton = new Button("Bearbeiten");

      headline = new HorizontalLayout(innerTitle, editButton);
      editButton.addClickListener(
          buttonClickEvent -> editButton.getUI().ifPresent(ui -> ui.navigate("/arc42/" + url)));
      headline.setVerticalComponentAlignment(Alignment.END, editButton);
      content.getElement().getStyle().set("width", "100%");
      content.getElement().getStyle().set("height", "300px");
      wrapper.add(headline, content);
      wrapper.setWidthFull();
      this.setWidthFull();
      // if title does not match "checkliste"
      if (!title.equalsIgnoreCase("checkliste")) {
        wrapper.setVisible(false);
      }
    }
    add(wrapper);
  }

  @Override
  public void setVisible(boolean visible) {
    wrapper.setVisible(visible);
  }
}