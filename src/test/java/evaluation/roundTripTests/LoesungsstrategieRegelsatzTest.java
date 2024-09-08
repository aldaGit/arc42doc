package evaluation.roundTripTests;

import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.analyse.model.evaluation.dao.KontextRegelsatzDAO;
import org.arc42.analyse.model.evaluation.dao.LoesungsstrategieRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.KontextRegelsatz;
import org.arc42.analyse.model.evaluation.regelsaetze.LoesungsstrategieRegelsatz;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static evaluation.TestUtil.createTestDoku;
import static evaluation.TestUtil.deleteTestDoku;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoesungsstrategieRegelsatzTest {

    static DokuNameDTO doku;
    static LoesungsstrategieRegelsatzDAO dao;

    @BeforeAll
    static void beforeAll() {
        doku = createTestDoku(new DokuNameDTO("Test"));
        dao = LoesungsstrategieRegelsatzDAO.getInstance();
    }

    @Test
    void saveRegelsatz() {
        LoesungsstrategieRegelsatz regelsatz = dao.save(new LoesungsstrategieRegelsatz(doku.getId()));
        assertEquals(doku.getId(), regelsatz.getArcId());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.LOESUNGSSTRATEGIE, regelsatz.getName());
    }

    @Test
    void readRegelsatz() {
        LoesungsstrategieRegelsatz regelsatz = dao.findByArcId(doku.getId());
        assertNotNull(regelsatz);
        assertEquals(1, regelsatz.getSollMeetings());
        assertEquals(10, regelsatz.getMaxMeetings());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.LOESUNGSSTRATEGIE, regelsatz.getName());
    }

    @Test
    void updateRegelsatz() {
        LoesungsstrategieRegelsatz regelsatz = dao.findByArcId(doku.getId());
        regelsatz.setSollMeetings(8);
        regelsatz.setSollMeetings(16); // 16 shouldn't work because max is 10 -> expected 8
        regelsatz.setGewichtung(18);
        dao.update(regelsatz);
        regelsatz = dao.findByArcId(doku.getId());
        assertEquals(8, regelsatz.getSollMeetings());
        assertEquals(18, regelsatz.getGewichtung());
    }

    @AfterAll
    static void afterAll() {
        deleteTestDoku(doku);
        doku = null;
        dao = null;
    }
}
