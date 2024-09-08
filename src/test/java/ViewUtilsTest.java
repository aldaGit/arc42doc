import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arc42.dokumentation.view.components.documentation.layout.staticComponents.ViewUtils;
import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
public class ViewUtilsTest {

  @Test
  public void addMenuBarTest() {
    String arcID = "10000000";
    MenuBar menuBar = ViewUtils.addMenuBar(arcID, 1);
    MenuBar menuBar2 = ViewUtils.addMenuBar(arcID, 1);
    assertNotEquals(null, ViewUtils.addMenuBar(arcID, 1));
    // assertEquals(menuBar, ViewUtils.addMenuBar(arcID));
    menuBar
        .getItems()
        .forEach(
            menuItem -> {
              assertEquals(
                  menuItem.getText(),
                  menuBar2.getItems().get(menuBar.getItems().indexOf(menuItem)).getText());
            });
    // look for checkliste
    assertEquals("Checkliste", menuBar.getItems().get(0).getText());

    // look for bogen
    assertEquals("Fragebogen", menuBar.getItems().get(1).getText());

    // look for swot
    assertEquals("SWOT-Analyse", menuBar.getItems().get(2).getText());

    // look for risikoMap
    assertEquals("Risiko-Map", menuBar.getItems().get(3).getText());
  }

  @Test
  public void addHelperTest() {
    Button helper = ViewUtils.addHelper(new Dialog());
    assertNotNull(helper);
    assertEquals("", helper.getText());
    assertEquals("vaadin:info-circle", helper.getIcon().getElement().getAttribute("icon"));
  }

  @Test
  public void matchesTermTest() {
    String searchTerm = "test";
    String text = "test2345";
    assertTrue(ViewUtils.matchesTerm(text, searchTerm));
  }

  @Test
  public void addHeadlineTest() {
    String headline = "test";
    Button helper = ViewUtils.addHelper(new Dialog());
    VerticalLayout vl = ViewUtils.addHeadline(headline, helper);
    assertNotNull(vl);
    assertEquals(Alignment.CENTER, vl.getHorizontalComponentAlignment(vl.getComponentAt(0)));
    assertEquals(1, vl.getComponentCount());
  }

  @Test
  public void backToOverviewTest() {
    String arcID = "10000000";
    Button backToOverview = ViewUtils.backToOverview(arcID);
    assertNotNull(backToOverview);
    assertEquals("Zurück zur Übersicht", backToOverview.getText());
  }
}
