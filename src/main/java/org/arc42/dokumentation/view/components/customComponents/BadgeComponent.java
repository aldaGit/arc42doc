package org.arc42.dokumentation.view.components.customComponents;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.view.util.BadgeGlossary;

public class BadgeComponent extends Span {

  private String color;

  public BadgeComponent(String text, String color) {
    setText(text);
    setId("my-badge");
    getStyle().set("background-color", color);
  }

  public BadgeComponent() {}

  public VerticalLayout getAllBadges() {
    BadgeComponent functionalSuitability = new BadgeComponent("Funktionalität", "coral");
    BadgeComponent performanceEfficiency = new BadgeComponent("Effizienz", "mediumseagreen");
    BadgeComponent compatibility = new BadgeComponent("Kompatibilität", "darkkhaki");
    BadgeComponent usability = new BadgeComponent("Benutzbarkeit", "darkslategray");
    BadgeComponent reliability = new BadgeComponent("Zuverlässigkeit", "cadetblue");
    BadgeComponent security = new BadgeComponent("Sicherheit", "slategray");
    BadgeComponent maintainability = new BadgeComponent("Wartbarkeit", "mediumslateblue");
    BadgeComponent portability = new BadgeComponent("Portabilität", "mediumvioletred");
    BadgeComponent sustainability = new BadgeComponent("Nachhaltigkeit", "green");

    VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.add(
        compatibility,
        functionalSuitability,
        performanceEfficiency,
        compatibility,
        usability,
        reliability,
        security,
        maintainability,
        portability,
        sustainability);
    return verticalLayout;
  }

  public static List<BadgeComponent> getAllBadgesAsList() {
    List<BadgeComponent> list = new ArrayList<>();
    for (int i = 0; i < BadgeGlossary.QUALITYBADGES.size(); i++) {
      list.add(new BadgeComponent(BadgeGlossary.QUALITYBADGES.get(i), BadgeGlossary.COLORS.get(i)));
    }
    return list;
  }

  public static List<BadgeComponent> getAllConceptBadgesAsList() {
    List<BadgeComponent> list = new ArrayList<>();
    for (int i = 0; i < BadgeGlossary.CONCEPTBADGES.size(); i++) {
      list.add(new BadgeComponent(BadgeGlossary.CONCEPTBADGES.get(i), BadgeGlossary.COLORS.get(i)));
    }
    return list;
  }

  public BadgeComponent getBadge(String text) {
    VerticalLayout verticalLayout = getAllBadges();
    for (int i = 0; i < verticalLayout.getComponentCount(); i++) {
      if (verticalLayout.getComponentAt(i) instanceof BadgeComponent) {
        if (((BadgeComponent) verticalLayout.getComponentAt(i)).getText().equals(text)) {
          return (BadgeComponent) verticalLayout.getComponentAt(i);
        }
      }
    }
    return null;
  }

  public String getColor() {
    return color;
  }

  public VerticalLayout getBadgesForList(List<String> names, List<String> colors) {
    VerticalLayout verticalLayout = new VerticalLayout();
    if (names.size() <= colors.size()) {
      for (int i = 0; i < names.size(); i++) {
        verticalLayout.add(new BadgeComponent(names.get(i), colors.get(i)));
      }
    }
    return verticalLayout;
  }
}
