package org.arc42.dokumentation.view.util.data;

import java.util.UUID;

public class Risk {
  private String uniqueID = UUID.randomUUID().toString();

  public String getUniqueID() {
    return uniqueID;
  }
}
