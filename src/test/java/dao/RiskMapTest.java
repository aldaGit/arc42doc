package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.arc42.dokumentation.model.dto.documentation.RisikoDTO.Schadenshoehe;
import org.arc42.dokumentation.view.components.documentation.riskComponents.RisikoMapLayout;
import org.junit.jupiter.api.Test;

public class RiskMapTest {
  @Test
  public void convertSchadenshoeheToIntTest() {
    assertEquals(1, RisikoMapLayout.convertSchadenshoeheToInt(Schadenshoehe.GERING));
    assertEquals(2, RisikoMapLayout.convertSchadenshoeheToInt(Schadenshoehe.MITTEL));
    assertEquals(3, RisikoMapLayout.convertSchadenshoeheToInt(Schadenshoehe.HOCH));
    assertEquals(4, RisikoMapLayout.convertSchadenshoeheToInt(Schadenshoehe.SEHRHOCH));
    // assertEquals(-1, RiskMapLayout.convertSchadenshoeheToInt(""));
    assertEquals(-1, RisikoMapLayout.convertSchadenshoeheToInt(null));
  }

  @Test
  public void convertEintrittswToIntTest() {
    String niedrig = "niedrig";
    String mittel = "mittel";
    String hoch = "hoch";
    String sehrHoch = "sehr hoch";
    String keine = "keine";
    String defaultString = "";
    assertEquals(1, RisikoMapLayout.convertEintrittswToInt(niedrig));
    assertEquals(2, RisikoMapLayout.convertEintrittswToInt(mittel));
    assertEquals(3, RisikoMapLayout.convertEintrittswToInt(hoch));
    assertEquals(4, RisikoMapLayout.convertEintrittswToInt(sehrHoch));
    assertEquals(0, RisikoMapLayout.convertEintrittswToInt(keine));
    assertEquals(-1, RisikoMapLayout.convertEintrittswToInt(defaultString));
    assertEquals(-1, RisikoMapLayout.convertEintrittswToInt(null));
  }
}
