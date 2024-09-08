package dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.arc42.dokumentation.view.components.documentation.arc42.Swot;
import org.junit.jupiter.api.Test;

public class SwotTest {
  @Test
  public void counterTest() {
    Swot swot = new Swot();
    assertEquals(0, swot.counterS());
    assertEquals(0, swot.counterW());
    assertEquals(0, swot.counterO());
    assertEquals(0, swot.counterT());
  }
}
