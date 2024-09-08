package org.arc42.dokumentation.view.account;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Style.TextAlign;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.data.Links;

@Route(value = Links.SETTINGS, layout = MainLayout.class)
@PageTitle("settings")
public class SettingsView extends Div implements BeforeEnterObserver {

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    this.doSetLayout();
  }

  private void doSetLayout() {
    Text settings = new Text("Hier sind bald die Settings. :)");
    settings.getStyle().set("font-size", "20px");
    settings.getStyle().set("margin-top", "20px");
    settings.getStyle().setTextAlign(TextAlign.CENTER);
    this.add(settings);
  }
}