package evaluation.roundTripTests;

import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.analyse.model.evaluation.dao.GlossarRegelsatzDAO;
import org.arc42.analyse.model.evaluation.dao.RisikenRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.GlossarRegelsatz;
import org.arc42.analyse.model.evaluation.regelsaetze.RisikenRegelsatz;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static evaluation.TestUtil.createTestDoku;
import static evaluation.TestUtil.deleteTestDoku;
import static org.junit.jupiter.api.Assertions.*;

public class GlossarRegelsatzTest {

    static DokuNameDTO doku;
    static GlossarRegelsatzDAO dao;

    @BeforeAll
    static void beforeAll() {
        doku = createTestDoku(new DokuNameDTO("Test"));
        dao = GlossarRegelsatzDAO.getInstance();
    }

    @Test
    void saveRegelsatz() {
        GlossarRegelsatz regelsatz = dao.save(new GlossarRegelsatz(doku.getId()));
        assertEquals(doku.getId(), regelsatz.getArcId());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.GLOSSAR, regelsatz.getName());
    }

    @Test
    void readRegelsatz() {
        GlossarRegelsatz regelsatz = dao.findByArcId(doku.getId());
        assertNotNull(regelsatz);
        assertEquals(1, regelsatz.getMinWortanzahl());
        assertEquals(100, regelsatz.getMaxWortanzahl());
        assertFalse(regelsatz.isChecked());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.GLOSSAR, regelsatz.getName());
    }

    @Test
    void updateRegelsatz() {
        GlossarRegelsatz regelsatz = dao.findByArcId(doku.getId());
        regelsatz.setMinWortanzahl(20);
        regelsatz.setMaxWortanzahl(40);
        regelsatz.setChecked(true);
        regelsatz.setGewichtung(77);
        dao.update(regelsatz);
        regelsatz = dao.findByArcId(doku.getId());
        assertEquals(20, regelsatz.getMinWortanzahl());
        assertEquals(40, regelsatz.getMaxWortanzahl());
        assertTrue(regelsatz.isChecked());
        assertEquals(77, regelsatz.getGewichtung());
    }

    @AfterAll
    static void afterAll() {
        deleteTestDoku(doku);
        doku = null;
        dao = null;
    }
}
