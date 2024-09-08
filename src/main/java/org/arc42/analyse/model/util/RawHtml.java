package org.arc42.analyse.model.util;

import com.vaadin.flow.component.html.Div;

public class RawHtml extends Div {
  public void setHtml(String html) {
    getElement().setProperty("innerHTML", html);
  }
}
