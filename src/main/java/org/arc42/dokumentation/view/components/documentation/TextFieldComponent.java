package org.arc42.dokumentation.view.components.documentation;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class TextFieldComponent extends HorizontalLayout {

  private final TextField textField1;
  private final TextField textField2;

  public TextFieldComponent(String text1, String text2) {
    textField1 = new TextField(text1);
    textField2 = new TextField(text2);
    textField1.focus();

    this.add(textField1, textField2);
  }

  public void clear() {
    textField1.clear();
    textField2.clear();
  }

  // Getter
  public TextField getTextField1() {
    return textField1;
  }

  public TextField getTextField2() {
    return textField2;
  }

  public boolean isEmpty() {
    if (getTextField1().getValue().isEmpty() && getTextField2().getValue().isEmpty()) {
      return true;
    }
    return false;
  }
}
