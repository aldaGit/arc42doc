package org.arc42.dokumentation.view.components.custom;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public class NotificationWindow extends Notification {

  public NotificationWindow(String text, int duration, String type) {
    Notification notification = new Notification();
    notification.setText(text);
    notification.setDuration(duration);
    notification.setPosition(Position.TOP_END);
    switch (type) {
      case "error":
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        break;
      case "success":
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        break;
      case "neutral":
        notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        break;
    }
    notification.open();
  }
}
