package evaluation.roundTripTests;

import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.analyse.model.evaluation.dao.KonzepteRegelsatzDAO;
import org.arc42.analyse.model.evaluation.dao.VerteilungssichtRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.KonzepteRegelsatz;
import org.arc42.analyse.model.evaluation.regelsaetze.VerteilungssichtRegelsatz;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.arc42.dokumentation.view.util.BadgeGlossary;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static evaluation.TestUtil.createTestDoku;
import static evaluation.TestUtil.deleteTestDoku;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class KonzepteRegelsatzTest {

    static DokuNameDTO doku;
    static KonzepteRegelsatzDAO dao;

    @BeforeAll
    static void beforeAll() {
        doku = createTestDoku(new DokuNameDTO("Test"));
        dao = KonzepteRegelsatzDAO.getInstance();
    }

    @Test
    void saveRegelsatz() {
        KonzepteRegelsatz regelsatz = dao.save(new KonzepteRegelsatz(doku.getId()));
        assertEquals(doku.getId(), regelsatz.getArcId());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.KONZEPTE, regelsatz.getName());
    }

    @Test
    void readRegelsatz() {
        KonzepteRegelsatz regelsatz = dao.findByArcId(doku.getId());
        assertNotNull(regelsatz);
        assertEquals(1, regelsatz.getNeededConceptCategories().size());
        assertEquals(BadgeGlossary.CONCEPTBADGES.get(0), regelsatz.getNeededConceptCategories().get(0));
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.KONZEPTE, regelsatz.getName());
    }

    @Test
    void updateRegelsatz() {
        KonzepteRegelsatz regelsatz = dao.findByArcId(doku.getId());
        regelsatz.setNeededConceptCategories(BadgeGlossary.CONCEPTBADGES.subList(1,4));
        regelsatz.setGewichtung(2);
        dao.update(regelsatz);
        regelsatz = dao.findByArcId(doku.getId());
        assertEquals(BadgeGlossary.CONCEPTBADGES.subList(1,4), regelsatz.getNeededConceptCategories());
        assertEquals(2, regelsatz.getGewichtung());
    }

    @AfterAll
    static void afterAll() {
        deleteTestDoku(doku);
        doku = null;
        dao = null;
    }
}
