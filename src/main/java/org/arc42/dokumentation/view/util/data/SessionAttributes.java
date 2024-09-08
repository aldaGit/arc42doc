package org.arc42.dokumentation.view.util.data;

import com.vaadin.flow.component.UI;

import org.arc42.dokumentation.model.user.ProfilDTO;
import org.jetbrains.annotations.Nullable;

public final class SessionAttributes {

  public static @Nullable ProfilDTO getCurrentUser() {
    try {
      return (ProfilDTO) UI.getCurrent().getSession().getAttribute(Links.CURRENT_USER);
    } catch (NullPointerException e) {
      System.out.println("No current user found");
      return null;
    }
  }
}
