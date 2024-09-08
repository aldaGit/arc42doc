package org.arc42.dokumentation.view.components.documentation.layout.staticComponents;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.arc42.dokumentation.view.util.data.RiskMethods;

public class ViewUtils {
  private static final String PRIMARY = "primary";

  /**
   * Creates a dialog with a help text.
   *
   * @param arcId
   * @param currentlySelected (1-4) 1 = checkliste 2 = bogen 3 = swot 4 = risiko map
   * @return
   */
  public static MenuBar addMenuBar(String arcId, int currentlySelected) {
    Text selectedText = new Text("");
    MenuBar menuBar = new MenuBar();
    if (arcId == null || arcId.isEmpty()) {
      return addMenuBar("50000", 1);
    }
    RiskMethods riskMethods = new RiskMethods();

    ComponentEventListener<ClickEvent<MenuItem>> menuListener =
        e -> selectedText.setText(e.getSource().getText());
    MenuItem checkListeIte = menuBar.addItem("Checkliste", menuListener);
    // make checkliste highlighted green
    checkListeIte.addClickListener(
        e -> UI.getCurrent().navigate("/arc42/" + arcId + "/risiken/checkliste"));
    MenuItem bogenItem = menuBar.addItem("Fragebogen", menuListener);
    bogenItem.addClickListener(e -> UI.getCurrent().navigate("/arc42/" + arcId + "/risiken/bogen"));
    MenuItem sWOItem = menuBar.addItem("SWOT-Analyse", menuListener);
    sWOItem.addClickListener(e -> UI.getCurrent().navigate("/arc42/" + arcId + "/risiken/SWOT"));
    MenuItem risikoMapItem = menuBar.addItem("Risiko-Map", menuListener);
    risikoMapItem.addClickListener(
        e -> UI.getCurrent().navigate("/arc42/" + arcId + "/risiken/RisikoMap"));
    menuBar.addThemeVariants(MenuBarVariant.LUMO_CONTRAST);

    switch (currentlySelected) {
      case 1:
        checkListeIte.addThemeNames(PRIMARY);
        break;
      case 2:
        bogenItem.addThemeNames(PRIMARY);
        break;
      case 3:
        sWOItem.addThemeNames(PRIMARY);
        break;
      case 4:
        risikoMapItem.addThemeNames(PRIMARY);
        break;
      default:
        break;
    }

    return menuBar;
  }

  public static Button addHelper(Dialog dialog) {
    Icon help = new Icon(VaadinIcon.INFO_CIRCLE);

    Button helpButton = new Button(help, e -> dialog.open());
    helpButton.addThemeVariants(ButtonVariant.LUMO_ICON);
    helpButton.getElement().setAttribute("aria-label", "Add item");
    return helpButton;
  }

  /**
   * Checks if the value contains the searchTerm.
   *
   * @param text the value to check
   * @param searchTerm the searchTerm to check
   * @return true if the value contains the searchTerm
   */
  public static boolean matchesTerm(String text, String searchTerm) {
    return text.toLowerCase().contains(searchTerm.toLowerCase());
  }

  public static VerticalLayout addHeadline(String headlineText, Button help) {
    H3 headline = new H3(headlineText + "       ");
    headline.getStyle().set("font-size", "25px");
    VerticalLayout titleLayout = new VerticalLayout();
    headline.add(help);
    titleLayout.add(headline);
    titleLayout.setPadding(false);
    titleLayout.getStyle().set("height", "40px");
    titleLayout.setHorizontalComponentAlignment(Alignment.CENTER, headline);
    return titleLayout;
  }

  public static Button backToOverview(String arcId) {

    Button backToOverview = new Button("Zurück zur Übersicht");
    backToOverview.addClickListener(e -> UI.getCurrent().navigate("/arc42/" + arcId + "/risiken"));
    backToOverview.getStyle().set("align-self", "end");
    backToOverview.getStyle().set("margin-left", "auto");
    return backToOverview;
  }
}
