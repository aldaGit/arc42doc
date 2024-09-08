package evaluation.roundTripTests;

import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.analyse.model.evaluation.dao.BausteinsichtRegelsatzDAO;
import org.arc42.analyse.model.evaluation.dao.KontextRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.BausteinsichtRegelsatz;
import org.arc42.analyse.model.evaluation.regelsaetze.KontextRegelsatz;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static evaluation.TestUtil.createTestDoku;
import static evaluation.TestUtil.deleteTestDoku;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BausteinsichtRegelsatzTest {

    static DokuNameDTO doku;
    static BausteinsichtRegelsatzDAO dao;

    @BeforeAll
    static void beforeAll() {
        doku = createTestDoku(new DokuNameDTO("Test"));
        dao = BausteinsichtRegelsatzDAO.getInstance();
    }

    @Test
    void saveRegelsatz() {
        BausteinsichtRegelsatz regelsatz = dao.save(new BausteinsichtRegelsatz(doku.getId()));
        assertEquals(doku.getId(), regelsatz.getArcId());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.BAUSTEINSICHT, regelsatz.getName());
    }

    @Test
    void readRegelsatz() {
        BausteinsichtRegelsatz regelsatz = dao.findByArcId(doku.getId());
        assertNotNull(regelsatz);
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.BAUSTEINSICHT, regelsatz.getName());
    }

    @Test
    void updateRegelsatz() {
        BausteinsichtRegelsatz regelsatz = dao.findByArcId(doku.getId());
        regelsatz.setGewichtung(100);
        dao.update(regelsatz);
        regelsatz = dao.findByArcId(doku.getId());
        assertEquals(100, regelsatz.getGewichtung());
    }

    @AfterAll
    static void afterAll() {
        deleteTestDoku(doku);
        doku = null;
        dao = null;
    }
}
