package evaluation.roundTripTests;

import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.analyse.model.evaluation.dao.QualitaetsszenarienRegelsatzDAO;
import org.arc42.analyse.model.evaluation.dao.RisikenRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.QualitaetsszenarienRegelsatz;
import org.arc42.analyse.model.evaluation.regelsaetze.RisikenRegelsatz;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.arc42.dokumentation.view.util.data.Risk;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static evaluation.TestUtil.createTestDoku;
import static evaluation.TestUtil.deleteTestDoku;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RisikenRegelsatzTest {

    static DokuNameDTO doku;
    static RisikenRegelsatzDAO dao;

    @BeforeAll
    static void beforeAll() {
        doku = createTestDoku(new DokuNameDTO("Test"));
        dao = RisikenRegelsatzDAO.getInstance();
    }

    @Test
    void saveRegelsatz() {
        RisikenRegelsatz regelsatz = dao.save(new RisikenRegelsatz(doku.getId()));
        assertEquals(doku.getId(), regelsatz.getArcId());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.RISIKEN, regelsatz.getName());
    }

    @Test
    void readRegelsatz() {
        RisikenRegelsatz regelsatz = dao.findByArcId(doku.getId());
        assertNotNull(regelsatz);
        assertEquals(1, regelsatz.getSollRisiken());
        assertEquals(1, regelsatz.getSollEntries());
        assertEquals(10, regelsatz.getMaxRisiken());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.RISIKEN, regelsatz.getName());
    }

    @Test
    void updateRegelsatz() {
        RisikenRegelsatz regelsatz = dao.findByArcId(doku.getId());
        regelsatz.setSollRisiken(3);
        regelsatz.setSollEntries(10);
        regelsatz.setGewichtung(44.4);
        dao.update(regelsatz);
        regelsatz = dao.findByArcId(doku.getId());
        assertEquals(3, regelsatz.getSollRisiken());
        assertEquals(10, regelsatz.getSollEntries());
        assertEquals(44.4, regelsatz.getGewichtung());
    }

    @AfterAll
    static void afterAll() {
        deleteTestDoku(doku);
        doku = null;
        dao = null;
    }
}
