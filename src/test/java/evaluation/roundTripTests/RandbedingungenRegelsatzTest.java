package evaluation.roundTripTests;

import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.analyse.model.evaluation.dao.EinfuehrungRegelsatzDAO;
import org.arc42.analyse.model.evaluation.dao.RandbedingungenRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.EinfuehrungRegelsatz;
import org.arc42.analyse.model.evaluation.regelsaetze.RandbedingungenRegelsatz;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static evaluation.TestUtil.createTestDoku;
import static evaluation.TestUtil.deleteTestDoku;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RandbedingungenRegelsatzTest {

    static DokuNameDTO doku;
    static RandbedingungenRegelsatzDAO dao;

    @BeforeAll
    static void beforeAll() {
        doku = createTestDoku(new DokuNameDTO("Test"));
        dao = RandbedingungenRegelsatzDAO.getInstance();
    }

    @Test
    void saveRegelsatz() {
        RandbedingungenRegelsatz regelsatz = dao.save(new RandbedingungenRegelsatz(doku.getId()));
        assertEquals(doku.getId(), regelsatz.getArcId());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.RANDBEDINGUNGEN, regelsatz.getName());
    }

    @Test
    void readRegelsatz() {
        RandbedingungenRegelsatz regelsatz = dao.findByArcId(doku.getId());
        assertNotNull(regelsatz);
        assertEquals(1,regelsatz.getSollTechnisch());
        assertEquals(1, regelsatz.getSollKonventionen());
        assertEquals(10, regelsatz.getMaxKonventionen());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.RANDBEDINGUNGEN, regelsatz.getName());
    }

    @Test
    void updateRegelsatz() {
        RandbedingungenRegelsatz regelsatz = dao.findByArcId(doku.getId());
        regelsatz.setSollTechnisch(5);
        regelsatz.setSollOrgan(8);
        regelsatz.setSollKonventionen(0);
        regelsatz.setGewichtung(30);
        dao.update(regelsatz);
        regelsatz = dao.findByArcId(doku.getId());
        assertEquals(5, regelsatz.getSollTechnisch());
        assertEquals(8, regelsatz.getSollOrgan());
        assertEquals(0, regelsatz.getSollKonventionen());
        assertEquals(30, regelsatz.getGewichtung());
    }

    @AfterAll
    static void afterAll() {
        deleteTestDoku(doku);
        doku = null;
        dao = null;
    }
}
