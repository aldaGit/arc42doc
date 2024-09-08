package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arc42.dokumentation.model.dto.documentation.RisikoDTO;
import org.arc42.dokumentation.view.components.documentation.arc42.Bogen;
import org.arc42.dokumentation.view.components.documentation.layout.staticComponents.BogenHelper;
import org.junit.jupiter.api.Test;

public class BogenTest {

  @Test
  public void isValidErfasserTest() {
    // test isValidErfasser from Bogen (whitebox test)
    String nullString = "null";
    String empty = "";
    String valid = "Marianne";
    String invalid = "\"\"";
    assertFalse(Bogen.isValidErfasser(nullString));
    assertFalse(Bogen.isValidErfasser(empty));
    assertFalse(Bogen.isValidErfasser(invalid));
    assertTrue(Bogen.isValidErfasser(valid));
  }

  @Test
  public void isValidTestNegative() {
    Bogen bogen = new Bogen();
    bogen.init();
    bogen.populateFieldsFromDTO(new RisikoDTO());
    assertFalse(bogen.isValid());
  }

  @Test
  public void isValidTestPositive() {
    Bogen bogen = new Bogen();
    bogen.init();
    RisikoDTO risk = new RisikoDTO();
    bogen.populateFieldsFromDTO(risk);
    assertFalse(bogen.isValid());
    // set erfasser and risikobeschreibung to valid values
    risk.setErfasser("Marianne");
    risk.setAnforderung("TestBeschreibung");
    bogen.populateFieldsFromDTO(risk);
    assertTrue(bogen.isValid());
  }

  // Test for BogenHelper
  @Test
  public void getDescriptionTextTest() {
    assertNotEquals("", BogenHelper.getDescriptionText());
    String text =
        "Zu jedem eingetragenen Risiko in der Checkliste kann optional je ein Fragebogen"
            + " ausgefüllt werden. Der Fragebogen erweiteret das eingetragene Risiko um"
            + " detailliertere Daten und Informationen des Risikos. Verpflichtend muss eine"
            + " Risiko-ID ausgewählt und das Feld des Erfassers ausgefüllt werden. Die"
            + " Informationen für einen Fragebogen können beispeilsweise aus einem"
            + " Experteninterview gewonnen werden. ";
    assertEquals(text, BogenHelper.getDescriptionText());
  }

  @Test
  public void addHinsweisTest() {
    assertNotEquals(null, BogenHelper.addHinsweis());
    assertEquals(
        "Die Textfelder \"Risiko-ID\" und \"Erfasser\" müssen verpflichtend ausgefüllt werden. Die"
            + " Risikobeschreibung ergänzt sich aus dem gesetzten Eintrag der Checkliste.",
        BogenHelper.addHinsweis().getValue());
  }
}
