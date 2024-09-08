package org.arc42.dokumentation.view.util;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.view.components.customComponents.BadgeComponent;

public class SelectBadgeComponent extends HorizontalLayout {

  private VerticalLayout selectedBatches;
  private VerticalLayout notSelectedBatches;

  public SelectBadgeComponent(List<String> selectedBadges, List<String> allBadges) {
    selectedBatches =
        new BadgeComponent()
            .getBadgesForList(
                selectedBadges, BadgeGlossary.COLORS.subList(0, selectedBadges.size()));
    VerticalLayout selectedBatchesLayout = new VerticalLayout();
    Span selected = new Span("Ausgewählt");
    selectedBatchesLayout.add(selected, selectedBatches);

    notSelectedBatches =
        new BadgeComponent()
            .getBadgesForList(
                getNotSelected(selectedBadges, allBadges),
                BadgeGlossary.COLORS.subList(selectedBadges.size(), BadgeGlossary.COLORS.size()));
    VerticalLayout notSelectedBatchesLayout = new VerticalLayout();
    Span notSelected = new Span("Nicht ausgewählt");
    notSelectedBatchesLayout.add(notSelected, notSelectedBatches);

    addListener();
    add(selectedBatchesLayout, notSelectedBatchesLayout);
  }

  public List<String> getSelectedBatches() {
    List<String> selected = new ArrayList<>();
    List<Component> components = selectedBatches.getChildren().toList();
    for (Component component : components) {
      if (component instanceof BadgeComponent) {
        selected.add(((BadgeComponent) component).getText());
      }
    }
    return selected;
  }

  private void createListener(BadgeComponent badgeComponent) {
    badgeComponent.addClickListener(
        (ComponentEventListener<ClickEvent<Span>>)
            event -> {
              if (badgeComponent.getParent().get() == notSelectedBatches) {
                notSelectedBatches.remove(badgeComponent);
                selectedBatches.add(badgeComponent);
              } else if (badgeComponent.getParent().get() == selectedBatches) {
                selectedBatches.remove(badgeComponent);
                notSelectedBatches.add(badgeComponent);
              }
            });
  }

  private void addListener() {
    for (int i = 0; i < notSelectedBatches.getComponentCount(); i++) {
      if (notSelectedBatches.getComponentAt(i) instanceof BadgeComponent) {
        createListener((BadgeComponent) notSelectedBatches.getComponentAt(i));
      }
    }
    for (int i = 0; i < selectedBatches.getComponentCount(); i++) {
      if (selectedBatches.getComponentAt(i) instanceof BadgeComponent) {
        createListener((BadgeComponent) selectedBatches.getComponentAt(i));
      }
    }
  }

  private List<String> getNotSelected(List<String> selected, List<String> all) {
    List<String> notSelected = new ArrayList<>();
    for (String s : all) {
      if (!selected.contains(s)) {
        notSelected.add(s);
      }
    }
    return notSelected;
  }

  public void setSelectedBatches(List<String> selectedBatches, List<String> all) {
    this.selectedBatches.removeAll();
    this.notSelectedBatches.removeAll();
    VerticalLayout selected =
        new BadgeComponent()
            .getBadgesForList(
                selectedBatches, BadgeGlossary.COLORS.subList(0, selectedBatches.size()));
    for (Component badge : selected.getChildren().toList()) {
      this.selectedBatches.add(badge);
    }
    VerticalLayout notSelected =
        new BadgeComponent()
            .getBadgesForList(
                getNotSelected(selectedBatches, all),
                BadgeGlossary.COLORS.subList(selectedBatches.size(), BadgeGlossary.COLORS.size()));
    for (Component badge : notSelected.getChildren().toList()) {
      this.notSelectedBatches.add(badge);
    }
    addListener();
  }
}
