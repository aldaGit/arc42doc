package org.arc42.analyse.model.dto.sonar;

public class SonarSettingsDTO {

  private String url;
  private String token;
  private String component;

  public SonarSettingsDTO(String url, String token, String component) {
    this.url = url;
    this.token = token;
    this.component = component;
  }

  public SonarSettingsDTO() {}

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }

  public String toString() {
    return "URL: " + this.url + " Token: " + this.token + " Component: " + this.component;
  }

  public boolean equals(SonarSettingsDTO o) {
    return this.url.equals(o.url)
        && this.token.equals(o.token)
        && this.component.equals(o.component);
  }
}
