package org.arc42.dokumentation.view.components.documentation;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ButtonComponent extends HorizontalLayout {

  private final Button create;
  private final Button edit;
  private final Button delete;

  public ButtonComponent() {
    create = new Button("Hinzufügen");
    edit = new Button("Ändern");
    delete = new Button("Löschen");

    create.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

    edit.setVisible(false);
    delete.setVisible(false);

    create.addClickShortcut(Key.ENTER);

    add(create, edit, delete);
  }

  public Button getCreate() {
    return create;
  }

  public Button getEdit() {
    return edit;
  }

  public Button getDelete() {
    return delete;
  }

  public void setVisible(boolean visible) {
    create.setVisible(!visible);
    edit.setVisible(visible);
    delete.setVisible(visible);
  }
}
