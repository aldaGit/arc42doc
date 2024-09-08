package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dao.arc42documentation.Arc42DokuNameDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.RisikoDAO;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.data.NotificationType;

@Route(value = "titel", layout = MainLayout.class)
public class Titel extends DocumentationView {

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    super.beforeEnter(event);
  }

  @Override
  public void beforeLeave(BeforeLeaveEvent event) {
    super.beforeLeave(event);
  }

  @Override
  public void init() {
    super.init();
    getEinfZiele().setOpened(true);
    getEinfZiele()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getTitel().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    DokuNameDTO dokuNameDTO = Arc42DokuNameDAO.getInstance().findById(url);

    Button speichern = new Button("Speichern");
    speichern.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    TextField titelTextField = new TextField("Titel");
    titelTextField.setValue(dokuNameDTO.getName());

    titelTextField.addValueChangeListener(
        (HasValue.ValueChangeListener<AbstractField.ComponentValueChangeEvent<TextField, String>>)
            event -> {
              speichern.setText("Ändern");
              hasChanges(true);
            });

    speichern.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              dokuNameDTO.setName(titelTextField.getValue());
              Arc42DokuNameDAO.getInstance().update(dokuNameDTO);
              new NotificationWindow(
                  "Titel wurde erfolgreich gespeichert!",
                  NotificationType.MEDIUM,
                  NotificationType.SUCCESS);
              hasChanges(false);
              speichern.setText("Speichern");
            });

    // Einbauen von Risiken-Tab
    Select<String> risikoidSelect = new Select<>();
    risikoidSelect.setRequiredIndicatorVisible(true);
    List<String> risikoIdListe = new ArrayList<>();
    List<String> dbResult = getAllIdsFromDB();
    risikoIdListe.addAll(dbResult);
    risikoidSelect.setItems(RisikoDAO.getInstance().getAllIds(url));

    HorizontalLayout riskTabReference = new HorizontalLayout();
    Button goToRisikoTab = new Button("Zum Risiken-Tab");
    goToRisikoTab.addClickListener(
        e -> UI.getCurrent().navigate("/arc42/" + dokuNameDTO.getId() + "/risiken"));
    riskTabReference.add(risikoidSelect, goToRisikoTab);

    HorizontalLayout textForRiskTab = new HorizontalLayout();
    textForRiskTab.setWidthFull();
    textForRiskTab.getStyle().set("background-color", "#F0F8FF");
    TextArea chooseRiskText = new TextArea();
    chooseRiskText.setWidthFull();
    chooseRiskText.setValue(
        "Sie können zur weiteren Bearbeitung eine Risiko-ID wählen, um dieses Risiko in den Tab zu"
            + " integrieren. Zur nähreren Dokumentation der Risiken klicken Sie den Button \"Zum"
            + " Risiken-Tab\".");
    chooseRiskText.setReadOnly(true);
    textForRiskTab.add(chooseRiskText);

    add(
        new VerticalLayout(
            new BreadCrumbComponent(new String[] {"Einführung und Ziele", "Titel"}),
            titelTextField,
            speichern,
            textForRiskTab,
            riskTabReference));
  }

  private List<String> getAllIdsFromDB() {
    List<String> res = RisikoDAO.getInstance().getAllBeschreibungen(url);
    return res != null ? res : new ArrayList<>();
  }
}
