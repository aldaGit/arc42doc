package org.arc42.dokumentation.view.components.documentation.main;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.ElementConstants;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeLeaveObserver;
import org.arc42.dokumentation.model.dto.documentation.DokuNameDTO;
import org.arc42.dokumentation.view.util.data.Links;
import org.arc42.dokumentation.view.util.data.Roles;

public abstract class DocumentationAbstractView extends HorizontalLayout
    implements BeforeEnterObserver, BeforeLeaveObserver {

  protected DokuNameDTO result;
  protected String url;

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (event.getUI().getSession().getAttribute(Roles.CURRENTUSER) == null) {
      UI.getCurrent().navigate("login");
    }
  }

  protected abstract void init();

  protected Anchor createStyledAnchor(String href, String text) {
    Anchor anchor = new Anchor(Links.ARC42 + "/" + url + "/" + href, text);
    anchor.getStyle().set(ElementConstants.STYLE_COLOR, "var(--lumo-secondary-text-color)");
    return anchor;
  }
}