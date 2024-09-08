package org.arc42.dokumentation.view.components.documentation.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import java.io.IOException;
import java.util.logging.Logger;
import org.arc42.dokumentation.control.logic.GoogleBigQueryController;
import org.arc42.dokumentation.view.util.data.Roles;

public abstract class ReadDocumentionAbstract extends VerticalLayout
    implements BeforeEnterObserver {

  protected final Logger logger;
  protected GoogleBigQueryController googleBigQueryController;

  protected ReadDocumentionAbstract() {

    this.logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    try {
      googleBigQueryController = GoogleBigQueryController.getInstance();
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (event.getUI().getSession().getAttribute(Roles.CURRENTUSER) == null) {
      UI.getCurrent().navigate("login");
    } else {
      init();
    }
  }

  public void init() {}
}
