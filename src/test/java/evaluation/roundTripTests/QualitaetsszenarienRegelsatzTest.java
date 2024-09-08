package evaluation.roundTripTests;

import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.analyse.model.evaluation.dao.EntwurfsentscheidungenRegelsatzDAO;
import org.arc42.analyse.model.evaluation.dao.QualitaetsszenarienRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.EntwurfsentscheidungenRegelsatz;
import org.arc42.analyse.model.evaluation.regelsaetze.QualitaetsszenarienRegelsatz;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static evaluation.TestUtil.createTestDoku;
import static evaluation.TestUtil.deleteTestDoku;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QualitaetsszenarienRegelsatzTest {

    static DokuNameDTO doku;
    static QualitaetsszenarienRegelsatzDAO dao;

    @BeforeAll
    static void beforeAll() {
        doku = createTestDoku(new DokuNameDTO("Test"));
        dao = QualitaetsszenarienRegelsatzDAO.getInstance();
    }

    @Test
    void saveRegelsatz() {
        QualitaetsszenarienRegelsatz regelsatz = dao.save(new QualitaetsszenarienRegelsatz(doku.getId()));
        assertEquals(doku.getId(), regelsatz.getArcId());
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.QUALITAETSSZENARIEN, regelsatz.getName());
    }

    @Test
    void readRegelsatz() {
        QualitaetsszenarienRegelsatz regelsatz = dao.findByArcId(doku.getId());
        assertNotNull(regelsatz);
        assertEquals(8.33, regelsatz.getGewichtung());
        assertEquals(TabGlossar.QUALITAETSSZENARIEN, regelsatz.getName());
    }

    @Test
    void updateRegelsatz() {
        QualitaetsszenarienRegelsatz regelsatz = dao.findByArcId(doku.getId());
        regelsatz.setGewichtung(20);
        dao.update(regelsatz);
        regelsatz = dao.findByArcId(doku.getId());
        assertEquals(20, regelsatz.getGewichtung());
    }

    @AfterAll
    static void afterAll() {
        deleteTestDoku(doku);
        doku = null;
        dao = null;
    }
}
