package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.arc42.dokumentation.model.dao.arc42documentation.RisikoDAO;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO.Eintrittswahrscheinlichkeit;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO.Schadenshoehe;
import org.arc42.dokumentation.model.dto.documentation.RisikoDTO.Status;
import org.arc42.dokumentation.view.components.documentation.arc42.Checkliste;
import org.junit.jupiter.api.Test;


public class ChecklisteTest {

  @Test
  public void findAllByArcIdTest() {

    RisikoDAO risikoDAO = RisikoDAO.getInstance();
    List<RisikoDTO> risikoDTOList = risikoDAO.findAllByArcId("10000000");
    assertNotNull(risikoDTOList);
    assertEquals(0, risikoDTOList.size());
    // stellt sicher, dass wir nicht das NULL-Result von Cypher weitergeben sondern stets eine
    // Liste, auch bei leerem ergebnis
  }

  @Test
  public void isValidTest() {
    RisikoDTO risiko = new RisikoDTO();
    Checkliste checkliste = new Checkliste();
    checkliste.setUrlAndArcID("100000");
    checkliste.init();
    checkliste.populateFieldsFromDTO(risiko);
    assertFalse(checkliste.isValid());
    risiko.setAnforderung("Test");
    risiko.setStatus(Status.ERLEDIGT);
    risiko.setEintrittswahrscheinlichkeit(Eintrittswahrscheinlichkeit.KEINE);
    risiko.setSchadenshoehe(Schadenshoehe.HOCH);
    checkliste.populateFieldsFromDTO(risiko);
    assertTrue(checkliste.isValid());
  }

  @Test
  public void compareRisikoDTOs() {
    RisikoDTO r1 = new RisikoDTO();
    RisikoDTO r2 = new RisikoDTO();
    r1.setAnforderung("Test");
    r1.setStatus(Status.ERLEDIGT);
    r1.setEintrittswahrscheinlichkeit(Eintrittswahrscheinlichkeit.HOCH);
    r1.setSchadenshoehe(Schadenshoehe.HOCH);
    // r1 and r2 should differ in anforderung, status, eWK and Schadenshoehe
    assertNotEquals(r1.getAnforderung(), r2.getAnforderung());
    assertNotEquals(r1.getStatus(), r2.getStatus());
    assertNotEquals(r1.getEintrittswahrscheinlichkeit(), r2.getEintrittswahrscheinlichkeit());
    assertNotEquals(r1.getSchadenshoehe(), r2.getSchadenshoehe());

    Checkliste checkliste = new Checkliste();
    checkliste.setUrlAndArcID("100000");
    checkliste.init();
    // from r1 into r2
    checkliste.populateFieldsFromDTO(r1);
    checkliste.readFieldsIntoDTO(r2);
    // r1 and r2 should be equal in anforderung, status, eWK and Schadenshoehe
    assertEquals(r1.getAnforderung(), r2.getAnforderung());
    assertEquals(r1.getStatus(), r2.getStatus());
    assertEquals(r1.getEintrittswahrscheinlichkeit(), r2.getEintrittswahrscheinlichkeit());
    assertEquals(r1.getSchadenshoehe(), r2.getSchadenshoehe());
  }
}
