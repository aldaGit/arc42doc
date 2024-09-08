package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.*;
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
import org.arc42.dokumentation.model.dao.arc42documentation.GlossarDAO;
import org.arc42.dokumentation.model.dto.documentation.GlossarEintragDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.data.NotificationType;

@Route(value = "glossar", layout = MainLayout.class)
public class Glossar extends DocumentationView {

  private GlossarEintragDTO dto;
  private GlossarDAO dao;

  private Grid<GlossarEintragDTO> grid;
  private TextField begriff;
  private TextField beschreibung;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    String arcId = event.getRouteParameters().get("arcId").get();
    super.beforeEnter(event);
  }

  @Override
  public void init() {
    super.init();
    getGlossar()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getGlossar().setOpened(true);

    VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setSizeFull();

    dao = GlossarDAO.getInstance();
    List<GlossarEintragDTO> glossarDTOS = this.dao.findAll(url);

    grid = new Grid<>(GlossarEintragDTO.class, false);
    grid.addColumn(GlossarEintragDTO::getBegriff).setHeader("Begriff");
    grid.addColumn(GlossarEintragDTO::getBeschreibung).setHeader("Beschreibung");
    grid.setItems(glossarDTOS);

    HorizontalLayout horizontalLayout = new HorizontalLayout();
    begriff = new TextField("Begriff");
    begriff.addFocusShortcut(Key.ENTER);
    begriff.focus();
    beschreibung = new TextField("Beschreibung");

    Button addGlossarEintrag = new Button("Hinzufügen");
    addGlossarEintrag.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    addGlossarEintrag.addClickShortcut(Key.ENTER);
    horizontalLayout.add(begriff, beschreibung);

    Button edit = new Button("Ändern");
    Button delete = new Button("Löschen");
    edit.setVisible(false);
    delete.setVisible(false);
    addGlossarEintrag.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    addGlossarEintrag.addClickShortcut(Key.ENTER);

    HorizontalLayout buttonLayout = new HorizontalLayout(addGlossarEintrag, edit, delete);
    HorizontalLayout gridLayout = new HorizontalLayout(grid);

    addGlossarEintrag.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              if (!(begriff.getValue().isEmpty())) {
                GlossarEintragDTO glossarEintragDTO =
                    new GlossarEintragDTO(begriff.getValue(), beschreibung.getValue());
                dao.save(glossarEintragDTO);
                clearTextFieldValue();
                grid.deselectAll();
                updateList();
              } else {
                new NotificationWindow(
                    "Das Feld Begriff muss ausgefüllt werden!",
                    NotificationType.SHORT,
                    NotificationType.NEUTRAL);
              }
            });

    grid.addSelectionListener(
        (SelectionListener<Grid<GlossarEintragDTO>, GlossarEintragDTO>)
            selection -> {
              Optional<GlossarEintragDTO> optionalDoku = selection.getFirstSelectedItem();
              if (optionalDoku.isPresent()) {
                dto = optionalDoku.get();
                begriff.setValue(dto.getBegriff());
                beschreibung.setValue(dto.getBeschreibung());
                addGlossarEintrag.setVisible(false);
                edit.setVisible(true);
                delete.setVisible(true);
              } else {
                addGlossarEintrag.setVisible(true);
                edit.setVisible(false);
                delete.setVisible(false);
              }
            });

    edit.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              dto.setBegriff(begriff.getValue());
              dto.setBeschreibung(beschreibung.getValue());
              dao.update(dto);
              clearTextFieldValue();
              updateList();
              new NotificationWindow(
                  "Glossar-Eintrag erfolgreich geändert!",
                  NotificationType.SHORT,
                  NotificationType.SUCCESS);
            });

    delete.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              dao.delete(dto);
              clearTextFieldValue();
              updateList();
              new NotificationWindow(
                  "Glossar-Eintrag erfolgreich gelöscht!",
                  NotificationType.SHORT,
                  NotificationType.NEUTRAL);
            });

    VerticalLayout main = new VerticalLayout();
    main.setHorizontalComponentAlignment(Alignment.STRETCH, gridLayout);
    main.add(
        new BreadCrumbComponent(new String[] {"Glossar"}),
        gridLayout,
        horizontalLayout,
        buttonLayout);
    add(main);
  }

  public void updateList() {
    List<GlossarEintragDTO> glossarEintragDTOS = this.dao.findAll(url);
    this.grid.setItems(glossarEintragDTOS);
  }

  private void clearTextFieldValue() {
    begriff.clear();
    beschreibung.clear();
  }
}
