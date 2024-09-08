package org.arc42.dokumentation.view.components.documentation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Route;
import java.util.List;
import java.util.Optional;
import org.arc42.dokumentation.model.dao.arc42documentation.MeetingDAO;
import org.arc42.dokumentation.model.dto.documentation.MeetingDTO;
import org.arc42.dokumentation.view.components.custom.BreadCrumbComponent;
import org.arc42.dokumentation.view.components.custom.NotificationWindow;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;
import org.arc42.dokumentation.view.util.data.NotificationType;

@Route(value = "loesungsstrategie/organisatorisch", layout = MainLayout.class)
public class ARC42LoesungsstrategieOrganisatorisch extends DocumentationView {

  private MeetingDTO meeting;
  private MeetingDAO dao;
  private TextField meetingName;
  private IntegerField frequency;
  private ComboBox<String> repetition;
  private ComboBox<String> meetingType;
  private Grid<MeetingDTO> meetingGrid;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (event.getRouteParameters().get("arcId").isPresent()) {
      event.getRouteParameters().get("arcId").get();
    }
    super.beforeEnter(event);
  }

  @Override
  public void init() {
    super.init();
    getLoesungsstrategie()
        .getSummary()
        .getElement()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    getLoesungsstrategie().setOpened(true);
    getLoesungsstrategieOrganisatorisch()
        .getStyle()
        .set(ElementConstants.STYLE_COLOR, "var(--lumo-primary-text-color)");
    VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.setSizeFull();

    H3 header = new H3("Projektorganisation");

    // Abruf der Meetings
    dao = MeetingDAO.getInstance();
    List<MeetingDTO> meetings = dao.findAll(url);

    // Tabelle
    meetingGrid = new Grid<>();
    meetingGrid.addColumn(MeetingDTO::getId).setHeader("ID");
    meetingGrid.addColumn(MeetingDTO::getMeetingName).setHeader("Meeting");
    meetingGrid.addColumn(MeetingDTO::getRegularity).setHeader("Wiederholung");
    meetingGrid.addColumn(MeetingDTO::getMeetingType).setHeader("Art des Meetings");
    meetingGrid.setItems(meetings);
    meetingGrid.setAllRowsVisible(true);

    // Form
    FormLayout form = new FormLayout();
    meetingName = new TextField("Meeting");
    meetingName.setRequired(true);
    frequency = new IntegerField("Häufigkeit");
    frequency.setValue(1);
    frequency.setMin(1);
    frequency.setMax(350);
    frequency.setStepButtonsVisible(true);
    repetition = new ComboBox<>("Zyklus");
    repetition.setRequired(true);
    repetition.setItems(MeetingDTO.getRepetitions());
    meetingType = new ComboBox<>("Art des Meetings");
    meetingType.setRequired(true);
    meetingType.setItems(MeetingDTO.getMeetingTypes());
    form.setResponsiveSteps(
        // Use one column by default
        new FormLayout.ResponsiveStep("0", 1),
        // Use two columns, if the layout's width exceeds 320px
        new FormLayout.ResponsiveStep("320px", 2),
        // Use three columns, if the layout's width exceeds 500px
        new FormLayout.ResponsiveStep("500px", 3));
    form.add(meetingName, 3);
    form.add(frequency, repetition, meetingType);

    // Buttons
    Button addMeeting = new Button("Hinzufügen");
    Button edit = new Button("Ändern");
    Button delete = new Button("Löschen");
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    edit.setVisible(false);
    delete.setVisible(false);
    HorizontalLayout buttons = new HorizontalLayout(addMeeting, edit, delete);
    form.add(buttons);

    verticalLayout.add(
        new BreadCrumbComponent(new String[] {"Lösungsstrategie", "Projektorganisation"}));
    verticalLayout.add(header, meetingGrid, form);
    add(verticalLayout);

    // Click & Selection-Events
    meetingGrid.addSelectionListener(
        selectionEvent -> {
          Optional<MeetingDTO> optionalDoku = selectionEvent.getFirstSelectedItem();
          if (optionalDoku.isPresent()) {
            meeting = optionalDoku.get();
            meetingName.setValue(meeting.getMeetingName());
            frequency.setValue(meeting.getFrequency());
            repetition.setValue(meeting.getRepetition());
            meetingType.setValue(meeting.getMeetingType());
            addMeeting.setVisible(false);
            edit.setVisible(true);
            delete.setVisible(true);
          } else {
            clearTextFieldValue();
            addMeeting.setVisible(true);
            edit.setVisible(false);
            delete.setVisible(false);
          }
        });

    addMeeting.addClickListener(
        buttonClickEvent -> {
          if (!(meetingName.isEmpty()
              || repetition.getValue().isEmpty()
              || meetingType.getValue().isEmpty())) {
            MeetingDTO meetingDTO =
                new MeetingDTO(
                    meetingName.getValue(),
                    frequency.getValue(),
                    repetition.getValue(),
                    meetingType.getValue());
            dao.save(meetingDTO);
            clearTextFieldValue();
            meetingGrid.deselectAll();
            updateList();
          } else {
            new NotificationWindow(
                "Es müssen alle Felder ausgefüllt werden!",
                NotificationType.SHORT,
                NotificationType.NEUTRAL);
          }
        });

    edit.addClickListener(
        buttonClickEvent -> {
          meeting.setMeetingName(meetingName.getValue());
          meeting.setFrequency(frequency.getValue());
          meeting.setRepetition(repetition.getValue());
          meeting.setMeetingType(meetingType.getValue());
          dao.update(meeting);
          clearTextFieldValue();
          updateList();
          new NotificationWindow(
              "Meeting erfolgreich geändert!", NotificationType.SHORT, NotificationType.SUCCESS);
        });

    delete.addClickListener(
        buttonClickEvent -> {
          dao.delete(meeting);
          clearTextFieldValue();
          updateList();
          new NotificationWindow(
              "Meeting erfolgreich gelöscht!", NotificationType.SHORT, NotificationType.NEUTRAL);
        });
  }

  private void updateList() {
    meetingGrid.setItems(dao.findAll(url));
  }

  private void clearTextFieldValue() {
    meetingName.clear();
    frequency.setValue(1);
    repetition.clear();
    meetingType.clear();
  }
}
