package org.arc42.dokumentation.view.components.documentation.arc42;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import java.util.List;
import java.util.Optional;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dao.arc42documentation.QualityGoalDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.QualityScenarioDAO;
import org.arc42.dokumentation.model.dto.documentation.QualityDTO;
import org.arc42.dokumentation.model.dto.documentation.QualityGoalDTO;
import org.arc42.dokumentation.model.dto.documentation.QualityScenarioDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.QualityCriteriaComponent;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@CssImport(value = "./themes/softwaredocumentation/views/my-styles.css", themeFor = "vaadin-grid")
@Route(value = "quality/szenarien", layout = MainLayout.class)
public class QualitySzenario extends DocumentationView {

  private QualityScenarioDTO qualityScenarioDTO;
  private ARC42DAOAbstract dao;
  private QualityCriteriaComponent qualityCriteria;

  TextArea scenarioName;
  TextArea stimulus;
  TextArea reaction;
  TextArea response;
  ComboBox<QualityDTO> selectQualityGoal;
  ComboBox<String> priority;
  ComboBox<String> risk;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    event.getRouteParameters().get("arcId").get();
    super.beforeEnter(event);
  }

  @Override
  public void init() {
    Grid<QualityScenarioDTO> qualityScenarioG;
    Button delete;
    Button create;
    super.init();
    getQualitaetsszenarien()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getQualitaetsszenarien().setOpened(true);
    getQualitaetsszenario()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");

    H4 header =
        new H4(
            new Text("Folgende Qualitätsziele wurden in: "),
            new BreadCrumbComponent(new String[] {"Einführung und Ziele", "Qualitätsziele"}),
            new Text("definiert:"));

    dao = QualityGoalDAO.getInstance();
    List<QualityDTO> qualityGoalDTOS = dao.findAll(url);
    Grid<QualityDTO> qualityGoalG = new Grid(QualityGoalDTO.class, false);
    qualityGoalG.addColumn(QualityDTO::getId).setHeader("Id").setFlexGrow(0);
    qualityGoalG.addColumn(QualityDTO::getQualitaetsziel).setHeader("Qualitätsziel");
    qualityGoalG.addColumn(QualityDTO::getMotivation).setHeader("Motivation");
    qualityGoalG
        .addColumn(new ComponentRenderer<>(QualityDTO::generateBadge))
        .setHeader("Kategorie");
    qualityGoalG.setItems(qualityGoalDTOS);
    qualityGoalG.setAllRowsVisible(true);
    qualityGoalG.setSelectionMode(Grid.SelectionMode.NONE);

    create = new Button("Szenario erstellen");

    Binder<QualityScenarioDTO> binder = new Binder<>(QualityScenarioDTO.class);

    scenarioName = new TextArea("Szenario Name");
    stimulus = new TextArea("Auslöser");
    reaction = new TextArea("Reaktion");
    response = new TextArea("Zielwert");
    HorizontalLayout horizontalLayout1 =
        new HorizontalLayout(scenarioName, stimulus, reaction, response);
    horizontalLayout1.setPadding(false);

    selectQualityGoal = new ComboBox<>("Qualitätsziel");
    selectQualityGoal.setItems(qualityGoalDTOS);
    selectQualityGoal.setItemLabelGenerator(QualityDTO::getId);

    priority = new ComboBox<>("Priorität");
    priority.setItems(QualityDTO.getPriority());
    risk = new ComboBox<>("Risiko");
    risk.setItems(QualityDTO.getPriority());

    binder
        .forField(scenarioName)
        .bind(QualityScenarioDTO::getScenarioName, QualityScenarioDTO::setScenarioName);
    binder
        .forField(stimulus)
        .bind(QualityScenarioDTO::getStimulus, QualityScenarioDTO::setStimulus);

    binder
        .forField(reaction)
        .bind(QualityScenarioDTO::getReaction, QualityScenarioDTO::setReaction);
    binder
        .forField(response)
        .bind(QualityScenarioDTO::getResponse, QualityScenarioDTO::setResponse);
    binder
        .forField(priority)
        .bind(QualityScenarioDTO::getCurrentPriority, QualityScenarioDTO::setPriority);
    binder.forField(risk).bind(QualityScenarioDTO::getRisk, QualityScenarioDTO::setRisk);
    binder
        .forField(selectQualityGoal)
        .bind(QualityScenarioDTO::getQualityGoalDTO, QualityScenarioDTO::setQualityGoalDTO);

    dao = QualityScenarioDAO.getInstance();
    qualityScenarioG = new Grid(QualityScenarioDTO.class, false);
    qualityCriteria = new QualityCriteriaComponent(qualityScenarioG, dao);
    qualityCriteria.addCreateListener();

    qualityScenarioG
        .addColumn(QualityScenarioDTO::getId)
        .setHeader("Id")
        .setFlexGrow(0)
        .setSortable(true);
    qualityScenarioG
        .addColumn(
            qualityScenarioDTO1 ->
                qualityScenarioDTO1.getQualityGoalDTO() != null
                    ? qualityScenarioDTO1.getQualityGoalDTO().getId()
                    : null)
        .setHeader("Zugehöriges Qualitätsziel")
        .setFlexGrow(0)
        .setResizable(true);
    qualityScenarioG
        .addColumn(new ComponentRenderer<>(QualityScenarioDTO::generateBadge))
        .setHeader("Kategorie")
        .setAutoWidth(true)
        .setResizable(true);
    qualityScenarioG
        .addColumn(QualityScenarioDTO::getScenarioName)
        .setHeader("Qualitätsszenario")
        .setResizable(true);
    qualityScenarioG
        .addColumn(QualityScenarioDTO::getStimulus)
        .setHeader("Stimulus")
        .setResizable(true);
    qualityScenarioG
        .addColumn(QualityScenarioDTO::getReaction)
        .setHeader("Reaktion")
        .setResizable(true);
    qualityScenarioG
        .addColumn(QualityScenarioDTO::getResponse)
        .setHeader("Zielwert")
        .setResizable(true);
    qualityScenarioG
        .addColumn(QualityScenarioDTO::getCurrentPriority)
        .setHeader("Priorität")
        .setFlexGrow(0)
        .setAutoWidth(true);
    qualityScenarioG
        .addColumn(QualityScenarioDTO::getRisk)
        .setHeader("Risiko")
        .setFlexGrow(0)
        .setResizable(true);

    List<QualityScenarioDTO> qualityScenarioDTOS = dao.findAll(url);
    qualityScenarioG.setItems(qualityScenarioDTOS);
    qualityScenarioG.setAllRowsVisible(true);
    HorizontalLayout horizontalLayout =
        new HorizontalLayout(selectQualityGoal, priority, risk, qualityCriteria);

    qualityScenarioG.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

    selectQualityGoal.addValueChangeListener(
        (HasValue.ValueChangeListener<
                AbstractField.ComponentValueChangeEvent<ComboBox<QualityDTO>, QualityDTO>>)
            event -> qualityCriteria.setVisible(event.getValue() == null));

    create.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              if (qualityCriteria.getSelectedBatch().getComponentCount() == 0
                  && qualityCriteria.isVisible()) {
                new NotificationWindow(
                    "Sie müssen mindestens einen Kategorie auswählen!", 4000, "error");
              } else {
                QualityScenarioDTO qualityScenarioDTO =
                    new QualityScenarioDTO(
                        "",
                        "",
                        qualityCriteria.isVisible()
                            ? qualityCriteria.getCategories(qualityCriteria.getSelectedBatch())
                            : null);
                boolean saved = binder.writeBeanIfValid(qualityScenarioDTO);
                if (saved) {
                  dao = QualityScenarioDAO.getInstance();
                  dao.createRelationship(qualityScenarioDTO);
                  scenarioName.clear();
                  stimulus.clear();
                  reaction.clear();
                  response.clear();
                  risk.clear();
                  selectQualityGoal.clear();
                  priority.clear();
                  qualityCriteria.clearBatch();
                  qualityScenarioG.setItems(dao.findAll(url));
                } else {
                  new NotificationWindow("Fehler beim Speichern!", 4000, "error");
                }
              }
            });

    delete = new Button("Löschen");
    // update = new Button("Ändern");
    delete.setVisible(false);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    // update.setVisible(false);
    qualityScenarioG.addSelectionListener(
        (SelectionListener<Grid<QualityScenarioDTO>, QualityScenarioDTO>)
            selectionEvent -> {
              Optional<QualityScenarioDTO> optionalDoku = selectionEvent.getFirstSelectedItem();
              if (optionalDoku.isPresent()) {
                create.setVisible(false);
                delete.setVisible(true);
                // update.setVisible(true);

                hideFields(false);
                qualityScenarioDTO = optionalDoku.get();
                qualityCriteria.addButtonListener(qualityScenarioDTO);
              } else {
                create.setVisible(true);
                delete.setVisible(false);
                hideFields(true);
              }
              qualityCriteria.selectedCell(qualityScenarioDTO, optionalDoku.isPresent());
            });

    delete.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              dao.delete(qualityScenarioDTO);
              qualityScenarioG.setItems(dao.findAll(url));
            });

    VerticalLayout verticalLayout = new VerticalLayout();
    VerticalLayout verticalLayout1 =
        new VerticalLayout(horizontalLayout1, horizontalLayout, create, delete);
    HorizontalLayout horizontalLayout2 = new HorizontalLayout(verticalLayout1, qualityCriteria);

    verticalLayout.add(
        new BreadCrumbComponent(new String[] {"Qualitätsszenarien", "Szenarien"}),
        header,
        qualityGoalG,
        qualityScenarioG,
        horizontalLayout2);
    verticalLayout.setSizeFull();
    add(verticalLayout);
  }

  private void hideFields(boolean hide) {
    scenarioName.setVisible(hide);
    stimulus.setVisible(hide);
    reaction.setVisible(hide);
    response.setVisible(hide);
    selectQualityGoal.setVisible(hide);
    priority.setVisible(hide);
    risk.setVisible(hide);
    qualityCriteria.setVisible(hide);
  }
}
