package evaluation.roundTripTests;

import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.analyse.model.evaluation.dao.BausteinsichtRegelsatzDAO;
import org.arc42.analyse.model.evaluation.dao.LaufzeitsichtRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.BausteinsichtRegelsatz;
import org.arc42.analyse.model.evaluation.regelsaetze.LaufzeitsichtRegelsatz;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static evaluation.TestUtil.createTestDoku;
import static evaluation.TestUtil.deleteTestDoku;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LaufzeitsichtRegelsatzTest {

    static DokuNameDTO doku;
    static LaufzeitsichtRegelsatzDAO dao;

    @BeforeAll
    static void beforeAll() {
        doku = createTestDoku(new DokuNameDTO("Test"));
        dao = LaufzeitsichtRegelsatzDAO.getInstance();
    }

    @Test
    void saveRegelsatz() {
        LaufzeitsichtRegelsatz regelsatz = dao.save(new LaufzeitsichtRegelsatz(doku.getId()));
        assertEquals(doku.getId(), regelsatz.getArcId());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.LAUFZEITSICHT, regelsatz.getName());
    }

    @Test
    void readRegelsatz() {
        LaufzeitsichtRegelsatz regelsatz = dao.findByArcId(doku.getId());
        assertNotNull(regelsatz);
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.LAUFZEITSICHT, regelsatz.getName());
    }

    @Test
    void updateRegelsatz() {
        LaufzeitsichtRegelsatz regelsatz = dao.findByArcId(doku.getId());
        regelsatz.setGewichtung(50);
        dao.update(regelsatz);
        regelsatz = dao.findByArcId(doku.getId());
        assertEquals(50, regelsatz.getGewichtung());
    }

    @AfterAll
    static void afterAll() {
        deleteTestDoku(doku);
        doku = null;
        dao = null;
    }
}
