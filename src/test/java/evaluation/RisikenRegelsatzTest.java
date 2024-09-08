package evaluation;

import org.arc42.analyse.model.evaluation.dao.KontextRegelsatzDAO;
import org.arc42.analyse.model.evaluation.dao.RisikenRegelsatzDAO;
import org.arc42.analyse.model.evaluation.regelsaetze.KontextRegelsatz;
import org.arc42.analyse.model.evaluation.regelsaetze.RisikenRegelsatz;
import org.arc42.dokumentation.model.dao.arc42documentation.FachlichKontextDAO;
import org.arc42.dokumentation.model.dao.arc42documentation.TextEingabeDAO;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.arc42.dokumentation.model.dto.documentation.FachlicherKontextDTO;
import org.arc42.dokumentation.model.dto.documentation.TextEingabeDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static evaluation.TestUtil.createTestDoku;
import static evaluation.TestUtil.deleteTestDoku;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RisikenRegelsatzTest {

    static DokuNameDTO doku;
    static RisikenRegelsatzDAO dao;

    @BeforeAll
    static void beforeAll() {
        doku = createTestDoku(new DokuNameDTO("Test"));
        dao = RisikenRegelsatzDAO.getInstance();
        dao.save(new RisikenRegelsatz(doku.getId()));
    }

    @Test
    void testEvaluate() {
        RisikenRegelsatz regelsatz = dao.findByArcId(doku.getId());
        assertEquals(1, regelsatz.getSollEntries());
        assertEquals(0.0, regelsatz.evaluate().getResult());

        TextEingabeDTO strength = new TextEingabeDTO("Teststaerke", TextEingabeDTO.TEXTTYPE.STRENGTH);
        TextEingabeDAO.getInstance().save(strength, doku.getId());
        TextEingabeDTO weakness = new TextEingabeDTO("Testweakness", TextEingabeDTO.TEXTTYPE.WEAKNESS);
        TextEingabeDAO.getInstance().save(weakness, doku.getId());
        TextEingabeDTO opportunity = new TextEingabeDTO("Testopportunity", TextEingabeDTO.TEXTTYPE.OPPORTUNITY);
        TextEingabeDAO.getInstance().save(opportunity, doku.getId());
        TextEingabeDTO threat = new TextEingabeDTO("Testthreat", TextEingabeDTO.TEXTTYPE.THREAT);
        TextEingabeDAO.getInstance().save(threat, doku.getId());

        assertEquals(0.5, regelsatz.evaluate().getResult());

    }

    @AfterAll
    static void afterAll() {
        deleteTestDoku(doku);
        doku = null;
    }
}
