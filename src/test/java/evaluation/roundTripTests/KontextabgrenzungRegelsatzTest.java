package evaluation.roundTripTests;

import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.analyse.model.evaluation.dao.KontextRegelsatzDAO;
import org.arc42.analyse.model.evaluation.dao.RandbedingungenRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.KontextRegelsatz;
import org.arc42.analyse.model.evaluation.regelsaetze.RandbedingungenRegelsatz;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static evaluation.TestUtil.createTestDoku;
import static evaluation.TestUtil.deleteTestDoku;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class KontextabgrenzungRegelsatzTest {

    static DokuNameDTO doku;
    static KontextRegelsatzDAO dao;

    @BeforeAll
    static void beforeAll() {
        doku = createTestDoku(new DokuNameDTO("Test"));
        dao = KontextRegelsatzDAO.getInstance();
    }

    @Test
    void saveRegelsatz() {
        KontextRegelsatz regelsatz = dao.save(new KontextRegelsatz(doku.getId()));
        assertEquals(doku.getId(), regelsatz.getArcId());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.KONTEXTABGRENZUNG, regelsatz.getName());
    }

    @Test
    void readRegelsatz() {
        KontextRegelsatz regelsatz = dao.findByArcId(doku.getId());
        assertNotNull(regelsatz);
        assertEquals(1, regelsatz.getSollFachlich());
        assertEquals(1, regelsatz.getSollTechnisch());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.KONTEXTABGRENZUNG, regelsatz.getName());
    }

    @Test
    void updateRegelsatz() {
        KontextRegelsatz regelsatz = dao.findByArcId(doku.getId());
        regelsatz.setSollTechnisch(10);
        regelsatz.setSollFachlich(2);
        regelsatz.setGewichtung(23);
        dao.update(regelsatz);
        regelsatz = dao.findByArcId(doku.getId());
        assertEquals(10, regelsatz.getSollTechnisch());
        assertEquals(2, regelsatz.getSollFachlich());
        assertEquals(23, regelsatz.getGewichtung());
    }

    @AfterAll
    static void afterAll() {
        deleteTestDoku(doku);
        doku = null;
        dao = null;
    }
}
