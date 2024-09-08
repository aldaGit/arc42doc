package org.arc42.dokumentation.view.util.data;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import java.util.HashMap;
import java.util.Map;

public class RiskMethods {
  Map<String, Component> menuBarSet = new HashMap<>();

  public void add(String s, Component item) {
    menuBarSet.put(s, item);
  }

  public void select(String l) {
    System.out.println("select" + l);
    if (l == null || l.equals("")) {
      return;
    }

    if (l.equals("Checkliste")) {
      return;
    }
    menuBarSet.getOrDefault(l, new Div()).setVisible(true);
  }

  public void deselect(String l) {
    System.out.println("deselect" + l);
    if (l == null || l.equals("")) {
      return;
    }
    if (l.equals("Checkliste")) {
      return;
    }
    menuBarSet.get(l).setVisible(false);
  }
}