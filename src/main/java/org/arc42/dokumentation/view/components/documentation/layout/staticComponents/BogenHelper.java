package org.arc42.dokumentation.view.components.documentation.layout.staticComponents;

import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextAreaVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import java.util.ArrayList;
import java.util.List;
import org.arc42.dokumentation.model.dao.arc42documentation.RisikoDAO;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO;

public class BogenHelper {
  public static List<String> getAllIdsFromDB(String url) {
    List<String> res = RisikoDAO.getInstance().getAllBeschreibungen(url);
    return res != null ? res : new ArrayList<>();
  }

  public static List<String> getAllBeschreibungen(String url) {
    List<String> res = RisikoDAO.getInstance().getAllIds(url);
    return res != null ? res : new ArrayList<>();
  }

  public static String getDescriptionText() {
    return "Zu jedem eingetragenen Risiko in der Checkliste kann optional je ein Fragebogen"
        + " ausgefüllt werden. Der Fragebogen erweiteret das eingetragene Risiko um"
        + " detailliertere Daten und Informationen des Risikos. Verpflichtend muss eine"
        + " Risiko-ID ausgewählt und das Feld des Erfassers ausgefüllt werden. Die"
        + " Informationen für einen Fragebogen können beispeilsweise aus einem"
        + " Experteninterview gewonnen werden. ";
  }

  public static TextArea addHinsweis() {
    TextArea hinweis = new TextArea();
    hinweis.setValue(
        "Die Textfelder \"Risiko-ID\" und \"Erfasser\" müssen verpflichtend ausgefüllt werden. Die"
            + " Risikobeschreibung ergänzt sich aus dem gesetzten Eintrag der Checkliste.");
    hinweis.setReadOnly(true);
    hinweis.setWidthFull();
    hinweis.addThemeVariants(TextAreaVariant.LUMO_ALIGN_CENTER);
    return hinweis;
  }

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
}
