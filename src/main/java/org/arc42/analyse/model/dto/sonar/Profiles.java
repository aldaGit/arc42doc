package org.arc42.analyse.model.dto.sonar;

import java.util.List;

public class Profiles {

  List<Profile> profiles;

  public List<Profile> getProfiles() {
    return profiles;
  }

  public void setProfiles(List<Profile> params) {
    this.profiles = params;
  }

  public class Profile {
    String key;

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }
  }
}
