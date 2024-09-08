package org.arc42.dokumentation.view.components.custom;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class VerticalSpacerGenerator {

  private final String height;

  public VerticalSpacerGenerator(String height) {
    this.height = height;
  }

  public Div buildVerticalSpacer() {
    final Div div = new Div();
    div.addClassName("vertical-spacer");
    Label emptyLabel = new Label(" ");
    emptyLabel.setHeight(this.height);
    VerticalLayout vl = new VerticalLayout();
    vl.add(emptyLabel);
    div.add(vl);
    return div;
  }
}
