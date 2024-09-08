package org.arc42.dokumentation.model.dto.general;

public class DbUserDTO {
  private static DbUserDTO instance;

  private DbUserDTO() {}

  public static DbUserDTO getInstance() {
    if (instance == null) {
      instance = new DbUserDTO();
    }
    return instance;
  }

  private String username = "";

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
