package org.arc42.dokumentation.view.evaluation.editRules;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.arc42.analyse.model.evaluation.dao.RegelwerkDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.SingleRegelsatzI;
import org.arc42.dokumentation.view.components.documentation.main.MainLayout;

@CssImport("./themes/softwaredocumentation/views/my-styles.css")
@Route(value = "gewichtung", layout = MainLayout.class)
public class EditGewichtung extends EditRegelnView {

  private NumberField sumField;
  private Button saveButton;
  private HorizontalLayout fields;

  @Override
  protected void init() {
    super.init();
    gewichtung.getStyle().setColor("var(--lumo-primary-text-color");

    VerticalLayout verticalLayout = new VerticalLayout();
    H3 header = new H3("Gewichtung der Kategorien");
    verticalLayout.add(header);
    fields = new HorizontalLayout();
    VerticalLayout left = new VerticalLayout();
    VerticalLayout right = new VerticalLayout();
    fields.add(left, right);
    Collection<SingleRegelsatzI> regelsaetze =
        regelwerkControl.getRegelwerk(url).getRegelsaetze().values();
    double sum = 0.0;
    int counter = 0;
    for (SingleRegelsatzI regelsatz : regelsaetze) {
      ++counter;
      System.out.println(counter + regelsatz.getName());
      NumberField gewichtungField = new NumberField(regelsatz.getName());
      gewichtungField.setMin(0);
      Binder<SingleRegelsatzI> binder = new Binder<>();
      binder.bind(
          gewichtungField, SingleRegelsatzI::getGewichtung, SingleRegelsatzI::setGewichtung);
      binder.readBean(regelsatz);
      if (counter >= 7) {
        right.add(gewichtungField);
      } else {
        left.add(gewichtungField);
      }
      sum += regelsatz.getGewichtung();
      gewichtungField.addValueChangeListener(
          event -> {
            try {
              binder.writeBean(regelsatz);
            } catch (ValidationException e) {
              Notification.show("Unerlaubter Wert");
            }
            updateSumField();
          });
    }
    sumField = new NumberField("Summe");
    sumField.setReadOnly(true);
    sumField.setValue(Math.round(sum * 10.0) / 10.0);

    saveButton =
        new Button(
            "Speichern",
            event -> {
              try {
                RegelwerkDAO.getInstance().update(regelwerkControl.getRegelwerk(url));
                Notification.show("Gewichtung gespeichert");
              } catch (Exception e) {
                System.out.println(e.getMessage());
                Notification.show("Gewichtung konnte nicht gespeichert werden");
              }
            });

    Button resetButton =
        new Button(
            "Reset",
            event -> {
              List<Component> components = new ArrayList<>();
              for (Component field : left.getChildren().toList()) {
                components.add(field);
              }
              for (Component field : right.getChildren().toList()) {
                components.add(field);
              }
              for (Component field : components) {
                ((NumberField) field).setValue(8.33);
              }
            });

    TextArea erklaerung = new TextArea("Erklärung zur Gewichtung");
    erklaerung.setValue(
        "Hier kann die Gewichtung der Kategorien für die Gesamtbewertung eingestellt werden. In den"
            + " Eingabefeldern kann für jede Kategorie ein Prozentwert zwischen 0 und 100 angegeben"
            + " werden. Die Summe der Felder muss (gerundet) 100 betragen, um die Gewichtung"
            + " speichern zu können. Mit dem Reset-Button kann die Gewichtung für jede Kategorie"
            + " wieder auf den Standardwert 8.33% gesetzt werden.");
    erklaerung.setReadOnly(true);
    erklaerung.setWidth("400px");

    HorizontalLayout buttons = new HorizontalLayout(saveButton, resetButton);
    VerticalLayout chartAndButtons = new VerticalLayout(erklaerung, sumField, buttons);
    chartAndButtons.setAlignItems(Alignment.CENTER);
    HorizontalLayout gesamt = new HorizontalLayout(fields, chartAndButtons);
    verticalLayout.add(gesamt);
    verticalLayout.setAlignItems(Alignment.CENTER);
    add(verticalLayout);
  }

  private void updateSumField() {
    boolean valuesValid = true;
    double sum = 0.0;
    for (Component component : fields.getChildren().toList().get(0).getChildren().toList()) {
      if (component instanceof NumberField field
          && !field.isReadOnly()
          && field.getValue() != null) {
        if (field.isInvalid()) {
          valuesValid = false;
        }
        sum += field.getValue();
      }
    }
    for (Component component : fields.getChildren().toList().get(1).getChildren().toList()) {
      if (component instanceof NumberField field
          && !field.isReadOnly()
          && field.getValue() != null) {
        if (field.isInvalid()) {
          valuesValid = false;
        }
        sum += field.getValue();
      }
    }
    double roundedSum = Math.round(sum * 10.0) / 10.0;
    saveButton.setEnabled(roundedSum == 100 && valuesValid);
    sumField.setValue(roundedSum);
  }
}
