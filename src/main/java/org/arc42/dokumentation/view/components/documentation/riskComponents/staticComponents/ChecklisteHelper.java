package org.arc42.dokumentation.view.components.documentation.riskComponents.staticComponents;

import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextAreaVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO;

public class ChecklisteHelper {

  public static TextField addSearchField(GridListDataView<RisikoDTO> allGridData) {
    TextField searchField = new TextField();
    searchField.setWidthFull();
    searchField.setPlaceholder("Nach Risiko-ID suchen");
    searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
    searchField.setValueChangeMode(ValueChangeMode.EAGER);
    searchField.addValueChangeListener(e -> allGridData.refreshAll());
    searchField.setHelperText("Geben Sie hier die gesuchte Risiko-ID ein.");
    return searchField;
  }

  public static TextArea addTextArea() {
    TextArea textarea = new TextArea();
    textarea.setWidthFull();
    textarea.setValue(
        "Sie müssen alle vier Felder zur Risikobeschreibung, Schadenshöhe,"
            + " Eintrittswahrscheinlichkeit und Status ausfüllen. ");
    textarea.setReadOnly(true);
    textarea.addThemeVariants(TextAreaVariant.LUMO_ALIGN_CENTER);
    return textarea;
  }
}
