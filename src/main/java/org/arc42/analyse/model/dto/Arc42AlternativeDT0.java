package org.arc42.analyse.model.dto;

public class Arc42AlternativeDT0 {

  private final String title;
  private String id;

  public Arc42AlternativeDT0(String id) {
    this.id = id;
    this.title = null;
  }

  public Arc42AlternativeDT0(String id, String title) {
    this.id = id;
    this.title = title;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  @Override
  public String toString() {
    return "Arc42AlternativeDT0{" + "id='" + id + '\'' + ", title='" + title + '\'' + '}';
  }
}
