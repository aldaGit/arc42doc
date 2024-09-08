package org.arc42.dokumentation.view.evaluation;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.Route;
import org.arc42.dokumentation.model.dao.arc42documentation.SustainabilityDashboardService;
import org.arc42.dokumentation.view.components.documentation.main.Arc42DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationAbstractView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@CssImport("./themes/softwaredocumentation/views/my-styles.css")
@Route(value = "arc42/:arcId?/bewertung/nachhaltigkeit", layout = MainLayout.class)
public class SustainabilityResult extends DocumentationAbstractView {

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    super.beforeEnter(event);
    if (url == null) {
      if (event.getRouteParameters().get("arcId") == null) {
        event.rerouteTo(Arc42DocumentationView.class);
      } else {
        url = event.getRouteParameters().get("arcId").get();
        this.init();
      }
    }
  }

  @Override
  protected void init() {
    SustainabilityDashboardService sustainabilityDashboardService =
        SustainabilityDashboardService.getInstance(url);
    VerticalLayout sustainabilityDashboard =
        sustainabilityDashboardService.createSustainabilityDashboard();
    Button backButton = new Button("Zurück");
    backButton.addClickListener(
        event -> {
          UI.getCurrent().navigate("arc42/" + url + "/bewertung/ergebnisse");
        });
    VerticalLayout verticalLayout = new VerticalLayout(backButton, sustainabilityDashboard);
    add(verticalLayout);
  }

  @Override
  public void beforeLeave(BeforeLeaveEvent beforeLeaveEvent) {}
}
