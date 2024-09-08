package org.arc42.analyse.model.evaluation.dao;

import java.util.LinkedHashMap;
import java.util.List;
import org.arc42.analyse.model.evaluation.Regelwerk;
import org.arc42.analyse.model.evaluation.TabGlossar;
import org.arc42.analyse.model.evaluation.regelsaetze.*;
import org.arc42.dokumentation.model.dao.arc42documentation.ARC42DAOAbstract;

public class RegelwerkDAO extends ARC42DAOAbstract<Regelwerk, String> {

  private static RegelwerkDAO instance;

  public RegelwerkDAO() {
    super();
  }

  public static RegelwerkDAO getInstance() {
    if (instance == null) {
      instance = new RegelwerkDAO();
    }
    return instance;
  }

  @Override
  public Regelwerk save(Regelwerk regelwerk) {
    LinkedHashMap<String, SingleRegelsatzI> savedRegelsaetze = new LinkedHashMap<>();

    EinfuehrungRegelsatz ezRegelsatz =
        EinfuehrungRegelsatzDAO.getInstance().save(regelwerk.getEinfuehrungRegelsatz());
    savedRegelsaetze.put(TabGlossar.EINFUEHRUNG, ezRegelsatz);

    RandbedingungenRegelsatz rbRegelsatz =
        RandbedingungenRegelsatzDAO.getInstance().save(regelwerk.getRandbedingungenRegelsatz());
    savedRegelsaetze.put(TabGlossar.RANDBEDINGUNGEN, rbRegelsatz);

    KontextRegelsatz kaRegelsatz =
        KontextRegelsatzDAO.getInstance().save(regelwerk.getKontextRegelsatz());
    savedRegelsaetze.put(TabGlossar.KONTEXTABGRENZUNG, kaRegelsatz);

    LoesungsstrategieRegelsatz lstrRegelsatz =
        LoesungsstrategieRegelsatzDAO.getInstance().save(regelwerk.getLoesungsstrategieRegelsatz());
    savedRegelsaetze.put(TabGlossar.LOESUNGSSTRATEGIE, lstrRegelsatz);

    BausteinsichtRegelsatz bsRegelsatz =
        BausteinsichtRegelsatzDAO.getInstance().save(regelwerk.getBausteinsichtRegelsatz());
    savedRegelsaetze.put(TabGlossar.BAUSTEINSICHT, bsRegelsatz);

    LaufzeitsichtRegelsatz lsRegelsatz =
        LaufzeitsichtRegelsatzDAO.getInstance().save(regelwerk.getLaufzeitsichtRegelsatz());
    savedRegelsaetze.put(TabGlossar.LAUFZEITSICHT, lsRegelsatz);

    VerteilungssichtRegelsatz vsRegelsatz =
        VerteilungssichtRegelsatzDAO.getInstance().save(regelwerk.getVerteilungssichtRegelsatz());
    savedRegelsaetze.put(TabGlossar.VERTEILUNGSSICHT, vsRegelsatz);

    KonzepteRegelsatz kRegelsatz =
        KonzepteRegelsatzDAO.getInstance().save(regelwerk.getKonzepteRegelsatz());
    savedRegelsaetze.put(TabGlossar.KONZEPTE, kRegelsatz);

    EntwurfsentscheidungenRegelsatz eeRegelsatz =
        EntwurfsentscheidungenRegelsatzDAO.getInstance()
            .save(regelwerk.getEntwurfsentscheidungenRegelsatz());
    savedRegelsaetze.put(TabGlossar.ENTWURFSENTSCHEIDUNGEN, eeRegelsatz);

    QualitaetsszenarienRegelsatz qsRegelsatz =
        QualitaetsszenarienRegelsatzDAO.getInstance()
            .save(regelwerk.getQualitaetsszenarienRegelsatz());
    savedRegelsaetze.put(TabGlossar.QUALITAETSSZENARIEN, qsRegelsatz);

    RisikenRegelsatz rnRegelsatz =
        RisikenRegelsatzDAO.getInstance().save(regelwerk.getRisikenRegelsatz());
    savedRegelsaetze.put(TabGlossar.RISIKEN, rnRegelsatz);

    GlossarRegelsatz glRegelsatz =
        GlossarRegelsatzDAO.getInstance().save(regelwerk.getGlossarRegelsatz());
    savedRegelsaetze.put(TabGlossar.GLOSSAR, glRegelsatz);

    return new Regelwerk(regelwerk.getArcId(), savedRegelsaetze);
  }

  @Override
  public Boolean delete(Regelwerk regelwerk) {
    return null;
  }

  @Override
  public void update(Regelwerk regelwerk) {
    EinfuehrungRegelsatzDAO.getInstance().update(regelwerk.getEinfuehrungRegelsatz());
    RandbedingungenRegelsatzDAO.getInstance().update(regelwerk.getRandbedingungenRegelsatz());
    KontextRegelsatzDAO.getInstance().update(regelwerk.getKontextRegelsatz());
    LoesungsstrategieRegelsatzDAO.getInstance().update(regelwerk.getLoesungsstrategieRegelsatz());
    BausteinsichtRegelsatzDAO.getInstance().update(regelwerk.getBausteinsichtRegelsatz());
    LaufzeitsichtRegelsatzDAO.getInstance().update(regelwerk.getLaufzeitsichtRegelsatz());
    VerteilungssichtRegelsatzDAO.getInstance().update(regelwerk.getVerteilungssichtRegelsatz());
    KonzepteRegelsatzDAO.getInstance().update(regelwerk.getKonzepteRegelsatz());
    EntwurfsentscheidungenRegelsatzDAO.getInstance()
        .update(regelwerk.getEntwurfsentscheidungenRegelsatz());
    QualitaetsszenarienRegelsatzDAO.getInstance()
        .update(regelwerk.getQualitaetsszenarienRegelsatz());
    RisikenRegelsatzDAO.getInstance().update(regelwerk.getRisikenRegelsatz());
    GlossarRegelsatzDAO.getInstance().update(regelwerk.getGlossarRegelsatz());
  }

  @Override
  public List<Regelwerk> findAll(String url) {
    return null;
  }

  @Override
  public Regelwerk findById(String id) {
    return null;
  }

  public Regelwerk findByArcId(String arcId) {
    LinkedHashMap<String, SingleRegelsatzI> regelsaetze = new LinkedHashMap<>();
    regelsaetze.put(
        TabGlossar.EINFUEHRUNG, EinfuehrungRegelsatzDAO.getInstance().findByArcId(arcId));
    regelsaetze.put(
        TabGlossar.RANDBEDINGUNGEN, RandbedingungenRegelsatzDAO.getInstance().findByArcId(arcId));
    regelsaetze.put(
        TabGlossar.KONTEXTABGRENZUNG, KontextRegelsatzDAO.getInstance().findByArcId(arcId));
    regelsaetze.put(
        TabGlossar.LOESUNGSSTRATEGIE,
        LoesungsstrategieRegelsatzDAO.getInstance().findByArcId(arcId));
    regelsaetze.put(
        TabGlossar.BAUSTEINSICHT, BausteinsichtRegelsatzDAO.getInstance().findByArcId(arcId));
    regelsaetze.put(
        TabGlossar.LAUFZEITSICHT, LaufzeitsichtRegelsatzDAO.getInstance().findByArcId(arcId));
    regelsaetze.put(
        TabGlossar.VERTEILUNGSSICHT, VerteilungssichtRegelsatzDAO.getInstance().findByArcId(arcId));
    regelsaetze.put(TabGlossar.KONZEPTE, KonzepteRegelsatzDAO.getInstance().findByArcId(arcId));
    regelsaetze.put(
        TabGlossar.ENTWURFSENTSCHEIDUNGEN,
        EntwurfsentscheidungenRegelsatzDAO.getInstance().findByArcId(arcId));
    regelsaetze.put(
        TabGlossar.QUALITAETSSZENARIEN,
        QualitaetsszenarienRegelsatzDAO.getInstance().findByArcId(arcId));
    regelsaetze.put(TabGlossar.RISIKEN, RisikenRegelsatzDAO.getInstance().findByArcId(arcId));
    regelsaetze.put(TabGlossar.GLOSSAR, GlossarRegelsatzDAO.getInstance().findByArcId(arcId));
    return new Regelwerk(arcId, regelsaetze);
  }
}
