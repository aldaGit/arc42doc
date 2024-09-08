package org.arc42.dokumentation.view.components.documentation.main;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class DemoLayout extends AppLayout {

  public DemoLayout() {
    createHeader();
  }

  private void createHeader() {
    H3 heading = new H3("Arc42 Dokumentation - Demo Toolkit");

    HorizontalLayout header = new HorizontalLayout(heading);
    header.setVerticalComponentAlignment(Alignment.CENTER, heading);
    heading.getStyle().set("margin-left", "auto");
    heading.getStyle().set("margin-right", "auto");
    header.setWidth("100%");
    addToNavbar(header);
  }
}