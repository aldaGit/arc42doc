package evaluation.roundTripTests;

import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.analyse.model.evaluation.dao.LaufzeitsichtRegelsatzDAO;
import org.arc42.analyse.model.evaluation.dao.VerteilungssichtRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.LaufzeitsichtRegelsatz;
import org.arc42.analyse.model.evaluation.regelsaetze.VerteilungssichtRegelsatz;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static evaluation.TestUtil.createTestDoku;
import static evaluation.TestUtil.deleteTestDoku;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VerteilungssichtRegelsatzTest {

    static DokuNameDTO doku;
    static VerteilungssichtRegelsatzDAO dao;

    @BeforeAll
    static void beforeAll() {
        doku = createTestDoku(new DokuNameDTO("Test"));
        dao = VerteilungssichtRegelsatzDAO.getInstance();
    }

    @Test
    void saveRegelsatz() {
        VerteilungssichtRegelsatz regelsatz = dao.save(new VerteilungssichtRegelsatz(doku.getId()));
        assertEquals(doku.getId(), regelsatz.getArcId());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.VERTEILUNGSSICHT, regelsatz.getName());
    }

    @Test
    void readRegelsatz() {
        VerteilungssichtRegelsatz regelsatz = dao.findByArcId(doku.getId());
        assertNotNull(regelsatz);
        assertEquals(1, regelsatz.getSollHardware());
        assertEquals(10, regelsatz.getMaxHardware());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.VERTEILUNGSSICHT, regelsatz.getName());
    }

    @Test
    void updateRegelsatz() {
        VerteilungssichtRegelsatz regelsatz = dao.findByArcId(doku.getId());
        regelsatz.setSollHardware(4);
        regelsatz.setGewichtung(13);
        dao.update(regelsatz);
        regelsatz = dao.findByArcId(doku.getId());
        assertEquals(4, regelsatz.getSollHardware());
        assertEquals(13, regelsatz.getGewichtung());
    }

    @AfterAll
    static void afterAll() {
        deleteTestDoku(doku);
        doku = null;
        dao = null;
    }
}
