package org.arc42.dokumentation.view.evaluation.editRules;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import org.arc42.analyse.model.evaluation.regelsaetze.SingleRegelsatzI;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOI;

public class SaveButtonComponent extends HorizontalLayout {

  public SaveButtonComponent(SingleRegelsatzI regelsatz, Binder binder, ARC42DAOI dao) {
    Button saveButton =
        new Button(
            "Speichern",
            event -> {
              try {
                binder.writeBean(regelsatz);
                dao.update(regelsatz);
                Notification.show("Regeln gespeichert");
              } catch (ValidationException e) {
                Notification.show("Regeln konnten nicht gespeichert werden");
              }
            });
    add(saveButton);
  }
}
