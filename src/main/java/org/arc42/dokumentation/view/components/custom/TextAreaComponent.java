package org.arc42.dokumentation.view.components.custom;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import java.util.ArrayList;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;
import org.arc42.dokumentation.model.dto.documentation.ImageDTO;
import org.arc42.dokumentation.model.dto.documentation.KonzeptDTO;
import org.arc42.dokumentation.view.components.documentation.main.DocumentationView;
import org.arc42.dokumentation.view.util.data.NotificationType;

public class TextAreaComponent<T> extends VerticalLayout {

  final H3 header;
  private final TextArea textArea;

  public TextArea getTextArea() {
    return textArea;
  }

  public TextAreaComponent(String title, String body, ARC42DAOAbstract<T, String> dao) {

    header = new H3(title);
    textArea = new TextArea();
    if (body != null && !body.trim().isEmpty()) {
      textArea.setValue(body);
    }
    textArea.setSizeFull();

    Button save = new Button("Speichern");
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    save.addClickListener(
        (ComponentEventListener<ClickEvent<Button>>)
            event -> {
              if (textArea.getValue().isEmpty()) {
                new NotificationWindow(
                    "Sie müssen eine Beschreibung angeben und dann speichern!",
                    NotificationType.SHORT,
                    NotificationType.ERROR);
              } else {
                T imageDTO = dao.findById(null);
                if (imageDTO == null) {
                  try {
                    imageDTO = (T) new KonzeptDTO("", textArea.getValue(), new ArrayList<>());
                  } catch (Exception e) {
                    imageDTO = (T) new ImageDTO(textArea.getValue());
                    ((ImageDTO) imageDTO).setDescription(textArea.getValue());
                  }
                }
                ((ImageDTO) imageDTO).setDescription(textArea.getValue());
                dao.save(imageDTO);
                new NotificationWindow(
                    "Erfolgreich gespeichert!", NotificationType.SHORT, NotificationType.SUCCESS);
              }
              new DocumentationView().hasChanges(false);
            });

    setSizeFull();
    add(header, textArea, save);
  }
}
