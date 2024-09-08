package org.arc42.dokumentation.model.dto.general;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FeLoginDTO {
  private String username;
  private String password;

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public FeLoginDTO(String username, String password) {
    this.username = username;
    this.password = password;
    Logger.getLogger(FeLoginDTO.class.getName()).log(Level.INFO, "username and password were set");
  }

  public String getUsername() {

    return username;
  }

  public String getPassword() { // hashed password
    return password;
  }
}
