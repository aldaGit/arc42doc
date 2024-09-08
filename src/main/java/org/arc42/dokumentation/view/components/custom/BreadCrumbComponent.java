package org.arc42.dokumentation.view.components.custom;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;

public class BreadCrumbComponent extends Div {

  public BreadCrumbComponent(String[] pages) {

    for (String page : pages) {
      add(new Text(page), new Icon("lumo", "angle-right"));
    }
  }
}
