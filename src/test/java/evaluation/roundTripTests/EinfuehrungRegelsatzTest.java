package evaluation.roundTripTests;

import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.analyse.model.evaluation.dao.EinfuehrungRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.EinfuehrungRegelsatz;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static evaluation.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EinfuehrungRegelsatzTest {

    static DokuNameDTO doku;
    static EinfuehrungRegelsatzDAO dao;

    @BeforeAll
    static void beforeAll() {
        doku = createTestDoku(new DokuNameDTO("Test"));
        dao = EinfuehrungRegelsatzDAO.getInstance();
    }

    @Test
    void saveRegelsatz() {
        EinfuehrungRegelsatz regelsatz = dao.save(new EinfuehrungRegelsatz(doku.getId()));
        assertEquals(doku.getId(), regelsatz.getArcId());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.EINFUEHRUNG, regelsatz.getName());
    }

    @Test
    void readRegelsatz() {
        EinfuehrungRegelsatz regelsatz = dao.findByArcId(doku.getId());
        assertNotNull(regelsatz);
        assertEquals(1, regelsatz.getMinAufgaben());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.EINFUEHRUNG, regelsatz.getName());
    }

    @Test
    void updateRegelsatz() {
        EinfuehrungRegelsatz regelsatz = dao.findByArcId(doku.getId());
        regelsatz.setMinAufgaben(5);
        regelsatz.setMinStakeholder(8);
        regelsatz.setNeededQualityCriteria(new ArrayList<>(List.of("Eins", "Zwei", "Drei")));
        regelsatz.setGewichtung(10);
        dao.update(regelsatz);
        regelsatz = dao.findByArcId(doku.getId());
        assertEquals(5, regelsatz.getMinAufgaben());
        assertEquals(8, regelsatz.getMinStakeholder());
        assertEquals(new ArrayList<>(List.of("Eins", "Zwei", "Drei")), regelsatz.getNeededQualityCriteria());
        assertEquals(10, regelsatz.getGewichtung());
    }

    @AfterAll
    static void afterAll() {
        deleteTestDoku(doku);
        doku = null;
        dao = null;
    }
}
