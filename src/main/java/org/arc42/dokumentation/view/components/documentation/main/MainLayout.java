package org.arc42.dokumentation.view.components.documentation.main;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import org.arc42.dokumentation.control.logic.Login;

@CssImport("themes/softwaredocumentation/styles.css")
public class MainLayout extends AppLayout {

  private Image logo = new Image("logo.png", "logo");

  public MainLayout() {
    createHeader();
  }

  private void createHeader() {
    RouterLink homeLink = new RouterLink("", Arc42DocumentationView.class);
    homeLink.getElement().setProperty("innerHTML", "<h3>Arc42 Dokumentation</h3>");
    Image userIcon = new Image("user1.png", "Icon");
    userIcon.setHeight("35px");
    userIcon.getStyle().set("margin-right", "20px");
    userIcon.getStyle().set("cursor", "pointer");
    MenuBar menuBar = new MenuBar();
    menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
    MenuItem menuItem = menuBar.addItem(userIcon);
    SubMenu subMenu = menuItem.getSubMenu();

    ComponentEventListener<ClickEvent<MenuItem>> navLogout =
        e -> {
          Login.logout();
        };
    ComponentEventListener<ClickEvent<MenuItem>> navSettings =
        e -> {
          getUI().ifPresent(ui -> ui.navigate("settings"));
        };
    ComponentEventListener<ClickEvent<MenuItem>> navHelp =
        e -> {
          getUI().ifPresent(ui -> ui.navigate("help"));
        };
    ComponentEventListener<ClickEvent<MenuItem>> navProfil =
        e -> {
          getUI().ifPresent(ui -> ui.navigate("profil"));
        };

    subMenu.addItem("Mein Profil", navProfil);
    // subMenu.addItem("Settings", navSettings);
    // subMenu.addItem("Help", navHelp);
    subMenu.addItem("Logout", navLogout);
    logo.setHeight("70px");
    logo.setWidth("250px");
    logo.getStyle().set("margin-left", "15px");
    HorizontalLayout home = new HorizontalLayout();
    home.add(logo);

    HorizontalLayout header = new HorizontalLayout(new Div(), home, menuBar, new Div());
    header.getStyle().setBackground("#0BA1E2");
    home.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("arc42View")));
    home.getStyle().set("cursor", "pointer");
    home.getStyle().setBackground("#0BA1E2");
    home.setSpacing(false);
    home.getStyle().set("postion", "relative");

    header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    header.expand(home);
    header.setWidth("100%");
    header.setSpacing(false);

    addToNavbar(header);
  }
}