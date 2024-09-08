package org.arc42.dokumentation.view.components.custom;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.ArrayList;
import java.util.List;

public class BadgeComponent extends Span {

  private String color;

  public BadgeComponent(String text, String color) {
    setText(text);
    setId("my-badge");
    getStyle().set("background-color", color);
  }

  public BadgeComponent(String[] strings) {}

  public VerticalLayout getAllBadges() {
    BadgeComponent functionalSuitability = new BadgeComponent("Funktionalität", "coral");
    BadgeComponent performanceEfficiency = new BadgeComponent("Effizienz", "mediumseagreen");
    BadgeComponent compatibility = new BadgeComponent("Kompatibilität", "darkkhaki");
    BadgeComponent usability = new BadgeComponent("Benutzbarkeit", "darkslategray");
    BadgeComponent reliability = new BadgeComponent("Zuverlässigkeit", "cadetblue");
    BadgeComponent security = new BadgeComponent("Sicherheit", "slategray");
    BadgeComponent maintainability = new BadgeComponent("Wartbarkeit", "mediumslateblue");
    BadgeComponent portability = new BadgeComponent("Portabilität", "mediumvioletred");

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
        portability);
    return verticalLayout;
  }

  public static List<BadgeComponent> getAllBadgesAsList() {
    List<BadgeComponent> list = new ArrayList<>();
    list.add(new BadgeComponent("Funktionalität", "coral"));
    list.add(new BadgeComponent("Effizienz", "mediumseagreen"));
    list.add(new BadgeComponent("Kompatibilität", "darkkhaki"));
    list.add(new BadgeComponent("Benutzbarkeit", "darkslategray"));
    list.add(new BadgeComponent("Zuverlässigkeit", "cadetblue"));
    list.add(new BadgeComponent("Sicherheit", "slategray"));
    list.add(new BadgeComponent("Wartbarkeit", "mediumslateblue"));
    list.add(new BadgeComponent("Portabilität", "mediumvioletred"));
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
}
