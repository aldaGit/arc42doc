package org.arc42.dokumentation.view.components.documentation;

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
import org.arc42.dokumentation.model.dao.arc42documentation.OekologischDAO;
import org.arc42.dokumentation.model.dto.documentation.OekologischDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.data.NotificationType;

@Route(value = "randbedingungen/oekologisch", layout = MainLayout.class)
public class ARC42Oekologisch extends DocumentationView {

  private OekologischDAO dao;
  private Grid<OekologischDTO> oekoG;
  private OekologischDTO oekologischDTO;
  private TextField randbedingung;
  private TextField hintergrund;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    event.getRouteParameters().get("arcId").get();
    super.beforeEnter(event);
  }

  @Override
  public void init() {
    super.init();
    this.dao = OekologischDAO.getInstance();
    getRandbedingunen()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getRandbedingunen().setOpened(true);
    getOekologisch().getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    List<OekologischDTO> oekologischDTOs = dao.findAll(url);
    this.oekoG = new Grid<>(OekologischDTO.class, false);
    oekoG.addColumn(OekologischDTO::getRandbedingung).setHeader("Ökologische Randbedingung");
    oekoG.addColumn(OekologischDTO::getHintergrund).setHeader("Hintergrund");
    oekoG.setItems(oekologischDTOs);

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    randbedingung = new TextField("Ökologische Randbedingung");
    hintergrund = new TextField("Hintergrund");
    Button addRandbedingung = new Button("Hinzufügen");
    addRandbedingung.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    Button edit = new Button("Ändern");
    Button delete = new Button("Löschen");
    edit.setVisible(false);
    delete.setVisible(false);

    HorizontalLayout buttonLayout = new HorizontalLayout(addRandbedingung, edit, delete);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    addRandbedingung.addClickShortcut(Key.ENTER);
    horizontalLayout.add(randbedingung, hintergrund);

    addRandbedingung.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              if (!(randbedingung.getValue().isEmpty() || hintergrund.getValue().isEmpty())) {
                OekologischDTO oekologischDTO =
                    new OekologischDTO(randbedingung.getValue(), hintergrund.getValue());
                dao.save(oekologischDTO);
                clearTextFieldValue();
                updateList();
              } else {
                new NotificationWindow(
                    "Es müssen alle Eingabefelder ausgefüllt werden!",
                    NotificationType.SHORT,
                    NotificationType.NEUTRAL);
              }
            });

    HorizontalLayout gridLayout = new HorizontalLayout(oekoG);

    oekoG.addSelectionListener(
        (SelectionListener<Grid<OekologischDTO>, OekologischDTO>)
            selection -> {
              Optional<OekologischDTO> optionalDoku = selection.getFirstSelectedItem();
              if (optionalDoku.isPresent()) {
                oekologischDTO = optionalDoku.get();
                randbedingung.setValue(oekologischDTO.getRandbedingung());
                hintergrund.setValue(oekologischDTO.getHintergrund());
                addRandbedingung.setVisible(false);
                edit.setVisible(true);
                delete.setVisible(true);

              } else {
                addRandbedingung.setVisible(true);
                edit.setVisible(false);
                delete.setVisible(false);
              }
            });

    edit.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              oekologischDTO.setRandbedingung(randbedingung.getValue());
              oekologischDTO.setHintergrund(hintergrund.getValue());
              dao.update(oekologischDTO);
              clearTextFieldValue();
              updateList();
              new NotificationWindow(
                  "Ökologische Randbedingung erfolgreich geändert!",
                  NotificationType.SHORT,
                  NotificationType.SUCCESS);
            });

    delete.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              dao.delete(oekologischDTO);
              clearTextFieldValue();
              updateList();
              new NotificationWindow(
                  "Ökologische Randbedingung erfolgreich gelöscht!",
                  NotificationType.SHORT,
                  NotificationType.NEUTRAL);
            });
    VerticalLayout main = new VerticalLayout();
    main.setHorizontalComponentAlignment(Alignment.STRETCH, gridLayout);
    main.add(
        new BreadCrumbComponent(new String[] {"Randbedingungen", "Ökologisch"}),
        gridLayout,
        horizontalLayout,
        buttonLayout);
    add(main);
  }

  public void updateList() {
    List<OekologischDTO> oekologischDTOs = dao.findAll(url);
    this.oekoG.setItems(oekologischDTOs);
  }

  private void clearTextFieldValue() {
    randbedingung.clear();
    hintergrund.clear();
  }
}
