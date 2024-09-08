package evaluation.roundTripTests;

import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.analyse.model.evaluation.dao.EntwurfsentscheidungenRegelsatzDAO;
import org.arc42.analyse.model.evaluation.dao.KonzepteRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.EntwurfsentscheidungenRegelsatz;
import org.arc42.analyse.model.evaluation.regelsaetze.KonzepteRegelsatz;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.arc42.dokumentation.view.util.BadgeGlossary;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static evaluation.TestUtil.createTestDoku;
import static evaluation.TestUtil.deleteTestDoku;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EntwurfsentscheidungenRegelsatzTest {

    static DokuNameDTO doku;
    static EntwurfsentscheidungenRegelsatzDAO dao;

    @BeforeAll
    static void beforeAll() {
        doku = createTestDoku(new DokuNameDTO("Test"));
        dao = EntwurfsentscheidungenRegelsatzDAO.getInstance();
    }

    @Test
    void saveRegelsatz() {
        EntwurfsentscheidungenRegelsatz regelsatz = dao.save(new EntwurfsentscheidungenRegelsatz(doku.getId()));
        assertEquals(doku.getId(), regelsatz.getArcId());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.ENTWURFSENTSCHEIDUNGEN, regelsatz.getName());
    }

    @Test
    void readRegelsatz() {
        EntwurfsentscheidungenRegelsatz regelsatz = dao.findByArcId(doku.getId());
        assertNotNull(regelsatz);
        assertEquals(1, regelsatz.getSollEntwurfsentscheidungen());
        assertEquals(1, regelsatz.getMinEntwurfsentscheidungen());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.ENTWURFSENTSCHEIDUNGEN, regelsatz.getName());
    }

    @Test
    void updateRegelsatz() {
        EntwurfsentscheidungenRegelsatz regelsatz = dao.findByArcId(doku.getId());
        regelsatz.setSollEntwurfsentscheidungen(5);
        regelsatz.setSollEntwurfsentscheidungen(12); // should not work
        regelsatz.setGewichtung(90);
        regelsatz.setGewichtung(1120); // should not work
        dao.update(regelsatz);
        regelsatz = dao.findByArcId(doku.getId());
        assertEquals(5, regelsatz.getSollEntwurfsentscheidungen());
        assertEquals(90, regelsatz.getGewichtung());
    }

    @AfterAll
    static void afterAll() {
        deleteTestDoku(doku);
        doku = null;
        dao = null;
    }
}
