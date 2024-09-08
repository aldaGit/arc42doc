package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import java.util.List;
import java.util.Optional;
import org.arc42.dokumentation.model.dao.arc42documentation.KonventionDAO;
import org.arc42.dokumentation.model.dto.documentation.KonventionDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.data.NotificationType;

@Route(value = "randbedingungen/konventionen", layout = MainLayout.class)
public class Konvention extends DocumentationView {

  private KonventionDAO dao;
  private KonventionDTO konventionDTO;
  private TextField konvention;
  private TextField erlaueterung;
  private Grid<KonventionDTO> konventionG;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    event.getRouteParameters().get("arcId").get();
    super.beforeEnter(event);
  }

  @Override
  public void init() {
    super.init();
    this.dao = KonventionDAO.getInstance();
    getRandbedingunen()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getRandbedingunen().setOpened(true);
    getKonventionen()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    List<KonventionDTO> konventionDTOS = dao.findAll(url);
    this.konventionG = new Grid<>(KonventionDTO.class, false);
    konventionG.addColumn(KonventionDTO::getKonvention).setHeader("Konvention");
    konventionG.addColumn(KonventionDTO::getErlaeuterung).setHeader("Hintergrund");
    this.konventionG.setItems(konventionDTOS);

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    konvention = new TextField("Konvention");
    erlaueterung = new TextField("Hintergrund");
    Button addKonvention = new Button("Hinzufügen");
    addKonvention.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    Button edit = new Button("Ändern");
    Button delete = new Button("Löschen");
    edit.setVisible(false);
    delete.setVisible(false);

    HorizontalLayout buttonLayout = new HorizontalLayout(addKonvention, edit, delete);
    addKonvention.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    addKonvention.addClickShortcut(Key.ENTER);
    horizontalLayout.add(konvention, erlaueterung);

    addKonvention.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              if (!(konvention.getValue().isEmpty() || erlaueterung.getValue().isEmpty())) {

                KonventionDTO konventionDTO =
                    new KonventionDTO(konvention.getValue(), erlaueterung.getValue());
                dao.save(konventionDTO);
                clearTextFieldValue();
                updateList();
              } else {
                new NotificationWindow(
                    "Es müssen alle Eingabefelder ausgefüllt werden!",
                    NotificationType.SHORT,
                    NotificationType.NEUTRAL);
              }
            });

    HorizontalLayout gridLayout = new HorizontalLayout(konventionG);

    konventionG.addSelectionListener(
        (SelectionListener<Grid<KonventionDTO>, KonventionDTO>)
            selection -> {
              Optional<KonventionDTO> optionalDoku = selection.getFirstSelectedItem();
              if (optionalDoku.isPresent()) {
                konventionDTO = optionalDoku.get();
                konvention.setValue(konventionDTO.getKonvention());
                erlaueterung.setValue(konventionDTO.getErlaeuterung());
                addKonvention.setVisible(false);
                edit.setVisible(true);
                delete.setVisible(true);

              } else {
                addKonvention.setVisible(true);
                edit.setVisible(false);
                delete.setVisible(false);
              }
            });

    edit.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              konventionDTO.setKonvention(konvention.getValue());
              konventionDTO.setErlaeuterung(erlaueterung.getValue());
              dao.update(konventionDTO);
              clearTextFieldValue();
              updateList();
              new NotificationWindow(
                  "Konventionen erfolgreich geändert!",
                  NotificationType.SHORT,
                  NotificationType.SUCCESS);
            });

    delete.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              dao.delete(konventionDTO);
              clearTextFieldValue();
              updateList();
              new NotificationWindow(
                  "Konventionen erfolgreich gelöscht!",
                  NotificationType.SHORT,
                  NotificationType.NEUTRAL);
            });
    VerticalLayout main = new VerticalLayout();
    main.setHorizontalComponentAlignment(Alignment.STRETCH, gridLayout);
    main.add(
        new BreadCrumbComponent(new String[] {"Randbedingungen", "Konventionen"}),
        gridLayout,
        horizontalLayout,
        buttonLayout);
    add(main);
  }

  public void updateList() {
    List<KonventionDTO> konventionDTOS = dao.findAll(url);
    this.konventionG.setItems(konventionDTOS);
  }

  private void clearTextFieldValue() {
    konvention.clear();
    erlaueterung.clear();
  }
}
